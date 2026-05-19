package com.example.grameenlight.data.repository

import android.content.Context
import com.example.grameenlight.data.local.PoleDao
import com.example.grameenlight.data.local.ComplaintDao
import com.example.grameenlight.data.local.entity.PoleEntity
import com.example.grameenlight.data.local.entity.ComplaintEntity
import com.example.grameenlight.model.Pole
import com.example.grameenlight.model.Complaint
import com.example.grameenlight.model.PoleStatus
import com.example.grameenlight.model.RepairStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import java.io.InputStream

class PoleRepository(
    private val context: Context,
    private val poleDao: PoleDao,
    private val complaintDao: ComplaintDao
) {

    /**
     * Loads the initial poles from the local assets/poles.json file.
     * This ensures the app is never empty.
     */
    suspend fun seedFromAssets() {
        try {
            val inputStream: InputStream = context.assets.open("poles.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            
            val jsonString = String(buffer, Charsets.UTF_8)
            val jsonArray = JSONArray(jsonString)
            
            val poleEntities = mutableListOf<PoleEntity>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                poleEntities.add(PoleEntity(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    posX = obj.getDouble("posX").toFloat(),
                    posY = obj.getDouble("posY").toFloat(),
                    status = obj.getString("status"),
                    repairStatus = RepairStatus.REPORTED.name,
                    lastUpdated = System.currentTimeMillis()
                ))
            }
            
            poleDao.insertPoles(poleEntities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get all poles from local Room DB. 
     */
    fun getAllPoles(): Flow<List<Pole>> = poleDao.getAllPoles().map { entities ->
        entities.map { it.toDomain() }
    }

    /**
     * Get all complaints from local Room DB.
     */
    fun getAllComplaints(): Flow<List<Complaint>> = complaintDao.getAllComplaints().map { entities ->
        entities.map { it.toDomain() }
    }

    /**
     * Save a new report locally.
     */
    suspend fun reportStatus(pole: Pole, status: PoleStatus, complaintId: String) {
        val timestamp = System.currentTimeMillis()
        
        // 1. Update Pole status
        poleDao.updatePoleStatus(pole.id, status.name, timestamp)
        
        if (status == PoleStatus.WORKING) {
            // 2. If it's working now, resolve any active complaints for this pole
            complaintDao.resolveComplaintsForPole(pole.id, RepairStatus.RESOLVED.name)
        } else {
            // 3. Otherwise, insert a new complaint
            val complaintEntity = ComplaintEntity(
                complaintId = complaintId,
                poleId = pole.id,
                statusReported = status.name,
                timestamp = timestamp,
                currentRepairStatus = RepairStatus.REPORTED.name
            )
            complaintDao.insertComplaint(complaintEntity)
        }
    }

    suspend fun clearAllData() {
        poleDao.deleteAllPoles()
        complaintDao.deleteAllComplaints()
    }

    // --- Mapper Helpers ---

    private fun PoleEntity.toDomain() = Pole(
        id = id,
        name = name,
        posX = posX,
        posY = posY,
        status = try { PoleStatus.valueOf(status) } catch(e: Exception) { PoleStatus.WORKING },
        repairStatus = try { RepairStatus.valueOf(repairStatus) } catch(e: Exception) { RepairStatus.REPORTED },
        lastUpdated = lastUpdated
    )

    private fun ComplaintEntity.toDomain() = Complaint(
        complaintId = complaintId,
        poleId = poleId,
        statusReported = try { PoleStatus.valueOf(statusReported) } catch(e: Exception) { PoleStatus.WORKING },
        timestamp = timestamp,
        currentRepairStatus = try { RepairStatus.valueOf(currentRepairStatus) } catch(e: Exception) { RepairStatus.REPORTED }
    )
}
