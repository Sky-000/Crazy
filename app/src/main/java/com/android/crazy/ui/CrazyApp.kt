/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.crazy.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.android.crazy.ui.navigation.*
import com.android.crazy.ui.screen.CrazyHomeScreen
import com.android.crazy.ui.screen.CrazyProfileScreen
import com.android.crazy.ui.screen.EmptyComingSoon
import com.android.crazy.ui.utils.*
import com.android.crazy.ui.viewmodel.CrazyHomeViewModel
import com.android.crazy.ui.viewmodel.CrazyProfileViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch

@Composable
fun CrazyApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
) {
    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val navigationType: CrazyNavigationType
    val contentType: CrazyContentType

    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = CrazyNavigationType.BOTTOM_NAVIGATION
            contentType = CrazyContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = CrazyNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                CrazyContentType.DUAL_PANE
            } else {
                CrazyContentType.SINGLE_PANE
            }
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                CrazyNavigationType.NAVIGATION_RAIL
            } else {
                CrazyNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = CrazyContentType.DUAL_PANE
        }
        else -> {
            navigationType = CrazyNavigationType.BOTTOM_NAVIGATION
            contentType = CrazyContentType.SINGLE_PANE
        }
    }

    /**
     * Content inside Navigation Rail/Drawer can also be positioned at top, bottom or center for
     * ergonomics and reachability depending upon the height of the device.
     */
    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            CrazyNavigationContentPosition.TOP
        }
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            CrazyNavigationContentPosition.CENTER
        }
        else -> {
            CrazyNavigationContentPosition.TOP
        }
    }

    CrazyNavigationWrapper(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun CrazyNavigationWrapper(
    navigationType: CrazyNavigationType,
    contentType: CrazyContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: CrazyNavigationContentPosition,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberAnimatedNavController()
    val navigationActions = remember(navController) {
        CrazyNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: CrazyRoute.INBOX

    if (navigationType == CrazyNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        // TODO check on custom width of PermanentNavigationDrawer: b/232495216
        PermanentNavigationDrawer(drawerContent = {
            PermanentNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
            )
        }) {
            CrazyAppContent(
                navigationType = navigationType,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigationContentPosition = navigationContentPosition,
                    navigateToTopLevelDestination = navigationActions::navigateTo,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            drawerState = drawerState
        ) {
            CrazyAppContent(
                navigationType = navigationType,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
            ) {
                scope.launch {
                    drawerState.open()
                }
            }
        }
    }
}

@Composable
fun CrazyAppContent(
    modifier: Modifier = Modifier,
    navigationType: CrazyNavigationType,
    contentType: CrazyContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: CrazyNavigationContentPosition,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (CrazyTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == CrazyNavigationType.NAVIGATION_RAIL) {
            CrazyNavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = onDrawerClicked,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            CrazyNavHost(
                navController = navController,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationType = navigationType,
                modifier = Modifier.weight(1f),
            )
            AnimatedVisibility(visible = navigationType == CrazyNavigationType.BOTTOM_NAVIGATION) {
                CrazyBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CrazyNavHost(
    navController: NavHostController,
    contentType: CrazyContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: CrazyNavigationType,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = CrazyRoute.HOME,
    ) {
        composable(CrazyRoute.HOME) {
            val homeViewModel = hiltViewModel<CrazyHomeViewModel>()
            CrazyHomeScreen(
                viewModel = homeViewModel,
                contentType = contentType,
                navigationType = navigationType,
                displayFeatures = displayFeatures,
            )
        }
        composable(CrazyRoute.INBOX) {
            EmptyComingSoon()
        }
        composable(CrazyRoute.EXPLORE) {
            EmptyComingSoon()
        }
        composable(CrazyRoute.PROFILE) {
            val profileViewModel = hiltViewModel<CrazyProfileViewModel>()
            CrazyProfileScreen(
                viewModel = profileViewModel,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navController = navController,
            )
        }
    }
}
