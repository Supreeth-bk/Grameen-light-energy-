package com.example.grameenlight.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.grameenlight.ui.theme.GrameenLightTheme
import com.example.grameenlight.ui.theme.PrimaryBlue

@Composable
fun OnboardingScreen(navController: NavHostController) {
    var currentPage by remember { mutableIntStateOf(0) }
    val pages = listOf(
        "See All Poles" to "View all streetlight poles in your village on an interactive map. Know the status at a glance.",
        "Report Status" to "Quickly report fused bulbs or daytime energy waste with just a single tap.",
        "Track Repairs" to "Keep track of your complaints and see when they are assigned or fixed by the Panchayat."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(PrimaryBlue.copy(alpha = 0.05f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                when(currentPage) {
                    0 -> Icons.Default.Map
                    1 -> Icons.Default.EditNote
                    else -> Icons.Default.Build
                },
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = PrimaryBlue
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                pages[currentPage].first,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                pages[currentPage].second,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.navigate("login") }) {
                Text("SKIP")
            }
            
            Row {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(8.dp)
                            .background(
                                if (index == currentPage) PrimaryBlue else Color.LightGray,
                                CircleShape
                            )
                    )
                }
            }

            Button(
                onClick = {
                    if (currentPage < 2) currentPage++
                    else navController.navigate("login")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(if (currentPage == 2) "FINISH" else "NEXT")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    val navController = rememberNavController()
    GrameenLightTheme {
        OnboardingScreen(navController)
    }
}
