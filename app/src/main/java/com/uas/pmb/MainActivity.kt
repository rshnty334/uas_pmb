package com.uas.pmb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uas.pmb.ui.screens.PmbNavGraph
import com.uas.pmb.ui.theme.PmbTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // WAJIB ADA: Ini yang bikin error GeneratedInjector hilang
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PmbTheme {
                PmbNavGraph()
            }
        }
    }
}