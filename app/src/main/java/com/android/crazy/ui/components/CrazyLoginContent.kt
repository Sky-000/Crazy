package com.android.crazy.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.crazy.R
import com.android.crazy.ui.theme.CrazyTheme
import com.android.crazy.ui.viewmodel.CrazyProfileUIState

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = stringResource(id = R.string.email),
                modifier = Modifier
                    .padding(start = 16.dp),
                tint = MaterialTheme.colorScheme.outline,
            )
            TextField(
                value = uiState.loginForm.email,
                onValueChange = { onEmailChange(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 30.dp),
                placeholder = { Text(text = stringResource(id = R.string.placeholder_email)) },
                label = { Text(text = stringResource(id = R.string.email)) },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    placeholderColor = MaterialTheme.colorScheme.outline,
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(onNext = {
                    localFocusManager.moveFocus(focusDirection = FocusDirection.Down)
                }),
                visualTransformation = VisualTransformation.None,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val passwordVisibility = remember { mutableStateOf(false) }
            val iconColor by animateColorAsState(
                targetValue = if (passwordVisibility.value) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }
            )
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = stringResource(id = R.string.password),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { passwordVisibility.value = !passwordVisibility.value },
                tint = iconColor,
            )
            TextField(
                value = uiState.loginForm.password,
                onValueChange = { onPasswordChange(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 30.dp),
                placeholder = { Text(text = stringResource(id = R.string.placeholder_password)) },
                label = { Text(text = stringResource(id = R.string.password)) },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    placeholderColor = MaterialTheme.colorScheme.outline,
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    login()
                }),
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            )
        }
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
            onClick = {
                login()
                keyboardController?.hide()
            },
            enabled = uiState.loginForm.email.isNotEmpty() && uiState.loginForm.password.isNotEmpty(),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
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
}

@Preview
@Composable
fun CrazyUserLoginPreview() {
    CrazyTheme(dynamicColor = false) {
        CrazyLoginContent(
            uiState = CrazyProfileUIState(),
            onEmailChange = {},
            onPasswordChange = {},
            login = {},
        )
    }
}