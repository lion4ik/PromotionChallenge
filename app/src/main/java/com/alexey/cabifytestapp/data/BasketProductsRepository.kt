package com.alexey.cabifytestapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alexey.cabifytestapp.DI.dataStore
import com.alexey.cabifytestapp.observability.ObservabilityMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

internal class BasketProductsRepository(
    private val appContext: Context,
    private val jsonParser: Json,
    private val observabilityMonitor: ObservabilityMonitor
) {

    private val _productsInBasketFlow: MutableStateFlow<List<ProductData>> = MutableStateFlow(
        emptyList()
    )
    val productsInBasketFlow: Flow<List<ProductData>> = _productsInBasketFlow

    private val listSerializer = ListSerializer(ProductData.serializer())

    companion object {
        private const val PRODUCTS_IN_BASKET_KEY = "products_in_basket"
        private val PREFERENCE_KEY_PRODUCTS_IN_BASKET = stringPreferencesKey(PRODUCTS_IN_BASKET_KEY)
    }

    suspend fun addProduct(product: ProductData) {
        val list = getProductsInBasket().toMutableList()
        list.add(product)
        try {
            appContext.dataStore.edit { prefs ->
                prefs[PREFERENCE_KEY_PRODUCTS_IN_BASKET] =
                    jsonParser.encodeToString(
                        listSerializer,
                        list.toList()
                    )
            }
            _productsInBasketFlow.update {
                list
            }
        } catch (e: Throwable) {
            observabilityMonitor.logError(
                e.message.orEmpty(), mapOf(
                    "product" to product.toString(),
                    "current_list_in_basket" to list.toString()
                )
            )
        }
    }

    suspend fun refreshProductsInBasket() {
        _productsInBasketFlow.update {
            getProductsInBasket()
        }
    }

    private suspend fun getProductsInBasket(): List<ProductData> {
        var rawProducts: String? = null
        try {
            rawProducts = appContext.dataStore.data.map {
                it[PREFERENCE_KEY_PRODUCTS_IN_BASKET]
            }.first()
            if (rawProducts != null) {
                return jsonParser.decodeFromString(
                    listSerializer,
                    rawProducts
                )
            }
        } catch (e: Throwable) {
            observabilityMonitor.logError(
                e.message.orEmpty(), mapOf(
                    "products_json" to rawProducts.orEmpty()
                )
            )
        }
        return emptyList()
    }
}