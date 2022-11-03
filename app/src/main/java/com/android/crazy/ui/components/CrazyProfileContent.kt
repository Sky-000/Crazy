package com.android.crazy.ui.components

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.crazy.R
import com.android.crazy.data.model.User
import com.android.crazy.ui.theme.CrazyTheme
import com.android.crazy.ui.viewmodel.CrazyProfileUiState
import com.android.crazy.ui.viewmodel.ModifyType

@Composable
fun CrazyProfileContent(
    uiState: CrazyProfileUiState,
    navController: NavController,
    modifier: Modifier = Modifier,
    onUserCardClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
) {
    BackHandler { navController.navigateUp() }
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, top = 16.dp)
    ) {
        item {
            CrazyUserCard(
                uiState = uiState,
                onCardClick = onUserCardClick
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            CrazyFunctionCard(
                function = stringResource(id = R.string.setting),
                leftIcon = Icons.Outlined.Settings,
                rightIcon = Icons.Outlined.ArrowForwardIos,
                onClick = { onSettingsClick() }
            )
        }
        item {
            CrazyFunctionCard(
                function = stringResource(id = R.string.about),
                leftIcon = Icons.Outlined.Info,
                rightIcon = Icons.Outlined.ArrowForwardIos,
                onClick = { onAboutClick() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrazyUserCard(
    uiState: CrazyProfileUiState,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp,
            focusedElevation = 4.dp,
            disabledElevation = 0.dp,
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onCardClick() }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (uiState.user != null) {
                Column(modifier = Modifier.padding(vertical = 24.dp)) {
                    CrazyProfileImage(
                        url = uiState.user.avatar,
                        description = uiState.user.name,
                        modifier = Modifier.size(64.dp)
                    )
                }
                Column {
                    Text(
                        text = uiState.user.name,
                        modifier = Modifier.padding(start = 16.dp),
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = uiState.user.phone,
                        modifier = Modifier.padding(start = 16.dp),
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = uiState.user.email,
                        modifier = Modifier.padding(start = 16.dp),
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            } else {
                Column(modifier = Modifier.padding(vertical = 24.dp)) {
                    CrazyProfileImage(
                        drawableResource = R.drawable.avatar_1,
                        description = "Crazy",
                        modifier = Modifier.size(64.dp)
                    )
                }
                Column {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier.padding(start = 16.dp),
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun CrazyUserSettings(
    modifier: Modifier = Modifier,
    logout: () -> Unit,
    modifyUser: (ModifyType) -> Unit,
    navigateToLogin: () -> Unit,
    onBackPress: () -> Unit,
    user: User?,
    loading: Boolean = false,
) {
    BackHandler { onBackPress() }
    val lazyListState = rememberLazyListState()
    Column {
        CrazyBackAppBar(onBackPressed = { onBackPress() })
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyListState
        ) {
            val functionCardList = if (user != null) listOf(
                FunctionCard(
                    function = user.name,
                    leftIcon = Icons.Outlined.Person,
                    rightIcon = Icons.Outlined.Edit,
                    onClick = { modifyUser(ModifyType.NAME) }
                ),
                FunctionCard(
                    function = user.phone,
                    leftIcon = Icons.Outlined.Phone,
                    rightIcon = Icons.Outlined.Edit,
                    onClick = { modifyUser(ModifyType.PHONE) }
                ),
                FunctionCard(
                    function = user.email,
                    leftIcon = Icons.Outlined.Email,
                    rightIcon = Icons.Outlined.Edit,
                    onClick = { modifyUser(ModifyType.EMAIL) }
                ),
                FunctionCard(
                    function = "Logout",
                    leftIcon = Icons.Outlined.Logout,
                    rightIcon = Icons.Outlined.ArrowForwardIos,
                    onClick = { logout() },
                    loading = loading
                )
            ) else listOf(
                FunctionCard(
                    function = "Login",
                    leftIcon = Icons.Outlined.Login,
                    rightIcon = Icons.Outlined.ArrowForwardIos,
                    onClick = { navigateToLogin() }
                ),
                FunctionCard(
                    function = "Register",
                    leftIcon = Icons.Outlined.PersonAdd,
                    rightIcon = Icons.Outlined.ArrowForwardIos,
                    onClick = { navigateToLogin() }
                )
            )
            items(items = functionCardList, key = { it.function }) { functionCard ->
                CrazyFunctionCard(
                    function = functionCard.function,
                    leftIcon = functionCard.leftIcon,
                    rightIcon = functionCard.rightIcon,
                    onClick = { functionCard.onClick() },
                    loading = functionCard.loading,
                )
            }
        }
    }
}

@SuppressLint("ModifierParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrazyFunctionCard(
    function: String,
    leftIcon: ImageVector,
    rightIcon: ImageVector,
    onClick: () -> Unit = {},
    loading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp,
            focusedElevation = 4.dp,
            disabledElevation = 0.dp,
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick() }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = leftIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = function,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(visible = loading, enter = fadeIn()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            }
            AnimatedVisibility(visible = !loading, enter = fadeIn()) {
                Icon(
                    imageVector = rightIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun CrazyUserContentPreview() {
    CrazyUserCard(
        uiState = CrazyProfileUiState(),
        onCardClick = {},
    )
}

@Preview
@Composable
fun CrazyUserSettingsPreview() {
    CrazyTheme(dynamicColor = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            CrazyFunctionCard(
                function = stringResource(id = R.string.login),
                leftIcon = Icons.Outlined.Login,
                rightIcon = Icons.Outlined.ArrowForwardIos,
                onClick = {})
            CrazyFunctionCard(
                function = stringResource(id = R.string.logout),
                leftIcon = Icons.Outlined.Logout,
                rightIcon = Icons.Outlined.ArrowForwardIos,
                onClick = {})
        }
    }
}

data class FunctionCard(
    val function: String,
    val leftIcon: ImageVector,
    val rightIcon: ImageVector,
    val onClick: () -> Unit = {},
    val loading: Boolean = false,
)