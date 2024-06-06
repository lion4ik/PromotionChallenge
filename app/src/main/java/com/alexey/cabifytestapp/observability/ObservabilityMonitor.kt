package com.alexey.cabifytestapp.observability

/**
    We want to make sure we know how our app works, how many errors users are getting.
    That's why we can try some errors into some observability system (like Datadog, Grafana, etc).
    We can implement this interface to do so.
 */
internal interface ObservabilityMonitor {

    fun logError(message: String, attributes: Map<String, String> = emptyMap())
}