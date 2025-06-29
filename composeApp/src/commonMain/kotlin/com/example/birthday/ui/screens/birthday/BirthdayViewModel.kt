package com.example.birthday.ui.screens.birthday

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.example.birthday.data.models.BirthdayInfo
import com.example.birthday.utils.PermissionCallback
import com.example.birthday.utils.PermissionStatus
import com.example.birthday.utils.PermissionType
import com.example.birthday.utils.createPermissionsManager

class BirthdayViewModel(val birthdayInfo: BirthdayInfo) : ViewModel() {
    var imageBitmap: MutableState<ImageBitmap?> = mutableStateOf(null)
    var imageSourceOptionDialog: MutableState<Boolean> = mutableStateOf(false)
    var launchCamera: MutableState<Boolean> = mutableStateOf(false)
    var launchGallery: MutableState<Boolean> = mutableStateOf(false)
    var launchSetting: MutableState<Boolean> = mutableStateOf(false)
    var permissionRationalDialog: MutableState<Boolean> = mutableStateOf(false)

}