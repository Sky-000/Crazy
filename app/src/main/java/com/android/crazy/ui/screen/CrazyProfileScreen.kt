package com.android.crazy.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
    contentType: CrazyContentType,
    displayFeatures: List<DisplayFeature>,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CrazyProfileViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    if (contentType == CrazyContentType.DUAL_PANE) {
        TwoPane(
            first = {
                CrazyProfileContent(
                    modifier = modifier,
                    navController = navController,
                    uiState = uiState,
                )
            },
            second = {
                DisplayPageWithAnimation(visible = uiState.page == CrazyProfilePage.PROFILE || uiState.page == CrazyProfilePage.USER_SETTINGS) {
                    CrazyUserSettings(
                        logout = { viewModel.logout() },
                        modifyUser = { viewModel.modifyUser(it) },
                        navigateToLogin = { viewModel.navigateToLogin() },
                        onBackPress = { viewModel.navigateToProfile() },
                        user = uiState.user,
                        loading = uiState.loading,
                    )
                }

                DisplayPageWithAnimation(visible = uiState.page == CrazyProfilePage.LOGIN) {
                    CrazyLoginContent(
                        login = { viewModel.login() },
                        onBackPressed = { viewModel.navigateToUserSettings() },
                        onEmailChange = { viewModel.onEmailChange(it) },
                        onPasswordChange = { viewModel.onPasswordChange(it) },
                        uiState = uiState,
                    )
                }

                DisplayPageWithAnimation(visible = uiState.page == CrazyProfilePage.SETTINGS) {
                    CrazyAboutContent(onBackPressed = { viewModel.navigateToProfile() })
                }

                DisplayPageWithAnimation(visible = uiState.page == CrazyProfilePage.ABOUT) {
                    CrazyAboutContent(onBackPressed = { viewModel.navigateToProfile() })
                }

                DisplayPageWithAnimation(visible = uiState.page == CrazyProfilePage.MODIFY) {
                    CrazyModifyContent(
                        onBackPressed = viewModel::navigateToUserSettings,
                        onDone = { uiState.modifyState.onDone(it) },
                        title = uiState.modifyState.title,
                        value = uiState.modifyState.value,
                        loading = uiState.loading,
                    )
                }

            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        )
    } else {
        DisplayPageWithAnimation(uiState.page == CrazyProfilePage.PROFILE) {
            CrazyProfileContent(
                modifier = modifier,
                navController = navController,
                onAboutClick = { viewModel.navigateToAbout() },
                onSettingsClick = { viewModel.navigateToSettings() },
                onUserCardClick = { viewModel.navigateToUserSettings() },
                uiState = uiState,
            )
        }

        DisplayPageWithAnimation(uiState.page == CrazyProfilePage.USER_SETTINGS) {
            CrazyUserSettings(
                logout = { viewModel.logout() },
                modifyUser = { viewModel.modifyUser(it) },
                navigateToLogin = { viewModel.navigateToLogin() },
                onBackPress = { viewModel.navigateToProfile() },
                user = uiState.user,
                loading = uiState.loading,
            )

        }

        DisplayPageWithAnimation(uiState.page == CrazyProfilePage.LOGIN) {
            CrazyLoginContent(
                login = { viewModel.login() },
                onBackPressed = { viewModel.navigateToUserSettings() },
                onEmailChange = { viewModel.onEmailChange(it) },
                onPasswordChange = { viewModel.onPasswordChange(it) },
                uiState = uiState,
            )
        }

        DisplayPageWithAnimation(uiState.page == CrazyProfilePage.SETTINGS) {
            CrazyAboutContent(onBackPressed = { viewModel.navigateToProfile() })
        }

        DisplayPageWithAnimation(uiState.page == CrazyProfilePage.ABOUT) {
            CrazyAboutContent(onBackPressed = { viewModel.navigateToProfile() })
        }

        DisplayPageWithAnimation(uiState.page == CrazyProfilePage.MODIFY) {
            CrazyModifyContent(
                onBackPressed = viewModel::navigateToUserSettings,
                onDone = { uiState.modifyState.onDone(it) },
                title = uiState.modifyState.title,
                value = uiState.modifyState.value,
                loading = uiState.loading,
            )
        }
    }
}

@Composable
fun DisplayPageWithAnimation(
    visible: Boolean,
    screen: @Composable () -> Unit,
) {
    AnimatedVisibility(visible = visible, enter = expandVertically(), exit = shrinkVertically()) {
        screen()
    }
}
