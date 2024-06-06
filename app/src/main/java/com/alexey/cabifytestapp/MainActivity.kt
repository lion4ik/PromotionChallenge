package com.alexey.cabifytestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.alexey.cabifytestapp.ui.AppScreens
import com.alexey.cabifytestapp.ui.theme.CabifyTestAppTheme

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CabifyTestAppTheme {
                AppScreens(modifier = Modifier.fillMaxSize())
            }
        }
    }
}