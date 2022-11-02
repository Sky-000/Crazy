package com.android.crazy.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.window.layout.DisplayFeature
import com.android.crazy.ui.components.*
import com.android.crazy.ui.utils.CrazyContentType
import com.android.crazy.ui.viewmodel.CrazyProfilePage
import com.android.crazy.ui.viewmodel.CrazyProfileViewModel
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
fun CrazyProfileScreen(
    viewModel: CrazyProfileViewModel,
    contentType: CrazyContentType,
    displayFeatures: List<DisplayFeature>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        when (uiState.page) {
            CrazyProfilePage.LOGIN -> {
                viewModel.navigateToUserSettings()
            }
            CrazyProfilePage.USER_SETTINGS -> {
                viewModel.navigateToProfile()
            }
            CrazyProfilePage.PROFILE -> {
                navController.navigateUp()
            }
            CrazyProfilePage.ABOUT -> {
                viewModel.navigateToProfile()
            }
            CrazyProfilePage.SETTINGS -> {
                viewModel.navigateToProfile()
            }
        }
    }

    if (contentType == CrazyContentType.DUAL_PANE) {
        TwoPane(
            first = {
                CrazyProfileContent(uiState = uiState, modifier = modifier)
            },
            second = {
                AnimatedVisibility(
                    visible = uiState.page == CrazyProfilePage.PROFILE || uiState.page == CrazyProfilePage.USER_SETTINGS,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    CrazyUserSettings(
                        uiState = uiState,
                        logout = { viewModel.logout() },
                        navigateToLogin = { viewModel.navigateToLogin() },
                        modifier = modifier.padding(top = 32.dp)
                    )
                }
                AnimatedVisibility(
                    visible = uiState.page == CrazyProfilePage.LOGIN,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        CrazyBackAppBar(
                            onBackPressed = { viewModel.navigateToProfile() })
                        CrazyLoginContent(
                            uiState = uiState,
                            onEmailChange = { viewModel.onEmailChange(it) },
                            onPasswordChange = { viewModel.onPasswordChange(it) },
                            login = { viewModel.login() },
                        )
                    }
                }
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        )
    } else {
        AnimatedVisibility(
            visible = uiState.page == CrazyProfilePage.PROFILE,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            CrazyProfileContent(
                uiState = uiState,
                modifier = modifier,
                onUserCardClick = { viewModel.navigateToUserSettings() },
                onAboutClick = { viewModel.navigateToAbout() },
                onSettingsClick = { viewModel.navigateToSettings() })
        }
        AnimatedVisibility(
            visible = uiState.page == CrazyProfilePage.USER_SETTINGS,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                CrazyBackAppBar(
                    onBackPressed = { viewModel.navigateToProfile() })
                CrazyUserSettings(
                    uiState = uiState,
                    logout = { viewModel.logout() },
                    navigateToLogin = { viewModel.navigateToLogin() })
            }
        }
        AnimatedVisibility(
            visible = uiState.page == CrazyProfilePage.LOGIN,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                CrazyBackAppBar(
                    onBackPressed = { viewModel.navigateToUserSettings() })
                CrazyLoginContent(
                    uiState = uiState,
                    onEmailChange = { viewModel.onEmailChange(it) },
                    onPasswordChange = { viewModel.onPasswordChange(it) },
                    login = { viewModel.login() },
                )
            }
        }
        AnimatedVisibility(
            visible = uiState.page == CrazyProfilePage.SETTINGS,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {

        }
        AnimatedVisibility(
            visible = uiState.page == CrazyProfilePage.ABOUT,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            CrazyAboutContent()
        }
    }
}

