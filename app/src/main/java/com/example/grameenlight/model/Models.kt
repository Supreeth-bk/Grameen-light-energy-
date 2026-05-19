package com.example.grameenlight.model

import androidx.compose.ui.graphics.Color

/**
 * Enhanced Pole model based on the SOP.
 */
data class Pole(
    val id: String,
    val name: String,
    val posX: Float,
    val posY: Float,
    val status: PoleStatus,
    val repairStatus: RepairStatus = RepairStatus.REPORTED,
    val lastUpdated: Long = System.currentTimeMillis()
)

enum class PoleStatus(val displayName: String, val color: Color, val hex: String) {
    WORKING("Working", Color(0xFF27AE60), "#27AE60"),
    FUSED("Fused", Color(0xFFE74C3C), "#E74C3C"),
    DAY_ON("Burning During Day", Color(0xFFF39C12), "#F39C12")
}

enum class RepairStatus(val displayName: String) {
    REPORTED("Reported"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved")
}

/**
 * Complaint model for tracking reports.
 */
data class Complaint(
    val complaintId: String,
    val poleId: String,
    val statusReported: PoleStatus,
    val timestamp: Long = System.currentTimeMillis(),
    val currentRepairStatus: RepairStatus = RepairStatus.REPORTED
)
