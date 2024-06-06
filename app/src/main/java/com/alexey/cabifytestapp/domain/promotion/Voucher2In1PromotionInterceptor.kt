package com.alexey.cabifytestapp.domain.promotion

import com.alexey.cabifytestapp.R
import com.alexey.cabifytestapp.domain.model.BasketProductItem
import com.alexey.cabifytestapp.domain.model.ProductCode

internal class Voucher2In1PromotionInterceptor : PromotionProcessor.PromotionInterceptor {

    override fun interceptItems(items: List<BasketProductItem>): List<BasketProductItem> {
        var voucherItemNumber = 0
        return items.map { productItem ->
            if (productItem.code == ProductCode.VOUCHER) {
                voucherItemNumber++
                if (voucherItemNumber % 2 == 0) {
                    productItem.copy(finalPrice = 0f, promotionDescription = R.string.app_voucher_2_in_1_promotion)
                } else {
                    productItem
                }
            } else {
                productItem
            }
        }
    }
}