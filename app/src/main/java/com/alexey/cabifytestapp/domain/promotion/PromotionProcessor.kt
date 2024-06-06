package com.alexey.cabifytestapp.domain.promotion

import com.alexey.cabifytestapp.domain.model.BasketProductItem
import com.alexey.cabifytestapp.domain.model.ProductItem

internal class PromotionProcessor(
    private val promotionInterceptors: List<PromotionInterceptor>
) {

    fun interface PromotionInterceptor {
        fun interceptItems(items: List<BasketProductItem>): List<BasketProductItem>
    }

    fun processItems(items: List<ProductItem>): List<BasketProductItem> {
        var basketProducts = items.map {
            BasketProductItem(
                name = it.name,
                originalPrice = it.price,
                finalPrice = it.price,
                code = it.code
            )
        }
        for (interceptor in promotionInterceptors) {
            basketProducts = interceptor.interceptItems(basketProducts).toMutableList()
        }
        return basketProducts
    }
}