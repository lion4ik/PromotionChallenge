package com.alexey.cabifytestapp.domain.promotion

import com.alexey.cabifytestapp.R
import com.alexey.cabifytestapp.domain.model.BasketProductItem
import com.alexey.cabifytestapp.domain.model.ProductCode

internal class TShirt3OrMoreDiscountPromotionInterceptor : PromotionProcessor.PromotionInterceptor {

    private companion object {
        private const val REDUCED_TSHIRT_PRICE = 19f
        private const val ITEMS_COUNT_FOR_DISCOUNT = 3
    }

    override fun interceptItems(items: List<BasketProductItem>): List<BasketProductItem> {
        return if (items.count { it.code == ProductCode.TSHIRT } >= ITEMS_COUNT_FOR_DISCOUNT) {
            items.map {
                if (it.code == ProductCode.TSHIRT) {
                    it.copy(finalPrice = REDUCED_TSHIRT_PRICE, promotionDescription = R.string.app_tshirt_3_or_more_promotion)
                } else {
                    it
                }
            }
        } else {
            items
        }
    }
}