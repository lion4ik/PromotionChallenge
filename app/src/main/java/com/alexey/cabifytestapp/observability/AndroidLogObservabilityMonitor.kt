package com.alexey.cabifytestapp.observability

import android.util.Log
import java.lang.StringBuilder

/**
    Simple implementation to track error through [android.util.Log.e]
 */
internal class AndroidLogObservabilityMonitor : ObservabilityMonitor {

    companion object {
        const val TAG = "AndroidLogObservabilityMonitor"
    }

    override fun logError(message: String, attributes: Map<String, String>) {
        val stringBuilder = StringBuilder(message)
        stringBuilder.append(" with attributes: ")
        attributes.entries.forEach {
            stringBuilder.append("${it.key} = ${it.value}")
        }
        Log.e(TAG, stringBuilder.toString())
    }
}