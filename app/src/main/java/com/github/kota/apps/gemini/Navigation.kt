import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.kota.apps.gemini.ui.ApiSettingsScreen
import com.github.kota.apps.gemini.ui.SettingsScreen
import com.github.kota.apps.gemini.ui.SystemSettingScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationPortal() {
    val navController = rememberAnimatedNavController() // AnimatedNavControllerを使用

    LaunchedEffect(Unit) {
        navController.navigate("chat") {
            popUpTo("chat") { inclusive = true }
        }
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = "chat",
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        composable("chat") {
            ChatScreen(
                onSettingsClick = { navController.navigate("settings") }
            )
        }
        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.popBackStack() } ,
                onApiKeyClick = { navController.navigate("apisettings") },
                onSystemClick = { navController.navigate("systemsettings") }
            )
        }
        composable("apisettings") {
            ApiSettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("systemsettings") {
            SystemSettingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
