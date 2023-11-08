package br.com.jpm.fitnessmap

import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MainItem(
    val id : Int,
    @DrawableRes val drawableId: Int,
    @StringRes val textStrongId: Int,
    val color: Int
)
