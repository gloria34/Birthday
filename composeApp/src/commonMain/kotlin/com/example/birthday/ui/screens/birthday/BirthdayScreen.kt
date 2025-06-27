import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import birthday.composeapp.generated.resources.Res
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import com.example.birthday.data.enums.BirthdayTheme
import com.example.birthday.data.enums.BirthdayTheme.Companion.avatarPlaceholder
import com.example.birthday.data.enums.BirthdayTheme.Companion.backgroundColor
import com.example.birthday.data.enums.BirthdayTheme.Companion.backgroundImage
import com.example.birthday.data.enums.BirthdayTheme.Companion.cameraIcon
import com.example.birthday.ui.components.PlatformImage
import com.example.birthday.ui.dialogs.PickImageDialog
import com.example.birthday.ui.screens.birthday.BirthdayViewModel
import com.example.birthday.ui.theme.AppColors
import com.example.birthday.ui.theme.SvgIcons
import com.example.birthday.utils.DateTimeUtils
import io.ktor.util.toUpperCasePreservingASCIIRules

@Composable
fun BirthdayScreen(viewModel: BirthdayViewModel) {
    val topBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val birthdayTheme = BirthdayTheme.getBirthdayThemeById(viewModel.birthdayInfo.theme)
    val isAgeInYears = DateTimeUtils.isAgeInYears(viewModel.birthdayInfo.dob)
    val ageIcon = DateTimeUtils.getAgeIcon(viewModel.birthdayInfo.dob)
    val cameraIcon = birthdayTheme.cameraIcon
    val age = DateTimeUtils.calculateAge(viewModel.birthdayInfo.dob, isAgeInYears)
    val navigator = LocalNavigator.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(birthdayTheme.backgroundColor)
    ) {
        BirthdayContent(
            name = viewModel.birthdayInfo.name ?: "Unknown",
            topBarHeight = topBarHeight,
            isAgeInYears = isAgeInYears,
            age = age,
            cameraIcon = cameraIcon,
            ageIcon = ageIcon,
            avatarPlaceholder = birthdayTheme.avatarPlaceholder
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
    name: String,
    topBarHeight: Dp,
    isAgeInYears: Boolean,
    age: Int,
    cameraIcon: String,
    ageIcon: String,
    avatarPlaceholder: String
) {
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
            "Today $name is".toUpperCasePreservingASCIIRules(),
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
        Text(
            "${if (isAgeInYears) if (age == 1) "year" else "years" else if (age == 1) "month" else "months"} old".toUpperCasePreservingASCIIRules(),
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
            }
        )
        Spacer(modifier = Modifier.height(134.dp))
    }
    if (showImageDialog) {
        PickImageDialog(
            onDismiss = { showImageDialog = false },
            onPickCamera = {
            },
            onPickGallery = {
            }
        )
    }
}

@Composable
fun AvatarWithCameraButton(
    avatarPlaceholder: String,
    cameraIcon: String,
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
        PlatformImage(
            avatarPlaceholder,
            modifier = Modifier
                .onSizeChanged { avatarSizePx.value = it.width } // it.width == it.height for circle
        )
        if (avatarSizePx.value > 0) {
            val radiusPx = avatarSizePx.value / 2f
            val offsetPx = radiusPx / kotlin.math.sqrt(2f)
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