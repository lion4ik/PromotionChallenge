package com.alexey.cabifytestapp.ui.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexey.cabifytestapp.domain.model.BasketProductItem
import com.alexey.cabifytestapp.domain.GetBasketProductsUseCase
import com.alexey.cabifytestapp.domain.model.ProductCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class BasketProductsViewModel(
    private val getBasketProductsUseCase: GetBasketProductsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<BasketProductsState> =
        MutableStateFlow(BasketProductsState.EmptyState)
    val uiState: StateFlow<BasketProductsState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getBasketProductsUseCase()
            getBasketProductsUseCase.productsInBasket.collect { updatedProductsInBasket ->
                _uiState.update {
                    if (updatedProductsInBasket.isNotEmpty()) {
                        BasketProductsState.DataState(updatedProductsInBasket)
                    } else {
                        BasketProductsState.EmptyState
                    }
                }
            }
        }
    }

    sealed class BasketProductsState {
        data object EmptyState : BasketProductsState()
        data class DataState(val basketProducts: Map<ProductCode, List<BasketProductItem>>) :
            BasketProductsState()
    }
}