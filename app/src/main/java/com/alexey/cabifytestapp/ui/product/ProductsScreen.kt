package com.alexey.cabifytestapp.ui.product

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexey.cabifytestapp.R
import com.alexey.cabifytestapp.domain.model.ProductCode
import com.alexey.cabifytestapp.domain.model.ProductItem
import java.util.Currency
import java.util.Locale

@Composable
internal fun ProductsScreen(
    modifier: Modifier,
    viewModel: ProductsViewModel
) {
    val collectedState by viewModel.uiState.collectAsState()
    when (val state = collectedState) {
        is ProductsViewModel.ProductsState.DataState -> {
            LazyColumn(modifier = modifier) {
                items(state.products) { productItem ->
                    ProductItemUi(productItem) { viewModel.onAddToBasket(it) }
                }
            }
        }

        ProductsViewModel.ProductsState.EmptyState -> {
            EmptyOrErrorList(R.string.app_no_items)
        }

        ProductsViewModel.ProductsState.LoadingState -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }

        ProductsViewModel.ProductsState.ErrorState -> {
            EmptyOrErrorList(R.string.app_error_ocurred)
        }
    }
}

@Composable
internal fun EmptyOrErrorList(@StringRes descriptionResId: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = descriptionResId)
        )
    }
}

@Composable
internal fun ProductItemUi(
    product: ProductItem,
    onAddToBasketClicked: (product: ProductItem) -> Unit
) {
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
            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Button(
                    onClick = {
                        onAddToBasketClicked(product)
                    }) {
                    Text(
                        text = stringResource(id = R.string.app_add_to_basket),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            val euroSymbol = Currency.getInstance(Locale.FRANCE).symbol
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${product.price}${euroSymbol}",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview
@Composable
internal fun ErrorOrEmptyPreview() {
    EmptyOrErrorList(R.string.app_error_ocurred)
}

@Preview
@Composable
internal fun ProductItemPreview() {
    ProductItemUi(
        ProductItem("Sample item", 5f, ProductCode.VOUCHER)
    ) { }
}