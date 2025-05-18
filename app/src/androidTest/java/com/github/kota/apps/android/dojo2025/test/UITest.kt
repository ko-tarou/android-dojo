package com.github.kota.apps.android.dojo2025.test

import ChatScreen
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.github.kota.apps.gemini.ui.SettingsScreen
import org.junit.Rule
import org.junit.Test

class ScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testTestDisplayed(){
        composeTestRule.setContent {
            SettingsScreen({},{},{})
        }

//        composeTestRule.onRoot().printToLog("ScreenTest")
        composeTestRule.onNodeWithText("kota").assertIsDisplayed()
    }
}