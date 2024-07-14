package com.app.dans_android.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val displayName: String? = "",
    val isDataValid: Boolean = false,
)