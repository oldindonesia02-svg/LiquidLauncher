package com.example.liquidlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.liquidlauncher.model.AppItem
import com.example.liquidlauncher.ui.screen.LauncherScreen
import com.example.liquidlauncher.ui.theme.LiquidLauncherTheme
import com.example.liquidlauncher.util.AppManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiquidLauncherTheme {
                val viewModel = viewModel<LauncherViewModel>(
                    factory = LauncherViewModelFactory(AppManager(this))
                )
                val apps by viewModel.apps.collectAsState()
                val isLoading by viewModel.isLoading.collectAsState()

                LauncherScreen(
                    apps = apps,
                    isLoading = isLoading,
                    onAppClick = { app ->
                        viewModel.launchApp(this, app)
                    }
                )
            }
        }
    }
}

class LauncherViewModel(private val appManager: AppManager) : ViewModel() {
    private val _apps = MutableStateFlow<List<AppItem>>(emptyList())
    val apps: StateFlow<List<AppItem>> = _apps

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadApps()
    }

    private fun loadApps() {
        try {
            val installedApps = appManager.getInstalledApps()
            _apps.value = installedApps
        } catch (e: Exception) {
            e.printStackTrace()
            _apps.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }

    fun launchApp(context: android.content.Context, app: AppItem) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(app.packageName)
            if (intent != null) {
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

class LauncherViewModelFactory(private val appManager: AppManager) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LauncherViewModel(appManager) as T
    }
}
