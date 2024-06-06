package com.alexey.cabifytestapp.domain

import com.alexey.cabifytestapp.data.ProductData

internal fun ProductItem.toDataModel(): ProductData =
        ProductData(
            name = this.name,
            code = this.code.name,
            price = this.price
        )

internal fun ProductData.toDomainModel(): ProductItem =
    ProductItem(
        name = this.name,
        price = this.price,
        code = ProductCode.valueOfOrUnknown(this.code)
    )