package com.alexey.cabifytestapp.domain

import com.alexey.cabifytestapp.data.BasketProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetBasketProductsUseCase(
    private val promotionProcessor: PromotionProcessor,
    private val basketProductsRepository: BasketProductsRepository
) {

    val productsInBasket: Flow<Map<ProductCode, List<BasketProductItem>>> =
        basketProductsRepository.productsInBasketFlow.map {
            val productItems = it.map { product ->
                product.toDomainModel()
            }
            promotionProcessor.processItems(productItems).groupBy { item -> item.code }
        }

    suspend operator fun invoke() {
        basketProductsRepository.refreshProductsInBasket()
    }
}