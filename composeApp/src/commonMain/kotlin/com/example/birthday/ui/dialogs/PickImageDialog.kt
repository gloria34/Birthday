package com.example.birthday.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PickImageDialog(
    onDismiss: () -> Unit,
    onPickCamera: () -> Unit,
    onPickGallery: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        title = {
            Text("Choose Image Source", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        },
        text = {
            Column {
                Text(
                    "Choose from Gallery",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onPickGallery()
                            onDismiss()
                        }
                        .padding(vertical = 12.dp),
                    fontSize = 16.sp
                )
                Text(
                    "Take Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onPickCamera()
                            onDismiss()
                        }
                        .padding(vertical = 12.dp),
                    fontSize = 16.sp
                )
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}
