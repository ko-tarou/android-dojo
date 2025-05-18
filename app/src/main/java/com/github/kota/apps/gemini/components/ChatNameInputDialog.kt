package com.github.kota.apps.gemini.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatNameInputDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var chatName by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("新しいチャット") },
            text = {
                TextField(
                    value = chatName,
                    onValueChange = { chatName = it },
                    placeholder = { Text("チャット名を入力") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (chatName.isNotBlank()) {
                            onConfirm(chatName)
                            onDismiss()
                        }
                    }
                ) {
                    Text("追加")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("キャンセル")
                }
            }
        )
    }
}
