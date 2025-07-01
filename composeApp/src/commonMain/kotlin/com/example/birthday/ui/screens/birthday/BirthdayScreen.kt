import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import birthday.composeapp.generated.resources.Res
import birthday.composeapp.generated.resources.cancel
import birthday.composeapp.generated.resources.connect_to_the_server
import birthday.composeapp.generated.resources.months_old
import birthday.composeapp.generated.resources.open_settings
import birthday.composeapp.generated.resources.permission_required
import birthday.composeapp.generated.resources.to_set_a_photo_of
import birthday.composeapp.generated.resources.today_name_is
import birthday.composeapp.generated.resources.unknown
import birthday.composeapp.generated.resources.years_old
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import com.example.birthday.data.enums.BirthdayTheme
import com.example.birthday.data.enums.BirthdayTheme.Companion.avatarPlaceholder
import com.example.birthday.data.enums.BirthdayTheme.Companion.backgroundColor
import com.example.birthday.data.enums.BirthdayTheme.Companion.backgroundImage
import com.example.birthday.data.enums.BirthdayTheme.Companion.borderStroke
import com.example.birthday.data.enums.BirthdayTheme.Companion.cameraIcon
import com.example.birthday.ui.components.PlatformImage
import com.example.birthday.ui.dialogs.PickImageDialog
import com.example.birthday.ui.screens.birthday.BirthdayViewModel
import com.example.birthday.ui.theme.AppColors
import com.example.birthday.ui.theme.SvgIcons
import com.example.birthday.utils.DateTimeUtils
import com.example.birthday.utils.PermissionCallback
import com.example.birthday.utils.PermissionStatus
import com.example.birthday.utils.PermissionType
import com.example.birthday.utils.createPermissionsManager
import com.example.birthday.utils.rememberCameraManager
import com.example.birthday.utils.rememberGalleryManager
import io.ktor.util.toUpperCasePreservingASCIIRules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayScreen(viewModel: BirthdayViewModel) {
    val topBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val birthdayTheme = BirthdayTheme.getBirthdayThemeById(viewModel.birthdayInfo.theme)
    val isAgeInYears = DateTimeUtils.isAgeInYears(viewModel.birthdayInfo.dob)
    val ageIcon = DateTimeUtils.getAgeIcon(viewModel.birthdayInfo.dob)
    val cameraIcon = birthdayTheme.cameraIcon
    val age = DateTimeUtils.calculateAge(viewModel.birthdayInfo.dob, isAgeInYears)
    val borderStroke = birthdayTheme.borderStroke
    val navigator = LocalNavigator.current
    val coroutineScope = rememberCoroutineScope()
    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> viewModel.launchCamera.value = true
                        PermissionType.GALLERY -> viewModel.launchGallery.value = true
                    }
                }

                else -> {
                    viewModel.permissionRationalDialog.value = true
                }
            }
        }
    })
    val cameraManager = rememberCameraManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                it?.toImageBitmap()
            }
            viewModel.imageBitmap.value = bitmap
        }
    }
    val galleryManager = rememberGalleryManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                it?.toImageBitmap()
            }
            viewModel.imageBitmap.value = bitmap
        }
    }
    if (viewModel.launchGallery.value) {
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.AskPermission(PermissionType.GALLERY)
        }
        viewModel.launchGallery.value = false
    }
    if (viewModel.launchCamera.value) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.AskPermission(PermissionType.CAMERA)
        }
        viewModel.launchCamera.value = false
    }
    if (viewModel.launchSetting.value) {
        permissionsManager.LaunchSettings()
        viewModel.launchSetting.value = false
    }
    if (viewModel.permissionRationalDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.permissionRationalDialog.value = false
                        viewModel.launchSetting.value = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.NavyBlue,
                        contentColor = AppColors.White
                    )
                ) {
                    Text(stringResource(Res.string.open_settings))
                }
            },
            dismissButton = {
                Button(
                    onClick = { viewModel.permissionRationalDialog.value = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.NavyBlue,
                        contentColor = AppColors.White
                    )
                ) {
                    Text(stringResource(Res.string.cancel))
                }
            },
            title = {
                Text(stringResource(Res.string.permission_required), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            },
            text = {
                Text(
                    stringResource(Res.string.to_set_a_photo_of),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    fontSize = 16.sp
                )
            },
            modifier = Modifier.padding(16.dp)
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(birthdayTheme.backgroundColor)
    ) {
        BirthdayContent(
            topBarHeight = topBarHeight,
            isAgeInYears = isAgeInYears,
            age = age,
            cameraIcon = cameraIcon,
            ageIcon = ageIcon,
            avatarPlaceholder = birthdayTheme.avatarPlaceholder,
            borderStroke = borderStroke,
            viewModel = viewModel
        )
        BirthdayBackgroundImage(
            birthdayTheme.backgroundImage,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        BirthdayBottomBar()
        BirthdayBackButton(topBarHeight) { navigator?.pop() }
    }
}

@Composable
private fun BirthdayContent(
    topBarHeight: Dp,
    isAgeInYears: Boolean,
    age: Int,
    cameraIcon: String,
    ageIcon: String,
    avatarPlaceholder: String,
    borderStroke: Color,
    viewModel: BirthdayViewModel
) {
    val name = viewModel.birthdayInfo.name ?: stringResource(Res.string.unknown)
    var showImageDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(topBarHeight)
        )
        Spacer(
            modifier = Modifier.weight(1f).defaultMinSize(
                minHeight = 22.dp
            )
        )
        Text(
            stringResource(Res.string.today_name_is, name).toUpperCasePreservingASCIIRules(),
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 55.dp, end = 55.dp),
            maxLines = 2,
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = AppColors.NavyBlue,
                fontWeight = FontWeight.Medium,
                fontSize = 21.sp
            )
        )
        AgeRow(ageIcon)
        val ageString = if (isAgeInYears) {
            pluralStringResource(
                Res.plurals.years_old,
                age
            )
        } else {
            pluralStringResource(
                Res.plurals.months_old,
                age
            )
        }
        Text(
            ageString.toUpperCasePreservingASCIIRules(),
            modifier = Modifier.wrapContentSize().padding(top = 14.dp),
            style = TextStyle(
                color = AppColors.NavyBlue,
                fontWeight = FontWeight.Medium,
                fontSize = 21.sp
            )
        )
        Spacer(
            modifier = Modifier.weight(1f).defaultMinSize(
                minHeight = 15.dp
            )
        )
        AvatarWithCameraButton(
            avatarPlaceholder = avatarPlaceholder,
            cameraIcon = cameraIcon,
            onCameraClick = {
                showImageDialog = true
            },
            borderStroke = borderStroke,
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.height(134.dp))
    }
    if (showImageDialog) {
        PickImageDialog(
            onDismiss = { showImageDialog = false },
            onPickCamera = {
                viewModel.launchCamera.value = true
            },
            onPickGallery = {
                viewModel.launchGallery.value = true
            }
        )
    }
}

@Composable
fun AvatarWithCameraButton(
    avatarPlaceholder: String,
    cameraIcon: String,
    borderStroke: Color,
    viewModel: BirthdayViewModel,
    onCameraClick: () -> Unit
) {
    val avatarSizePx = remember { mutableStateOf(0) }
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 55.dp, end = 55.dp),
        contentAlignment = Alignment.Center
    ) {
        if (viewModel.imageBitmap.value != null) {
            Image(
                bitmap = viewModel.imageBitmap.value!!,
                contentDescription = null,
                modifier = Modifier
                    .onSizeChanged { avatarSizePx.value = it.width }.aspectRatio(1.0F)
                    .border(width = 7.dp, color = borderStroke, shape = CircleShape).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else PlatformImage(
            avatarPlaceholder,
            modifier = Modifier
                .onSizeChanged { avatarSizePx.value = it.width }
        )
        if (avatarSizePx.value > 0) {
            val radiusPx = avatarSizePx.value / 2f
            val offsetPx = radiusPx / sqrt(2f)
            val offsetDp = with(density) { offsetPx.toDp() }
            PlatformImage(
                cameraIcon,
                modifier = Modifier
                    .size(36.dp)
                    .offset(x = offsetDp, y = -offsetDp)
                    .clickable(onClick = onCameraClick)
            )
        }
    }
}

@Composable
private fun AgeRow(ageIcon: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 14.dp)
    ) {
        PlatformImage(
            SvgIcons.LEFT_SWIRLS,
            modifier = Modifier.height(43.dp).width(50.dp)
        )
        PlatformImage(
            ageIcon,
            modifier = Modifier.height(90.dp).padding(end = 22.dp, start = 22.dp)
        )
        PlatformImage(
            SvgIcons.RIGHT_SWIRLS,
            modifier = Modifier.height(43.dp).width(50.dp)
        )
    }
}

@Composable
private fun BirthdayBackgroundImage(backgroundImage: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = Res.getUri(backgroundImage),
        contentDescription = null,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun BirthdayBottomBar() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlatformImage(
            SvgIcons.NANIT_LOGO,
            modifier = Modifier.height(19.dp).width(58.dp)
        )
        Spacer(modifier = Modifier.height(109.dp))
    }
}

@Composable
private fun BirthdayBackButton(topBarHeight: Dp, onClick: () -> Unit) {
    PlatformImage(
        SvgIcons.ARROW_BACK,
        modifier = Modifier
            .padding(top = topBarHeight + 16.dp, start = 16.dp)
            .size(24.dp)
            .clickable { onClick() }
    )
}