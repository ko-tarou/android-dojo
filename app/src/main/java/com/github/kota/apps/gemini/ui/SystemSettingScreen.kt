package com.github.kota.apps.gemini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.kota.apps.gemini.components.AppHeader
import com.github.kota.apps.gemini.composition.LocalFontSize
import com.github.kota.apps.gemini.viewmodel.SystemSettingsViewModel

@Composable
fun SystemSettingScreen(
    onBackClick: () -> Unit,
    viewModel: SystemSettingsViewModel = viewModel()
) {
    val fontSize by viewModel.fontSize.collectAsState()
    val isTss by viewModel.isTss.collectAsState()

    Scaffold(
        topBar = {
            AppHeader(
                title = "システム",
                onBackClick = onBackClick,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text("文字の大きさ", fontSize = LocalFontSize.current.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = { if (fontSize > 8) viewModel.updateFontSize(fontSize - 2) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text("◀", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    fontSize.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                IconButton(
                    onClick = { if (fontSize < 40) viewModel.updateFontSize(fontSize + 2) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text("▶", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text("読み上げ機能", fontSize = LocalFontSize.current.sp, fontWeight = FontWeight.Bold)

                IconButton(
                    onClick = { viewModel.updateIsTss(!isTss) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text(if (isTss) "ON" else "OFF", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
