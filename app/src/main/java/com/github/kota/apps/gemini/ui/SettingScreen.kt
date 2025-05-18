package com.github.kota.apps.gemini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.github.kota.apps.gemini.components.AppHeader
import com.github.kota.apps.gemini.composition.LocalFontSize

@Composable
fun SettingsScreen(onBackClick: () -> Unit, onApiKeyClick: () -> Unit,onSystemClick: () -> Unit) { // 遷移用のLambda追加
    val settings = listOf("システム", "APIキー")

    Scaffold(
        topBar = {
            AppHeader(title = "設定", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(settings) { title ->
                SettingItem(
                    title = title,
                    onClick = {
                        if (title == "APIキー") {
                            onApiKeyClick()
                        }
                        if (title == "システム") {
                            onSystemClick()
                        }
                    }
                )
            }
        }

    }
}

@Composable
fun SettingItem(title: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontSize = (LocalFontSize.current+2).sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Next",
            modifier = Modifier.size(24.dp)
        )
    }
}