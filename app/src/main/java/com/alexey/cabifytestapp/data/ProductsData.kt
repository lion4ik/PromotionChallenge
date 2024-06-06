package com.alexey.cabifytestapp.data

import kotlinx.serialization.Serializable

@Serializable
internal data class ProductsData(
    val products: List<ProductData>
)

@Serializable
internal data class ProductData(
    val name: String,
    val code: String,
    val price: Float
)