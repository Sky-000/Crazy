package com.android.crazy.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
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
import com.android.crazy.R
import com.android.crazy.ui.theme.CrazyTheme
import com.android.crazy.ui.viewmodel.CrazyProfileUIState

@Composable
fun CrazyProfileContent(
    uiState: CrazyProfileUIState,
    modifier: Modifier = Modifier,
    onUserCardClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CrazyUserCard(
            uiState = uiState,
            onCardClick = onUserCardClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        CrazyFunctionCard(
            name = stringResource(id = R.string.setting),
            icon = Icons.Outlined.Settings,
            contentDescription = stringResource(id = R.string.setting),
            onClick = { onSettingsClick() }
        )
        CrazyFunctionCard(
            name = stringResource(id = R.string.about),
            icon = Icons.Outlined.Menu,
            contentDescription = stringResource(id = R.string.about),
            onClick = { onAboutClick() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrazyUserCard(
    uiState: CrazyProfileUIState,
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

@Composable
fun CrazyUserSettings(
    uiState: CrazyProfileUIState,
    modifier: Modifier = Modifier,
    logout: () -> Unit,
    navigateToLogin: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(uiState.user != null) {
            Column {
                CrazyFunctionCard(
                    name = uiState.user?.name ?: "",
                    icon = Icons.Outlined.Person,
                    contentDescription = null,
                    onClick = { })
                CrazyFunctionCard(
                    name = uiState.user?.phone ?: "",
                    icon = Icons.Outlined.Phone,
                    contentDescription = null,
                    onClick = { })
                CrazyFunctionCard(
                    name = uiState.user?.email ?: "",
                    icon = Icons.Outlined.Email,
                    contentDescription = null,
                    onClick = { })
                CrazyFunctionCard(
                    name = stringResource(id = R.string.logout),
                    icon = Icons.Outlined.Logout,
                    contentDescription = stringResource(id = R.string.logout),
                    onClick = { logout() },
                    loading = uiState.loading)
            }
        }
        AnimatedVisibility(uiState.user == null) {
            Column {
                CrazyFunctionCard(
                    name = stringResource(id = R.string.login),
                    icon = Icons.Outlined.Login,
                    contentDescription = stringResource(id = R.string.login),
                    onClick = { navigateToLogin() })
                CrazyFunctionCard(
                    name = stringResource(id = R.string.register),
                    icon = Icons.Outlined.PersonAdd,
                    contentDescription = stringResource(id = R.string.login),
                    onClick = { })
            }
        }
    }
}

@SuppressLint("ModifierParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrazyFunctionCard(
    name: String,
    icon: ImageVector,
    contentDescription: String? = null,
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
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = name,
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
                    imageVector = Icons.Outlined.ArrowForwardIos,
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
        uiState = CrazyProfileUIState(),
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
                name = stringResource(id = R.string.login),
                icon = Icons.Outlined.Login,
                onClick = {})
            CrazyFunctionCard(
                name = stringResource(id = R.string.logout),
                icon = Icons.Outlined.Logout,
                onClick = {})
        }
    }
}