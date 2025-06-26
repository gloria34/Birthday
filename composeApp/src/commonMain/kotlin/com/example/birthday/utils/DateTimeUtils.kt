package com.example.birthday.utils

import com.example.birthday.ui.theme.SvgIcons
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object DateTimeUtils {

    fun isAgeInYears(dob: Long): Boolean {
        return calculateAgeInYears(dob) > 0
    }

    fun getAgeIcon(dob: Long): String {
        val isAgeInYears = isAgeInYears(dob)
        val age = calculateAge(dob, isAgeInYears)
        return when (age) {
            0 -> SvgIcons.NUMBER_0
            1 -> SvgIcons.NUMBER_1
            2 -> SvgIcons.NUMBER_2
            3 -> SvgIcons.NUMBER_3
            4 -> SvgIcons.NUMBER_4
            5 -> SvgIcons.NUMBER_5
            6 -> SvgIcons.NUMBER_6
            7 -> SvgIcons.NUMBER_7
            8 -> SvgIcons.NUMBER_8
            9 -> SvgIcons.NUMBER_9
            10 -> SvgIcons.NUMBER_10
            11 -> SvgIcons.NUMBER_11
            12 -> SvgIcons.NUMBER_12
            else -> SvgIcons.NUMBER_9
        }
    }

    fun calculateAge(dob: Long, isAgeInYears: Boolean): Int {
        return if (isAgeInYears) {
            calculateAgeInYears(dob)
        } else {
            calculateAgeInMonths(dob)
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun calculateAgeInYears(dob: Long): Int {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val ageInMillis = currentTime - dob
        val ageInYears = (ageInMillis / 1000 / 60 / 60 / 24 / 365).toInt()
        return ageInYears
    }

    @OptIn(ExperimentalTime::class)
    private fun calculateAgeInMonths(dob: Long): Int {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val ageInMillis = currentTime - dob
        val ageInMonths = (ageInMillis / 1000 / 60 / 60 / 24 / 30).toInt()
        return ageInMonths
    }
}