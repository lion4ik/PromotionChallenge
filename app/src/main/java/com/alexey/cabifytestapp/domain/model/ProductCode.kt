package com.alexey.cabifytestapp.domain.model

internal enum class ProductCode {
    VOUCHER, TSHIRT, MUG, UNKNOWN;

    companion object {
        fun valueOfOrUnknown(name: String): ProductCode =
            entries.find { it.name == name } ?: UNKNOWN
    }
}