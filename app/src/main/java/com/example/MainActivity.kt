package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import com.example.toonstudio.ui.ToonStudioApp
import com.example.toonstudio.ui.ToonStudioViewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StudioBackground

class MainActivity : ComponentActivity() {

    private val viewModel: ToonStudioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(StudioBackground)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    ToonStudioApp(viewModel = viewModel)
                }
            }
        }
    }
}
