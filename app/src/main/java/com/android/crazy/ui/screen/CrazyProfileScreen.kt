package com.android.crazy.ui.screen

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.android.crazy.R
import com.android.crazy.ui.theme.CrazyTheme
import com.android.crazy.ui.viewmodel.CrazyProfileUIState

@Composable
fun CrazyProfileScreen(
    crazyProfileUIState: CrazyProfileUIState,
    login: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (crazyProfileUIState.isLogin && crazyProfileUIState.user != null) {
            Text(
                text = "Welcome ${crazyProfileUIState.user.name}",
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            CrazyLoginContent(crazyProfileUIState = crazyProfileUIState, login = login)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CrazyLoginContent(
    crazyProfileUIState: CrazyProfileUIState,
    modifier: Modifier = Modifier,
    login: (String, String) -> Unit,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.inverseOnSurface,
    ) {
        val email = remember { mutableStateOf("15252114322@163.com") }
        val password = remember { mutableStateOf("wcnm741741..") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val localFocusManager = LocalFocusManager.current
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.login_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 32.dp),
            )
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
                    localFocusManager.moveFocus(focusDirection = FocusDirection.Down)
                })
            )
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
                    login(email.value, password.value)
                }),
                visualTransformation = PasswordVisualTransformation(),
            )
            CrazyLoginButton(
                crazyProfileUIState = crazyProfileUIState,
                enable = email.value.isNotEmpty() && password.value.isNotEmpty(),
                onClick = {
                    login(email.value, password.value)
                    keyboardController?.hide()
                })
        }
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
            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
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
    crazyProfileUIState: CrazyProfileUIState,
    enable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val transition =
        updateTransition(targetState = crazyProfileUIState.error != null, label = "shake")
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

    Button(
        onClick = { onClick() },
        enabled = enable,
        modifier = modifier
            .fillMaxWidth(if (crazyProfileUIState.loading) 0.3f else 1f)
            .offset(shakeOffset)
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .animateContentSize(),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp,
            disabledElevation = 2.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (crazyProfileUIState.error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            contentColor = if (crazyProfileUIState.error != null) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary,
        ),

        ) {
        if (crazyProfileUIState.loading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = crazyProfileUIState.error ?: stringResource(id = R.string.login),
                color = if (crazyProfileUIState.error != null) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium,
            )
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
            crazyProfileUIState = CrazyProfileUIState(),
            enable = true,
            onClick = { crazyProfileUIState.value = crazyProfileUIState.value.copy(loading = true) }
        )
    }
}