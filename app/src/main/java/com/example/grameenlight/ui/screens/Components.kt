package com.example.grameenlight.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grameenlight.model.PoleStatus

@Composable
fun StatusChip(status: PoleStatus, onClick: (PoleStatus) -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick(status) },
        shape = RoundedCornerShape(8.dp),
        color = status.color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, status.color)
    ) {
        Text(
            status.displayName.split(" ").last(),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = status.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
