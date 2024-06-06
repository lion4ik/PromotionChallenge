package com.alexey.cabifytestapp

import com.alexey.cabifytestapp.domain.BasketProductItem
import com.alexey.cabifytestapp.domain.PromotionProcessor
import com.alexey.cabifytestapp.domain.ProductCode
import com.alexey.cabifytestapp.domain.ProductItem
import com.alexey.cabifytestapp.domain.TShirt3OrMoreDiscountPromotionInterceptor
import com.alexey.cabifytestapp.domain.Voucher2In1PromotionInterceptor
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PromotionProcessorTest {

    companion object {
        private const val VOUCHER_NAME = "Cabify Voucher"
        private const val TSHIRT_NAME = "Cabify T-Shirt"
    }

    private val promotionProcessor = PromotionProcessor(
        listOf(
            Voucher2In1PromotionInterceptor(),
            TShirt3OrMoreDiscountPromotionInterceptor()
        )
    )

    @Test
    fun `verify voucher 2 in 1 promotion applied`() {
        val expected = listOf(
            BasketProductItem(VOUCHER_NAME, 5f, 5f, ProductCode.VOUCHER),
            BasketProductItem(
                VOUCHER_NAME,
                5f,
                0f,
                ProductCode.VOUCHER,
                R.string.app_voucher_2_in_1_promotion
            ),
            BasketProductItem(VOUCHER_NAME, 5f, 5f, ProductCode.VOUCHER),
            BasketProductItem(
                VOUCHER_NAME,
                5f,
                0f,
                ProductCode.VOUCHER,
                R.string.app_voucher_2_in_1_promotion
            ),

            )
        val items = listOf(
            ProductItem(VOUCHER_NAME, 5f, ProductCode.VOUCHER),
            ProductItem(VOUCHER_NAME, 5f, ProductCode.VOUCHER),
            ProductItem(VOUCHER_NAME, 5f, ProductCode.VOUCHER),
            ProductItem(VOUCHER_NAME, 5f, ProductCode.VOUCHER),
        )

        val processedItems = promotionProcessor.processItems(items)
        Assertions.assertEquals(expected, processedItems)
    }

    @Test
    fun `verify voucher 2 in 1 promotion was not applied for 1 item`() {
        val expected = listOf(
            BasketProductItem(VOUCHER_NAME, 5f, 5f, ProductCode.VOUCHER)
        )
        val items = listOf(
            ProductItem(VOUCHER_NAME, 5f, ProductCode.VOUCHER)
        )
        val processedItems = promotionProcessor.processItems(items)
        Assertions.assertEquals(expected, processedItems)
    }

    @Test
    fun `verify t shirt 3 or more promotion applied`() {
        val expected = listOf(
            BasketProductItem(
                TSHIRT_NAME,
                20f,
                19f,
                ProductCode.TSHIRT,
                R.string.app_tshirt_3_or_more_promotion
            ),
            BasketProductItem(
                TSHIRT_NAME,
                20f,
                19f,
                ProductCode.TSHIRT,
                R.string.app_tshirt_3_or_more_promotion
            ),
            BasketProductItem(
                TSHIRT_NAME,
                20f,
                19f,
                ProductCode.TSHIRT,
                R.string.app_tshirt_3_or_more_promotion
            ),
            BasketProductItem(
                TSHIRT_NAME,
                20f,
                19f,
                ProductCode.TSHIRT,
                R.string.app_tshirt_3_or_more_promotion
            )
        )
        val items = listOf(
            ProductItem(TSHIRT_NAME, 20f, ProductCode.TSHIRT),
            ProductItem(TSHIRT_NAME, 20f, ProductCode.TSHIRT),
            ProductItem(TSHIRT_NAME, 20f, ProductCode.TSHIRT),
            ProductItem(TSHIRT_NAME, 20f, ProductCode.TSHIRT),
        )

        val processedItems = promotionProcessor.processItems(items)
        Assertions.assertEquals(expected, processedItems)
    }

    @Test
    fun `verify t shirt 3 or more promotion was applied for less than 3 items`() {
        val expected = listOf(
            BasketProductItem(
                TSHIRT_NAME,
                20f,
                20f,
                ProductCode.TSHIRT,
            ),
            BasketProductItem(
                TSHIRT_NAME,
                20f,
                20f,
                ProductCode.TSHIRT,
            )
        )
        val items = listOf(
            ProductItem(TSHIRT_NAME, 20f, ProductCode.TSHIRT),
            ProductItem(TSHIRT_NAME, 20f, ProductCode.TSHIRT),
        )

        val processedItems = promotionProcessor.processItems(items)
        Assertions.assertEquals(expected, processedItems)
    }
}