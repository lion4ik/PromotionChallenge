package com.alexey.cabifytestapp.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alexey.cabifytestapp.DI
import com.alexey.cabifytestapp.R
import com.alexey.cabifytestapp.ui.basket.BasketProductsViewModel
import com.alexey.cabifytestapp.ui.basket.BasketScreen
import com.alexey.cabifytestapp.ui.product.ProductsScreen
import com.alexey.cabifytestapp.ui.theme.Purple80

@Composable
internal fun AppScreens(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = { BottomBar(navController, modifier = Modifier.fillMaxWidth()) },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
            ) {
                Navigation(navController = navController)
            }
        }
    )
}

@Composable
internal fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        NavigationItem.entries.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.name,
                onClick = {
                    navController.navigate(navItem.name) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Row {
                        Image(
                            painter = painterResource(id = navItem.iconResId),
                            contentDescription = navItem.name
                        )

                        if (navItem == NavigationItem.BASKET) {
                            BasketCounter(viewModel = viewModel(factory = DI.basketProductsVmFactory))
                        }
                    }
                }
            )
        }
    }
}

@Composable
internal fun BasketCounter(viewModel: BasketProductsViewModel) {
    val collectedState by viewModel.uiState.collectAsState()
    val state = collectedState
    if (state is BasketProductsViewModel.BasketProductsState.DataState) {
        val count = state.basketProducts.values.sumOf { it.size }
        if (count > 0) {
            Text(
                modifier = Modifier
                    .drawBehind {
                        drawCircle(
                            color = Purple80,
                            radius = this.size.maxDimension / 2
                        )
                    },
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                text = if (count > MAX_COUNTER_NUMBER) "99+" else count.toString(),
            )
        }
    }
}

private const val MAX_COUNTER_NUMBER = 99

internal enum class NavigationItem(@DrawableRes val iconResId: Int) {
    PRODUCTS(R.drawable.ic_home), BASKET(R.drawable.ic_shopping_basket)
}

@Composable
internal fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationItem.PRODUCTS.name) {
        composable(NavigationItem.PRODUCTS.name) {
            ProductsScreen(
                modifier = Modifier,
                viewModel = viewModel(
                    factory = DI.productsVmFactory
                )
            )
        }
        composable(NavigationItem.BASKET.name) {
            BasketScreen(
                modifier = Modifier,
                basketProductsViewModel = viewModel(factory = DI.basketProductsVmFactory)
            )
        }
    }
}