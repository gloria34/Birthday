package com.example.birthday.data.enums

import androidx.compose.ui.graphics.Color
import com.example.birthday.ui.theme.AppColors
import com.example.birthday.ui.theme.RasterIcons
import com.example.birthday.ui.theme.SvgIcons

enum class BirthdayTheme {
    ELEPHANT,
    FOX,
    PELICAN;

    companion object {
        fun getBirthdayThemeById(value: String): BirthdayTheme {
            return when (value.lowercase()) {
                "elephant" -> ELEPHANT
                "fox" -> FOX
                "pelican" -> PELICAN
                else -> throw IllegalArgumentException("Unknown theme: $value")
            }
        }

        val BirthdayTheme.backgroundImage: String
            get() = when (this) {
                ELEPHANT -> RasterIcons.BG_ELEPHANT
                FOX -> RasterIcons.BG_FOX
                PELICAN -> RasterIcons.BG_PELICAN
            }

        val BirthdayTheme.backgroundColor: Color
            get() = when (this) {
                ELEPHANT -> AppColors.Yellow
                FOX -> AppColors.Green
                PELICAN -> AppColors.Blue
            }

        val BirthdayTheme.avatarPlaceholder: String
            get() = when (this) {
                ELEPHANT -> SvgIcons.AVATAR_PLACEHOLDER_YELLOW
                FOX -> SvgIcons.AVATAR_PLACEHOLDER_GREEN
                PELICAN -> SvgIcons.AVATAR_PLACEHOLDER_BLUE
            }

        val BirthdayTheme.cameraIcon: String
            get() = when (this) {
                ELEPHANT -> SvgIcons.CAMERA_ICON_YELLOW
                FOX -> SvgIcons.CAMERA_ICON_GREEN
                PELICAN -> SvgIcons.CAMERA_ICON_BLUE
            }

        val BirthdayTheme.borderStroke: Color
            get() = when (this) {
                ELEPHANT -> AppColors.Amber
                FOX -> AppColors.Teal
                PELICAN -> AppColors.LightBlue
            }
    }
}