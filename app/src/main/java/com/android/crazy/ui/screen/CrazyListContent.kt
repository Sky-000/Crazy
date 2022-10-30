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

package com.android.crazy.ui.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.android.crazy.R
import com.android.crazy.data.model.Email
import com.android.crazy.ui.components.CrazyEmailListItem
import com.android.crazy.ui.components.CrazySearchBar
import com.android.crazy.ui.components.EmailDetailAppBar
import com.android.crazy.ui.components.ReplyEmailThreadItem
import com.android.crazy.ui.utils.CrazyContentType
import com.android.crazy.ui.utils.CrazyNavigationType
import com.android.crazy.ui.viewmodel.CrazyHomeUIState
import com.android.crazy.ui.viewmodel.CrazyHomeViewModel
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
fun CrazyInboxScreen(
    viewModel: CrazyHomeViewModel,
    contentType: CrazyContentType,
    navigationType: CrazyNavigationType,
    displayFeatures: List<DisplayFeature>,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    /**
     * When moving from LIST_AND_DETAIL page to LIST page clear the selection and user should see LIST screen.
     */
    LaunchedEffect(key1 = contentType) {
        if (contentType == CrazyContentType.SINGLE_PANE && !uiState.isDetailOnlyOpen) {
            viewModel.closeDetailScreen()
        }
    }

    val emailLazyListState = rememberLazyListState()

    if (contentType == CrazyContentType.DUAL_PANE) {
        TwoPane(
            first = {
                CrazyEmailList(
                    crazyHomeUIState = uiState,
                    emails = uiState.emails,
                    emailLazyListState = emailLazyListState,
                    navigateToDetail = { emailId, pane ->
                        viewModel.setSelectedEmail(emailId, pane)
                    },
                    onSearchQueryChange = { query ->
                        viewModel.onSearchQueryChange(query)
                    },
                )
            },
            second = {
                CrazyEmailDetail(
                    email = uiState.selectedEmail ?: uiState.emails.first(),
                    isFullScreen = false
                )
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            CrazySinglePaneContent(
                crazyHomeUIState = uiState,
                emailLazyListState = emailLazyListState,
                modifier = Modifier.fillMaxSize(),
                closeDetailScreen = { viewModel.closeDetailScreen() },
                navigateToDetail = { emailId, pane ->
                    viewModel.setSelectedEmail(emailId, pane)
                },
                onSearchQueryChange = { query ->
                    viewModel.onSearchQueryChange(query)
                },
            )
            // When we have bottom navigation we show FAB at the bottom end.
            if (navigationType == CrazyNavigationType.BOTTOM_NAVIGATION) {
                LargeFloatingActionButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(64.dp),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CrazySinglePaneContent(
    crazyHomeUIState: CrazyHomeUIState,
    emailLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, CrazyContentType) -> Unit,
    onSearchQueryChange: (String) -> Unit,
) {
    if (crazyHomeUIState.selectedEmail != null && crazyHomeUIState.isDetailOnlyOpen) {
        BackHandler {
            closeDetailScreen()
        }
        CrazyEmailDetail(email = crazyHomeUIState.selectedEmail) {
            closeDetailScreen()
        }
    } else {
        CrazyEmailList(
            crazyHomeUIState = crazyHomeUIState,
            emails = crazyHomeUIState.emails,
            emailLazyListState = emailLazyListState,
            modifier = modifier,
            navigateToDetail = navigateToDetail,
            onSearchQueryChange = onSearchQueryChange
        )
    }
}

@Composable
fun CrazyEmailList(
    crazyHomeUIState: CrazyHomeUIState,
    emails: List<Email>,
    emailLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long, CrazyContentType) -> Unit,
    onSearchQueryChange: (String) -> Unit,
) {
    LazyColumn(modifier = modifier, state = emailLazyListState) {
        item {
            CrazySearchBar(
                modifier = Modifier.fillMaxWidth(),
                value = crazyHomeUIState.searchQuery,
                onValueChange = { onSearchQueryChange(it) })
        }
        items(items = emails, key = { it.id }) { email ->
            CrazyEmailListItem(email = email) { emailId ->
                navigateToDetail(emailId, CrazyContentType.SINGLE_PANE)
            }
        }
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun CrazyEmailDetail(
    email: Email,
    isFullScreen: Boolean = true,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(top = 16.dp)
    ) {
        item {
            EmailDetailAppBar(email, isFullScreen) {
                onBackPressed()
            }
        }
        items(items = email.threads, key = { it.id }) { email ->
            ReplyEmailThreadItem(email = email)
        }
    }
}
