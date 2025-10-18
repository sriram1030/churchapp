package com.ipcc.ipccchurch.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    isLoading: Boolean,
    errorMessage: String?,
    onRegisterClick: (name: String, email: String, pass: String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var termsAccepted by rememberSaveable { mutableStateOf(false) }

    AuthScaffold(
        title = "Sign up",
        footerText = "Already have an account?",
        footerLinkText = "Log in",
        onFooterLinkClick = onNavigateToLogin
    ) {
        // We don't need the Name field from your inspiration image, but keeping it as per your implementation
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Your email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter your password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, "Toggle password visibility")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("I accept the terms and privacy policy", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onRegisterClick(name, email, password) },
                enabled = termsAccepted,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Sign up")
            }
        }

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }
    }
}