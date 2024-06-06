package com.alexey.cabifytestapp.data

import com.alexey.cabifytestapp.domain.ProductItem
import com.alexey.cabifytestapp.domain.toDomainModel
import com.alexey.cabifytestapp.observability.ObservabilityMonitor

internal class ProductRepository(
    private val productApi: ProductApi,
    private val observabilityMonitor: ObservabilityMonitor
) {

    suspend fun getProducts(): Result<List<ProductItem>> {
        return try {
            Result.success(productApi.getAvailableProducts().products.map {
                it.toDomainModel()
            })
        } catch (e: Throwable) {
            observabilityMonitor.logError(e.message.orEmpty())
            Result.failure(e)
        }
    }
}