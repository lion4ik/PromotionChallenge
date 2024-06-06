package com.alexey.cabifytestapp.domain.model

import androidx.annotation.StringRes

internal data class BasketProductItem(
    val name: String,
    val originalPrice: Float,
    val finalPrice: Float,
    val code: ProductCode,
    @StringRes val promotionDescription: Int = 0
)