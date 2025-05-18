package com.github.kota.apps.gemini

import NavigationPortal
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.github.kota.apps.gemini.composition.LocalFontSize
import com.github.kota.apps.gemini.viewmodel.SystemSettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val fontSizeViewModel: SystemSettingsViewModel = viewModel()
      val fontSize by fontSizeViewModel.fontSize.collectAsState()

      CompositionLocalProvider(LocalFontSize provides fontSize) {
        AppTheme {
          NavigationPortal()
        }
      }
    }
  }
}