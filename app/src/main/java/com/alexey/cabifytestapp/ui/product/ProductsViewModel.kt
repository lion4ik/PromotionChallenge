package com.alexey.cabifytestapp.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexey.cabifytestapp.data.BasketProductsRepository
import com.alexey.cabifytestapp.data.ProductRepository
import com.alexey.cabifytestapp.domain.model.ProductItem
import com.alexey.cabifytestapp.domain.model.toDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ProductsViewModel(
    private val productRepository: ProductRepository,
    private val basketProductsRepository: BasketProductsRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProductsState> =
        MutableStateFlow(ProductsState.EmptyState)
    val uiState: StateFlow<ProductsState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                ProductsState.LoadingState
            }
            val productsResult = productRepository.getProducts()
            productsResult.fold({ productItems ->
                _uiState.update {
                    ProductsState.DataState(productItems)
                }
            }, {
                _uiState.update {
                    ProductsState.ErrorState
                }
            })
        }
    }

    fun onAddToBasket(product: ProductItem) {
        viewModelScope.launch {
            basketProductsRepository.addProduct(
                product.toDataModel()
            )
        }
    }

    internal sealed class ProductsState {
        data object EmptyState : ProductsState()
        data object ErrorState : ProductsState()
        data object LoadingState : ProductsState()
        data class DataState(val products: List<ProductItem>) : ProductsState()
    }
}