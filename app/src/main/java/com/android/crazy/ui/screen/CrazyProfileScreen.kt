package com.android.crazy.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.android.crazy.R
import com.android.crazy.ui.components.CrazyAppBar
import com.android.crazy.ui.components.CrazyProfileImage
import com.android.crazy.ui.theme.CrazyTheme
import com.android.crazy.ui.utils.CrazyContentType
import com.android.crazy.ui.viewmodel.CrazyProfilePage
import com.android.crazy.ui.viewmodel.CrazyProfileUIState
import com.android.crazy.ui.viewmodel.CrazyProfileViewModel
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
fun CrazyProfileScreen(
    viewModel: CrazyProfileViewModel,
    contentType: CrazyContentType,
    displayFeatures: List<DisplayFeature>,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    if (contentType == CrazyContentType.DUAL_PANE) {
        TwoPane(
            first = {
                CrazyProfileContent(uiState = uiState, modifier = modifier)
            },
            second = {
                AnimatedVisibility(
                    visible = uiState.page == CrazyProfilePage.PROFILE || uiState.page == CrazyProfilePage.SETTING,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        CrazyAppBar(
                            title = stringResource(id = R.string.user_setting),
                            isFullScreen = false
                        )
                        CrazyUserSetting(
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
                        CrazyAppBar(
                            isFullScreen = true,
                            onBackPressed = { viewModel.navigateToSetting() })
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
                onCardClick = { viewModel.navigateToSetting() })
        }
        AnimatedVisibility(
            visible = uiState.page == CrazyProfilePage.SETTING,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                CrazyAppBar(
                    title = stringResource(id = R.string.user_setting),
                    isFullScreen = true,
                    onBackPressed = { viewModel.navigateToProfile() })
                CrazyUserSetting(
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
                CrazyAppBar(
                    isFullScreen = true,
                    onBackPressed = { viewModel.navigateToSetting() })
                CrazyLoginContent(
                    uiState = uiState,
                    onEmailChange = { viewModel.onEmailChange(it) },
                    onPasswordChange = { viewModel.onPasswordChange(it) },
                    login = { viewModel.login() },
                )
            }
        }
    }
}

@Composable
fun CrazyProfileContent(
    uiState: CrazyProfileUIState,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CrazyUserCard(
            uiState = uiState,
            onCardClick = onCardClick
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
            if (uiState.isLogin && uiState.user != null) {
                CrazyProfileImage(
                    url = uiState.user.avatar,
                    description = uiState.user.name,
                    modifier = Modifier.size(64.dp)
                )
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
                CrazyProfileImage(
                    drawableResource = R.drawable.avatar_1,
                    description = "Crazy",
                    modifier = Modifier.size(64.dp)
                )
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
fun CrazyUserSetting(
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
        AnimatedVisibility(uiState.isLogin) {
            CrazyUserSettingCard(
                settingName = stringResource(id = R.string.logout),
                onClick = { logout() },
                loading = uiState.loading
            )
        }
        AnimatedVisibility(!uiState.isLogin) {
            CrazyUserSettingCard(
                settingName = stringResource(id = R.string.login),
                onClick = { navigateToLogin() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrazyUserSettingCard(
    settingName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    loading: Boolean = false
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
        onClick = { onClick() }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = settingName,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(visible = loading, enter = fadeIn()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.outline,
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CrazyLoginContent(
    uiState: CrazyProfileUIState,
    modifier: Modifier = Modifier,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    login: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.login_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 64.dp),
        )
        CrazyLoginTextField(
            value = uiState.loginForm.email,
            onValueChange = { onEmailChange(it) },
            imageVector = Icons.Default.Email,
            contentDescription = null,
            placeholder = stringResource(id = R.string.email),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(onNext = {
                localFocusManager.moveFocus(focusDirection = FocusDirection.Down)
            }),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
        CrazyLoginTextField(
            value = uiState.loginForm.password,
            onValueChange = { onPasswordChange(it) },
            imageVector = Icons.Default.Password,
            contentDescription = null,
            placeholder = stringResource(id = R.string.password),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                login()
            }),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
        CrazyLoginButton(
            uiState = uiState,
            enable = uiState.loginForm.email.isNotEmpty() && uiState.loginForm.password.isNotEmpty(),
            login = {
                login()
                keyboardController?.hide()
            },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrazyLoginTextField(
    contentDescription: String?,
    imageVector: ImageVector,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    value: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, CircleShape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.padding(start = 16.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 24.dp),
            placeholder = { Text(text = placeholder) },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface,
                placeholderColor = MaterialTheme.colorScheme.outline,
                containerColor = MaterialTheme.colorScheme.surface
            ),
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation
        )
    }
}

@Composable
fun CrazyLoginButton(
    uiState: CrazyProfileUIState,
    enable: Boolean,
    login: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val transition =
        updateTransition(targetState = uiState.error != null, label = "shake")
    val shakeOffset by transition.animateDp(
        transitionSpec = {
            if (true isTransitioningTo false) {
                tween(durationMillis = 100)
            } else {
                keyframes {
                    durationMillis = 500
                    0.dp at 0
                    10.dp at 100
                    (-10).dp at 200
                    10.dp at 300
                    (-10).dp at 400
                    0.dp at 500
                }
            }
        }, label = "shakeOffset"
    ) { targetState ->
        if (targetState) 0.dp else 0.dp
    }
    val containerColor by animateColorAsState(targetValue = if (uiState.error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
    val contentColor by animateColorAsState(targetValue = if (uiState.error != null) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary)
    Button(
        onClick = { login() },
        enabled = enable,
        modifier = modifier
            .fillMaxWidth(if (uiState.loading) 0.3f else 1f)
            .height(40.dp)
            .offset(shakeOffset)
            .animateContentSize(),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp,
            disabledElevation = 2.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),

        ) {
        AnimatedVisibility(visible = uiState.loading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        }
        AnimatedVisibility(visible = !uiState.loading) {
            val textColor by animateColorAsState(targetValue = if (uiState.error != null) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary)
            Text(
                text = uiState.error ?: stringResource(id = R.string.login),
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
            )
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
            CrazyUserSettingCard(settingName = "Login")
            Spacer(modifier = Modifier.height(8.dp))
            CrazyUserSettingCard(settingName = "Logout")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun CrazyLoginTextFieldEmailPreview() {
    CrazyTheme(dynamicColor = false) {
        val email = remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        CrazyLoginTextField(
            value = email.value,
            onValueChange = { email.value = it },
            imageVector = Icons.Default.Email,
            contentDescription = null,
            placeholder = stringResource(id = R.string.email),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(onNext = {
                keyboardController?.hide()
            })
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun CrazyLoginTextFieldPasswordPreview() {
    CrazyTheme(dynamicColor = false) {
        val password = remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        CrazyLoginTextField(
            value = password.value,
            onValueChange = { password.value = it },
            imageVector = Icons.Default.Password,
            contentDescription = null,
            placeholder = stringResource(id = R.string.password),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )
    }
}

@Preview
@Composable
fun CrazyLoginButtonPreview() {
    CrazyTheme(dynamicColor = false) {
        val crazyProfileUIState = remember {
            mutableStateOf(CrazyProfileUIState())
        }
        CrazyLoginButton(
            uiState = CrazyProfileUIState(),
            enable = true,
            login = { crazyProfileUIState.value = crazyProfileUIState.value.copy(loading = true) }
        )
    }
}