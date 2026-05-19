package com.example.grameenlight.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.grameenlight.model.Complaint
import com.example.grameenlight.model.Pole
import com.example.grameenlight.model.PoleStatus
import com.example.grameenlight.model.RepairStatus
import com.example.grameenlight.ui.theme.*
import com.example.grameenlight.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.automirrored.filled.FactCheck

fun Modifier.glassPanel(cornerRadius: Dp = 24.dp) = this
    .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(cornerRadius))
    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(cornerRadius))

@Composable
fun Modifier.tactileClickable(onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = "scale"
    )
    
    return this
        .graphicsLayer(scaleX = scale, scaleY = scale)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val poles by viewModel.filteredPoles.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val energySaved by viewModel.energySavedKWh.collectAsState()
    val complaints by viewModel.complaints.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val personalImpact by viewModel.personalImpactKWh.collectAsState()
    val isTechnicianMode by viewModel.isTechnicianMode.collectAsState()

    GrameenLightTheme(darkTheme = isDarkMode) {
        MainScreenContent(
            poles = poles,
            searchQuery = searchQuery,
            energySaved = energySaved,
            personalImpact = personalImpact,
            complaints = complaints,
            isTechnicianMode = isTechnicianMode,
            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
            onReportStatus = { pole, status -> viewModel.reportStatus(pole, status) },
            onToggleDarkMode = { viewModel.toggleDarkMode() },
            onToggleTechnicianMode = { viewModel.toggleTechnicianMode() },
            onResetData = { viewModel.resetDemoData() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    poles: List<Pole>,
    searchQuery: String,
    energySaved: Float,
    personalImpact: Float,
    complaints: List<Complaint>,
    isTechnicianMode: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onReportStatus: (Pole, PoleStatus) -> Unit,
    onToggleDarkMode: () -> Unit,
    onToggleTechnicianMode: () -> Unit,
    onResetData: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, "Home") },
                        label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        selected = currentRoute == "home" || currentRoute == null,
                        onClick = { 
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryBlue,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Search, "Search") },
                        label = { Text("Search", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        selected = currentRoute == "search",
                        onClick = { navController.navigate("search") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryBlue,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.Assignment, "Reports") },
                        label = { Text("Reports", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        selected = currentRoute == "reports",
                        onClick = { navController.navigate("reports") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryBlue,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, "Settings") },
                        label = { Text("Settings", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        selected = currentRoute == "settings",
                        onClick = { navController.navigate("settings") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryBlue,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                        )
                    )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { 
                MapScreen(
                    poles = poles,
                    energySaved = energySaved,
                    isTechnicianMode = isTechnicianMode,
                    onReportStatus = onReportStatus
                ) 
            }
            composable("search") { 
                SearchScreen(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    poles = poles,
                    onSelectPole = { 
                        navController.navigate("home")
                    }
                ) 
            }
            composable("reports") { MyReportsScreen(complaints = complaints, personalImpact = personalImpact, isTechnicianMode = isTechnicianMode) }
            composable("settings") { 
                SettingsScreen(
                    onToggleDarkMode = onToggleDarkMode,
                    onToggleTechnicianMode = onToggleTechnicianMode,
                    isTechnicianMode = isTechnicianMode,
                    onResetData = onResetData
                ) 
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    poles: List<Pole>,
    energySaved: Float,
    isTechnicianMode: Boolean,
    onReportStatus: (Pole, PoleStatus) -> Unit
) {
    var selectedPole by remember { mutableStateOf<Pole?>(null) }
    val sheetState = rememberModalBottomSheetState()

    // Animation for Maintenance Lines and Glow
    val infiniteTransition = rememberInfiniteTransition(label = "mapFlow")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "phase"
    )
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(animation = tween(1500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow"
    )
    var scale by remember { mutableFloatStateOf(0.8f) }
    var offset by remember { mutableStateOf(Offset(200f, 200f)) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .transformable(state = state)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            withTransform({
                translate(offset.x, offset.y)
                scale(scale, Offset.Zero)
            }) {
                // Draw Village Houses & Landmarks
                val houseColor = Color.LightGray.copy(alpha = 0.15f)
                val houseSize = Size(60f, 40f)
                
                // Panchayat/School Landmark
                drawRect(Color(0xFFE67E22).copy(alpha = 0.4f), Offset(450f, 1000f), Size(120f, 100f))
                
                // Clusters of houses
                drawRect(houseColor, Offset(100f, 400f), houseSize)
                drawRect(houseColor, Offset(200f, 400f), houseSize)
                drawRect(houseColor, Offset(500f, 600f), houseSize)
                drawRect(houseColor, Offset(800f, 400f), houseSize)
                drawRect(houseColor, Offset(50f, 850f), houseSize)

                // Draw Realistic Village Roads
                val roadColor = Color.DarkGray.copy(alpha = 0.2f)
                val roadWidth = 100f
                drawLine(roadColor, Offset(500f, 0f), Offset(500f, 2000f), roadWidth) // Main St
                drawLine(roadColor, Offset(0f, 500f), Offset(1000f, 500f), roadWidth) // Market Rd
                drawLine(roadColor, Offset(0f, 1500f), Offset(1000f, 1500f), roadWidth) // Temple Rd

                // Draw Poles with GLOW effect
                poles.forEach { pole ->
                    val color = pole.status.color
                    // Soft Outer Glow
                    drawCircle(color.copy(alpha = 0.15f * glowScale), 60f, Offset(pole.posX, pole.posY))
                    drawCircle(color.copy(alpha = 0.3f), 30f, Offset(pole.posX, pole.posY))
                    drawCircle(Color.White, 16f, Offset(pole.posX, pole.posY))
                    drawCircle(color, 12f, Offset(pole.posX, pole.posY))
                }

                // Technician Mode Overlays
                if (isTechnicianMode) {
                    val serviceCenter = Offset(700f, 1500f)
                    val techColor = Color(0xFF3498DB)
                    
                    // Service Center Hub Icon (Panchayat symbol)
                    drawRect(techColor, serviceCenter - Offset(40f, 40f), Size(80f, 80f))
                    drawCircle(Color.White.copy(alpha = 0.5f), 100f, serviceCenter, style = androidx.compose.ui.graphics.drawscope.Stroke(2f))
                    
                    // Animated Repair Routes
                    poles.filter { it.status != PoleStatus.WORKING }.forEach { pole ->
                        drawLine(
                            color = techColor,
                            start = serviceCenter,
                            end = Offset(pole.posX, pole.posY),
                            strokeWidth = 4f,
                            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(20f, 10f), phase)
                        )
                    }
                }
            }
        }

        // Overlay Labels for Technician Mode
        if (isTechnicianMode) {
            Text(
                "MAINTENANCE ACTIVE",
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp).background(Color.Red.copy(alpha = 0.7f), RoundedCornerShape(4.dp)).padding(8.dp),
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // System Intelligence Alert (Interactive Card)
        if (isTechnicianMode || poles.any { it.status != PoleStatus.WORKING }) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 110.dp, start = 16.dp, end = 16.dp)
                    .clickable { /* Could show a detailed summary toast */ },
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SYSTEM: Click for fault details", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Village Energy Monitor (Floating Glass Card)
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassPanel()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "VILLAGE ENERGY MONITOR", 
                        fontSize = 10.sp, 
                        fontWeight = FontWeight.Bold, 
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            "${energySaved.roundToInt()} kWh", 
                            fontSize = 32.sp, 
                            fontWeight = FontWeight.Black, 
                            color = StatusGreen
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Saved", fontSize = 14.sp, color = Color.Gray)
                    }
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(StatusGreen.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .border(1.dp, StatusGreen.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Bolt, null, tint = StatusGreen)
                }
            }
        }

        // Click interaction overlay (Simplified)
        Box(modifier = Modifier.fillMaxSize().clickable(
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
            indication = null
        ) {
            // Demo logic: find closest pole or just toggle first fused one
            selectedPole = poles.find { it.status != PoleStatus.WORKING } ?: poles.firstOrNull()
        })

        selectedPole?.let { pole ->
            ModalBottomSheet(
                onDismissRequest = { selectedPole = null },
                sheetState = sheetState,
                containerColor = Color.Transparent, // We'll use our glass card
                dragHandle = null
            ) {
                Box(modifier = Modifier.padding(16.dp).padding(bottom = 32.dp).glassPanel().background(SurfaceContainer.copy(alpha = 0.95f), RoundedCornerShape(24.dp))) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        // Handle
                        Box(modifier = Modifier.size(40.dp, 4.dp).background(Color.White.copy(alpha = 0.2f), CircleShape).align(Alignment.CenterHorizontally))
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Column {
                                Text(pole.id, fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = Color.White.copy(alpha = 0.5f), letterSpacing = 1.sp)
                                Text("Pole Audit", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            }
                            StatusBadge(pole.status)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Meta Info Section (New)
                        Box(modifier = Modifier.fillMaxWidth().glassPanel(12.dp).padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Schedule, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Updated 2 hours ago", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("${pole.posX.toInt()}, ${pole.posY.toInt()} (Sector B)", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                                    }
                                }
                                Box(modifier = Modifier.size(48.dp).background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Lightbulb, null, tint = Color.White.copy(alpha = 0.5f))
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("UPDATE STATUS", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatusButton("Working", StatusGreen, pole.status == PoleStatus.WORKING) { onReportStatus(pole, PoleStatus.WORKING); selectedPole = null }
                            StatusButton("Fused", StatusRed, pole.status == PoleStatus.FUSED) { onReportStatus(pole, PoleStatus.FUSED); selectedPole = null }
                            StatusButton("Daylight ON", StatusYellow, pole.status == PoleStatus.DAY_ON) { onReportStatus(pole, PoleStatus.DAY_ON); selectedPole = null }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: PoleStatus) {
    Surface(
        color = status.color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(100.dp),
        border = BorderStroke(1.dp, status.color.copy(alpha = 0.3f))
    ) {
        Text(
            status.name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = status.color,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun StatusButton(label: String, color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .tactileClickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) color else Color.White.copy(alpha = 0.05f),
        border = if (!isSelected) BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)) else null
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, color = if (isSelected) Color.White else color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(query: String, onQueryChange: (String) -> Unit, poles: List<Pole>, onSelectPole: (Pole) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
            placeholder = { Text("Search Pole ID or Street...") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = PrimaryBlue) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(poles) { pole ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassPanel(16.dp)
                        .tactileClickable { onSelectPole(pole) }
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(12.dp).background(pole.status.color, CircleShape))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(pole.id, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                            Text(pole.name, fontSize = 12.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray.copy(alpha = 0.5f))
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(onToggleDarkMode: () -> Unit, onToggleTechnicianMode: () -> Unit, isTechnicianMode: Boolean, onResetData: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showAboutDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("SETTINGS", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(32.dp))
            
            SettingsItem("Maintenance Mode", if (isTechnicianMode) Icons.Default.BuildCircle else Icons.Default.Engineering) {
                onToggleTechnicianMode()
            }
            
            SettingsItem("Dark Mode Toggle", Icons.Default.DarkMode) {
                onToggleDarkMode()
            }
            SettingsItem("Reset Village Data", Icons.Default.Refresh) {
                onResetData()
                scope.launch { snackbarHostState.showSnackbar("Demo data reset to default") }
            }
            SettingsItem("About Grameen Light", Icons.Default.Info) {
                showAboutDialog = true
            }
            SettingsItem("Privacy Policy", Icons.Default.Lock) { }
        }
        
        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("About Grameen Light", fontWeight = FontWeight.Bold) },
                text = { 
                    Column {
                        Text("Grameen Light is a smart streetlight monitoring system designed for rural empowerment.", fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Goal: To reduce energy wastage and improve village safety through rapid fault reporting and resolution tracking.", fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tech: Built with Jetpack Compose & Room DB for a seamless offline-first experience.", fontSize = 14.sp, color = Color.Gray)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) { Text("Close") }
                }
            )
        }
        
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun SettingsItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .glassPanel(16.dp)
            .tactileClickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun MyReportsScreen(complaints: List<Complaint>, personalImpact: Float, isTechnicianMode: Boolean) {
    if (isTechnicianMode) {
        TechnicianHub(complaints)
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("CITIZEN REPORTS", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(16.dp))

            // Personal Impact Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .glassPanel(24.dp)
                    .background(Color(0xFF1B263B).copy(alpha = 0.8f), RoundedCornerShape(24.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VolunteerActivism, null, tint = StatusGreen, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("YOUR IMPACT", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("${personalImpact.roundToInt()} kWh Saved", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = { (personalImpact % 100) / 100f },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = StatusGreen,
                        trackColor = Color.Gray.copy(alpha = 0.2f)
                    )
                    Text("Next Badge: Energy Hero at 100kWh", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (complaints.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.History, null, tint = Color.LightGray, modifier = Modifier.size(64.dp))
                        Text("No reports found", fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                }
            } else {
                LazyColumn {
                    items(complaints) { complaint ->
                        ComplaintCard(complaint)
                    }
                }
            }
        }
    }
}

@Composable
fun ComplaintCard(complaint: Complaint) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .glassPanel(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("#RPT-${complaint.complaintId.takeLast(4)}", color = PrimaryBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Text("SECTOR 4 • ${java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date(complaint.timestamp))} AGO", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                StatusLifecycleBadge(complaint.currentRepairStatus)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                "Main street light flickering near the community well. Night commuters reporting safety hazard.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Technician Feedback Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 3.dp.toPx()
                        drawLine(PrimaryBlue, Offset(0f, 0f), Offset(0f, size.height), strokeWidth)
                    }
                    .padding(start = 16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.FactCheck, null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Technician Feedback", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        "\"Diagnostic complete. Capacitor replacement scheduled for morning shift.\"",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
fun StatusLifecycleBadge(status: RepairStatus) {
    val color = when(status) {
        RepairStatus.REPORTED -> Color.Gray
        RepairStatus.ASSIGNED -> PrimaryBlue
        RepairStatus.IN_PROGRESS -> Color(0xFFF39C12)
        RepairStatus.RESOLVED -> StatusGreen
    }
    Box(
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(color.copy(alpha = 0.1f)).padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(status.displayName, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TechnicianHub(complaints: List<Complaint>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Active Dashboard", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(24.dp))
            
            // 2x2 Stats Grid
            Row(modifier = Modifier.fillMaxWidth()) {
                StatCard("Pending", "12", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                StatCard("In Progress", "04", Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                StatCard("Critical", "02", Modifier.weight(1f), StatusRed)
                Spacer(modifier = Modifier.width(12.dp))
                StatCard("Completed", "184", Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("Active Complaints", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(complaints.filter { it.currentRepairStatus != RepairStatus.RESOLVED }) { complaint ->
            ComplaintCard(complaint)
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier, color: Color = Color.White) {
    Box(modifier = modifier.glassPanel(12.dp).padding(16.dp)) {
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    GrameenLightTheme {
        MainScreenContent(
            poles = listOf(
                Pole("P101", "Market Road", 100f, 500f, PoleStatus.WORKING),
                Pole("P102", "Bus Stand", 400f, 500f, PoleStatus.FUSED)
            ),
            searchQuery = "",
            energySaved = 125.0f,
            personalImpact = 31.0f,
            complaints = emptyList(),
            isTechnicianMode = false,
            onSearchQueryChange = {},
            onReportStatus = { _, _ -> },
            onToggleDarkMode = {},
            onToggleTechnicianMode = {},
            onResetData = {}
        )
    }
}
