package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.FilterSettingsEntity

@Dao
interface FilterSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFilter(filter: FilterSettingsEntity)

    @Query("SELECT * FROM filter_settings_table WHERE filterId = :id")
    suspend fun getFilterById(id: Int): FilterSettingsEntity?

    @Query("SELECT * FROM filter_settings_table WHERE filterId = :id")
    fun getFilterFlowById(id: Int): Flow<FilterSettingsEntity?>

    @Query("DELETE FROM filter_settings_table WHERE filterId = :id")
    suspend fun deleteFilterById(id: Int)

    // Метод для быстрого копирования временного фильтра в основной (Применить)
    @Query("""
        INSERT OR REPLACE INTO filter_settings_table (filterId, countryId, countryName, regionId, regionName, industryId, industryName, expectedSalary, notShowWithoutSalary)
        SELECT :targetId, countryId, countryName, regionId, regionName, industryId, industryName, expectedSalary, notShowWithoutSalary
        FROM filter_settings_table WHERE filterId = :sourceId
    """)
    suspend fun copyFilter(sourceId: Int, targetId: Int)
}
