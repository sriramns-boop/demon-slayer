package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.state.GameScreen
import com.example.state.GameViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val gameViewModel: GameViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    Crossfade(
                        targetState = gameViewModel.currentScreen,
                        animationSpec = tween(300),
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            GameScreen.SPLASH -> SplashScreen(
                                onStartGame = { gameViewModel.navigateTo(GameScreen.MAIN_MENU) }
                            )
                            GameScreen.MAIN_MENU -> MainMenuScreen(
                                onNavigate = { gameViewModel.navigateTo(it) }
                            )
                            GameScreen.GAMEPLAY_HUD -> GameplayHudScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.BOSS_HUD -> BossHudScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.INVENTORY -> InventoryScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.SKILL_TREE -> SkillTreeScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.CHARACTER -> CharacterScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.WORLD_MAP -> WorldMapScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.QUEST_LOG -> QuestLogScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.DIALOGUE -> DialogueScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.PAUSE_MENU -> PauseMenuScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.VICTORY -> VictoryScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.DEFEAT -> DefeatScreen(
                                viewModel = gameViewModel
                            )
                            GameScreen.UI_SHOWCASE -> UiShowcaseSheetScreen(
                                viewModel = gameViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Demon Slayer: $name", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Blade of the Night") }
}
