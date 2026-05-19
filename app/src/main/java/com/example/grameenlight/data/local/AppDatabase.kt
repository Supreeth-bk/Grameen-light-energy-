package com.example.grameenlight.data.local

import androidx.room.*
import com.example.grameenlight.data.local.entity.ComplaintEntity
import com.example.grameenlight.data.local.entity.PoleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoleDao {
    @Query("SELECT * FROM poles")
    fun getAllPoles(): Flow<List<PoleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoles(poles: List<PoleEntity>)

    @Query("UPDATE poles SET status = :status, lastUpdated = :timestamp WHERE id = :poleId")
    suspend fun updatePoleStatus(poleId: String, status: String, timestamp: Long)

    @Query("DELETE FROM poles")
    suspend fun deleteAllPoles()
}

@Dao
interface ComplaintDao {
    @Query("SELECT * FROM complaints ORDER BY timestamp DESC")
    fun getAllComplaints(): Flow<List<ComplaintEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity)

    @Query("UPDATE complaints SET currentRepairStatus = :status WHERE poleId = :poleId AND currentRepairStatus != 'RESOLVED'")
    suspend fun resolveComplaintsForPole(poleId: String, status: String)

    @Query("DELETE FROM complaints")
    suspend fun deleteAllComplaints()
}

@Database(entities = [PoleEntity::class, ComplaintEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun poleDao(): PoleDao
    abstract fun complaintDao(): ComplaintDao
}
