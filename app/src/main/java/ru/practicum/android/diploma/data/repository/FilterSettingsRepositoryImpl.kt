package ru.practicum.android.diploma.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.FilterSettingsDao
import ru.practicum.android.diploma.data.db.entity.FilterSettingsEntity
import ru.practicum.android.diploma.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterSettings

class FilterSettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val filterSettingsDao: FilterSettingsDao
) : FilterSettingsRepository {

    override fun getFilterSettings(): FilterSettings {
        val json = sharedPreferences.getString(FILTER_SETTINGS_KEY, null)
        return if (json != null) {
            gson.fromJson(json, FilterSettings::class.java)
        } else {
            FilterSettings()
        }
    }

    override suspend fun saveFilterSettings(settings: FilterSettings) {
        val json = gson.toJson(settings)
        sharedPreferences.edit {
            putString(FILTER_SETTINGS_KEY, json)
        }
        filterSettingsDao.saveFilter(settings.toEntity(ID_PERMANENT))
    }

    override fun saveCountry(country: Area) {
        // Метод больше не используется напрямую, так как мы перешли на Room и временные фильтры.
        // Оставляем пустым или удаляем вызовы из ViewModels.
    }

    override fun clearFilterSettings() {
        sharedPreferences.edit {
            remove(FILTER_SETTINGS_KEY)
        }
    }

    // РЕАЛИЗАЦИЯ ЧЕРЕЗ ROOM

    override fun getFilterFlow(): Flow<FilterSettings> {
        return filterSettingsDao.getFilterFlowById(ID_PERMANENT).map { entity ->
            entity?.toDomain() ?: FilterSettings()
        }
    }

    override fun getTempFilterFlow(): Flow<FilterSettings> {
        return filterSettingsDao.getFilterFlowById(ID_TEMPORARY).map { entity ->
            entity?.toDomain() ?: FilterSettings()
        }
    }

    override suspend fun saveTempFilter(settings: FilterSettings) {
        filterSettingsDao.saveFilter(settings.toEntity(ID_TEMPORARY))
    }

    override suspend fun applyTempFilter() {
        filterSettingsDao.copyFilter(ID_TEMPORARY, ID_PERMANENT)
        // Синхронизируем со старыми префами для совместимости
        val temp = filterSettingsDao.getFilterById(ID_TEMPORARY)
        temp?.let { saveFilterSettings(it.toDomain()) }
    }

    override suspend fun initTempFilter() {
        // Копируем из основного во временный при входе на экран
        val permanent = filterSettingsDao.getFilterById(ID_PERMANENT)
        if (permanent != null) {
            filterSettingsDao.saveFilter(permanent.copy(filterId = ID_TEMPORARY))
        } else {
            // Если в базе пусто, пробуем взять из префов (для миграции)
            val settings = getFilterSettings()
            filterSettingsDao.saveFilter(settings.toEntity(ID_PERMANENT))
            filterSettingsDao.saveFilter(settings.toEntity(ID_TEMPORARY))
        }
    }

    override suspend fun clearTempFilter() {
        filterSettingsDao.saveFilter(FilterSettings().toEntity(ID_TEMPORARY))
    }

    private fun FilterSettingsEntity.toDomain(): FilterSettings {
        return FilterSettings(
            countryId = countryId,
            countryName = countryName,
            regionId = regionId,
            regionName = regionName,
            industryId = industryId,
            industryName = industryName,
            expectedSalary = expectedSalary,
            notShowWithoutSalary = notShowWithoutSalary
        )
    }

    private fun FilterSettings.toEntity(id: Int): FilterSettingsEntity {
        return FilterSettingsEntity(
            filterId = id,
            countryId = countryId,
            countryName = countryName,
            regionId = regionId,
            regionName = regionName,
            industryId = industryId,
            industryName = industryName,
            expectedSalary = expectedSalary,
            notShowWithoutSalary = notShowWithoutSalary
        )
    }

    companion object {
        private const val FILTER_SETTINGS_KEY = "filter_settings_key"
        private const val ID_PERMANENT = 1
        private const val ID_TEMPORARY = 2
    }
}
