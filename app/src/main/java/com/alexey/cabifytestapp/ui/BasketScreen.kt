package com.alexey.cabifytestapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.alexey.cabifytestapp.R
import com.alexey.cabifytestapp.domain.BasketProductItem
import com.alexey.cabifytestapp.ui.theme.Green
import java.util.Currency
import java.util.Locale

internal val euroSymbol = Currency.getInstance(Locale.FRANCE).symbol

@Composable
internal fun BasketScreen(
    modifier: Modifier, basketProductsViewModel: BasketProductsViewModel
) {
    val collectedState by basketProductsViewModel.uiState.collectAsState()

    when (val state = collectedState) {
        is BasketProductsViewModel.BasketProductsState.EmptyState -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.app_no_items)
                )
            }
        }

        is BasketProductsViewModel.BasketProductsState.DataState -> {
            Column(modifier = modifier) {
                LazyColumn {
                    val basketProducts = state.basketProducts
                    basketProducts.entries.forEach { entry ->
                        item {
                            BasketItemUi(entry.value)
                        }
                    }

                    item {
                        val totalPrice =
                            state.basketProducts.values.sumOf { products -> products.sumOf { it.finalPrice.toDouble() } }
                        TotalPriceItem(totalPrice)
                    }
                }
            }
        }
    }
}

@Composable
internal fun TotalPriceItem(totalPrice: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(id = R.string.app_total_price)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            text = "${totalPrice}${euroSymbol}",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.End
        )
    }
}

@Composable
internal fun BasketItemUi(
    basketProducts: List<BasketProductItem>
) {
    val product = basketProducts.first()
    val finalPriceForAll = basketProducts.sumOf { it.finalPrice.toDouble() }
    val originalPriceForAll = basketProducts.sumOf { it.originalPrice.toDouble() }
    val promotionDescription =
        basketProducts.firstOrNull { it.promotionDescription != 0 }?.promotionDescription
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name, style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = stringResource(
                        id = R.string.app_price_per_item,
                        "${product.originalPrice}${euroSymbol}"
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                if (promotionDescription != null) {

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text(
                        text = stringResource(id = promotionDescription),
                        style = MaterialTheme.typography.labelLarge,
                        color = Green
                    )
                }

                Text(
                    text = stringResource(id = R.string.app_items_count, "${basketProducts.size}"),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (originalPriceForAll != finalPriceForAll) {
                        Text(
                            text = "${originalPriceForAll}${euroSymbol}",
                            style = MaterialTheme.typography.titleLarge.plus(
                                TextStyle(
                                    textDecoration = TextDecoration.LineThrough
                                )
                            ),
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(modifier = Modifier.padding(2.dp))

                    Text(
                        text = "${finalPriceForAll}${euroSymbol}",
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}