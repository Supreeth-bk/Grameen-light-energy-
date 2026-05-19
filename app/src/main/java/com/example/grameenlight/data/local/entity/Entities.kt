package com.example.grameenlight.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poles")
data class PoleEntity(
    @PrimaryKey val id: String,
    val name: String,
    val posX: Float,
    val posY: Float,
    val status: String,
    val repairStatus: String,
    val lastUpdated: Long
)

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey val complaintId: String,
    val poleId: String,
    val statusReported: String,
    val timestamp: Long,
    val currentRepairStatus: String
)
