package com.rudra.liquidlauncher

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VoidLiquidMasterLauncher()
        }
    }
}

@Composable
fun VoidLiquidMasterLauncher() {
    val context = LocalContext.current
    val pm = context.packageManager

    var isDrawerOpen by remember { mutableStateOf(false) }
    var isControlCenterOpen by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedDockTab by remember { mutableIntStateOf(0) }

    var roll by remember { mutableFloatStateOf(0f) }
    var pitch by remember { mutableFloatStateOf(0f) }

    // জাইরোস্কোপ সেন্সর লিসেনার
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, it.values)
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)

                    roll = (orientation[2] * 30f).coerceIn(-40f, 40f)
                    pitch = (orientation[1] * 20f).coerceIn(-30f, 30f)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        onDispose { sensorManager.unregisterListener(listener) }
    }

    val animatedRoll by animateFloatAsState(targetValue = roll, animationSpec = tween(50), label = "roll")
    val animatedPitch by animateFloatAsState(targetValue = pitch, animationSpec = tween(50), label = "pitch")

    val apps = remember {
        val intent = Intent(Intent.ACTION_MAIN, null).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
        pm.queryIntentActivities(intent, 0).sortedBy { it.loadLabel(pm).toString().lowercase() }
    }

    val filteredApps = apps.filter {
        it.loadLabel(pm).toString().contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -30) isDrawerOpen = true
                    if (dragAmount > 30) {
                        if (isDrawerOpen) isDrawerOpen = false
                        else isControlCenterOpen = true
                    }
                }
            }
    ) {
        // ১. লিকুইড ক্লক উইজেট
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val currentTime = remember { SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) }
            val currentDate = remember { SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date()) }

            GlassCardContainer(
                modifier = Modifier.padding(horizontal = 24.dp),
                roll = animatedRoll,
                pitch = animatedPitch
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentTime,
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = currentDate,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // ২. লিকুইড গ্লাস ডক
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 28.dp)
                .fillMaxWidth()
                .height(86.dp)
        ) {
            GlassCardContainer(
                modifier = Modifier.fillMaxSize(),
                roll = animatedRoll,
                pitch = animatedPitch
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    apps.take(4).forEachIndexed { index, app ->
                        val isSelected = selectedDockTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    selectedDockTab = index
                                    launchApp(context, pm, app)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color.White.copy(alpha = 0.22f))
                                )
                            }
                            AppIconTile(app = app, pm = pm, showLabel = false) {
                                selectedDockTab = index
                                launchApp(context, pm, app)
                            }
                        }
                    }
                }
            }
        }

        // ৩. লিকুইড কন্ট্রোল সেন্টার
        if (isControlCenterOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { isControlCenterOpen = false }
            ) {
                GlassCardContainer(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    roll = animatedRoll,
                    pitch = animatedPitch
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "CONTROL CENTER",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            LiquidToggleTile("Settings", Icons.Default.Settings, true)
                            LiquidToggleTile("Search", Icons.Default.Search, true)
                            LiquidToggleTile("Phone", Icons.Default.Phone, false)
                            LiquidToggleTile("Favorite", Icons.Default.Star, false)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        LiquidSlider("Brightness", 0.75f)
                        Spacer(modifier = Modifier.height(10.dp))
                        LiquidSlider("Volume", 0.60f)
                    }
                }
            }
        }

        // ৪. ফ্রস্টেড অ্যাপ ড্রয়ার
        if (isDrawerOpen) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.88f))
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search installed apps...", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.White.copy(alpha = 0.08f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                        focusedBorderColor = Color.White.copy(alpha = 0.3f),
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 18.dp)
                ) {
                    items(filteredApps) { app ->
                        AppIconTile(app = app, pm = pm, showLabel = true) {
                            launchApp(context, pm, app)
                            isDrawerOpen = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GlassCardContainer(
    modifier: Modifier = Modifier,
    roll: Float = 0f,
    pitch: Float = 0f,
    shape: RoundedCornerShape = RoundedCornerShape(32.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(16.dp, shape = shape, ambientColor = Color.Black.copy(alpha = 0.35f))
            .clip(shape)
            .background(Color.White.copy(alpha = 0.12f))
            .border(
                width = 1.4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.65f),
                        Color.White.copy(alpha = 0.10f),
                        Color.White.copy(alpha = 0.40f)
                    )
                ),
                shape = shape
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = roll.dp, y = pitch.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.26f), Color.Transparent),
                        radius = 280f
                    )
                )
        )
        content()
    }
}

@Composable
fun LiquidToggleTile(title: String, icon: ImageVector, defaultState: Boolean) {
    var enabled by remember { mutableStateOf(defaultState) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(if (enabled) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.08f))
                .clickable { enabled = !enabled },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = Color.White)
        }
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun LiquidSlider(title: String, initialValue: Float) {
    var progress by remember { mutableFloatStateOf(initialValue) }
    Column {
        Text(title, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White.copy(alpha = 0.08f))
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        progress = (change.position.x / size.width).coerceIn(0f, 1f)
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.32f))
            )
        }
    }
}

@Composable
fun AppIconTile(app: ResolveInfo, pm: PackageManager, showLabel: Boolean, onClick: () -> Unit) {
    val iconBitmap = remember(app) {
        app.loadIcon(pm).toBitmap(96, 96).asImageBitmap()
    }

    Column(
        modifier = Modifier
            .padding(6.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = iconBitmap,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        if (showLabel) {
            Text(
                text = app.loadLabel(pm).toString(),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

fun launchApp(context: Context, pm: PackageManager, app: ResolveInfo) {
    val intent = pm.getLaunchIntentForPackage(app.activityInfo.packageName)
    intent?.let { context.startActivity(it) }
}
