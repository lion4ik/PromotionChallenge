package com.alexey.cabifytestapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexey.cabifytestapp.data.BasketProductsRepository
import com.alexey.cabifytestapp.data.ProductApi
import com.alexey.cabifytestapp.data.ProductRepository
import com.alexey.cabifytestapp.domain.GetBasketProductsUseCase
import com.alexey.cabifytestapp.domain.promotion.PromotionProcessor
import com.alexey.cabifytestapp.domain.promotion.TShirt3OrMoreDiscountPromotionInterceptor
import com.alexey.cabifytestapp.domain.promotion.Voucher2In1PromotionInterceptor
import com.alexey.cabifytestapp.observability.AndroidLogObservabilityMonitor
import com.alexey.cabifytestapp.observability.ObservabilityMonitor
import com.alexey.cabifytestapp.ui.basket.BasketProductsViewModel
import com.alexey.cabifytestapp.ui.product.ProductsViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
This is simple DI graph implementation. Since it's a testing challenge it was decided not to use
some DI frameworks like Dagger2, Koin, etc. For scalability and in case if we want to avoid
startup performance issues it's better to use some DI framework. Such a frameworks help to manage
dependencies and create them in a smart way.
 */
internal object DI {

    val okHttpClient: OkHttpClient =
        OkHttpClient.Builder()
            .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.BASE_ENDPOINT)
        .build()

    val productApi: ProductApi = retrofit.create(ProductApi::class.java)

    val productRepository: ProductRepository by lazy { ProductRepository(productApi, observabilityMonitor) }
    val basketProductsRepository by lazy {
        BasketProductsRepository(
            App.instance,
            jsonParser,
            observabilityMonitor
        )
    }

    val productsVmFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductsViewModel(productRepository, basketProductsRepository) as T
            }
        }
    }

    val basketProductsVmFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BasketProductsViewModel(getBasketProductsUseCase) as T
            }
        }
    }

    val jsonParser = Json { ignoreUnknownKeys = true }
    private const val PREFERENCES_FILE_NAME = "basket"
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_FILE_NAME)

    val promotionProcessor = PromotionProcessor(
        listOf(
            Voucher2In1PromotionInterceptor(),
            TShirt3OrMoreDiscountPromotionInterceptor()
        )
    )

    val getBasketProductsUseCase by lazy {
        GetBasketProductsUseCase(
            promotionProcessor,
            basketProductsRepository
        )
    }

    val observabilityMonitor: ObservabilityMonitor = AndroidLogObservabilityMonitor()
}