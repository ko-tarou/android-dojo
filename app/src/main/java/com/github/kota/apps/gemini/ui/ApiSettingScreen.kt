package com.github.kota.apps.gemini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.kota.apps.gemini.components.AppHeader
import com.github.kota.apps.gemini.components.ApiKeyInputDialog
import com.github.kota.apps.gemini.composition.LocalFontSize
import com.github.kota.apps.gemini.viewmodel.ApiSettingsViewModel

@Composable
fun ApiSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: ApiSettingsViewModel = viewModel()
) {
    val apiKeys by viewModel.apiKeys.collectAsState()
    val selectedApiKey by viewModel.selectedApiKey.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppHeader(title = "APIキー", onBackClick = onBackClick)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(12.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(apiKeys) { apiKeyEntity ->
                ApiSettingItem(
                    text = apiKeyEntity.apiKey,
                    selected = apiKeyEntity.apiKey == selectedApiKey,
                    onSelect = { viewModel.selectApiKey(apiKeyEntity.apiKey) },
                    onDelete = { viewModel.deleteApiKey(apiKeyEntity.id) }
                )
            }
        }
    }

    ApiKeyInputDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = { newKey ->
            if (newKey.isNotBlank()) {
                viewModel.saveApiKey(newKey)
            }
            showDialog = false
        }
    )
}


@Composable
fun ApiSettingItem(text: String, selected: Boolean, onSelect: () -> Unit, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onSelect() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontSize = (LocalFontSize.current+2).sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        }
    }
}
