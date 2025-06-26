import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.birthday.ui.components.PlatformImage
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
    ageIcon: String,
    avatarPlaceholder: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Today $name is".toUpperCasePreservingASCIIRules(),
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 55.dp, end = 55.dp, top = topBarHeight + 22.dp),
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
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 55.dp, end = 55.dp)
        ) {
            PlatformImage(avatarPlaceholder)
        }
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(167.dp))
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
            .fillMaxSize()
            .padding(bottom = 53.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlatformImage(
            SvgIcons.NANIT_LOGO,
            modifier = Modifier.height(19.dp).width(58.dp)
        )
        Spacer(modifier = Modifier.height(53.dp))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.LightCoral,
                contentColor = AppColors.White
            ),
            modifier = Modifier.height(42.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                "Share the news",
                style = TextStyle(
                    color = AppColors.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(start = 21.dp, end = 5.dp)
            )
            PlatformImage(
                SvgIcons.SHARE,
                modifier = Modifier.height(33.dp).width(30.dp).padding(end = 11.dp)
            )
        }
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