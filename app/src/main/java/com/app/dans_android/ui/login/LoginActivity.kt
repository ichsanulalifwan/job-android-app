package com.app.dans_android.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.app.dans_android.databinding.ActivityLoginBinding
import com.app.dans_android.util.constant.FacebookOauthConstant
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel: LoginViewModel by viewModels()
    private val auth by lazy {
        Firebase.auth
    }
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnGoogleLogin.setOnClickListener {

            }
        }

        loginGoogle()
        loginFacebook()
    }

    private fun loginGoogle() {
        // Configure Google Sign In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun loginFacebook() {
        FacebookSdk.sdkInitialize(this)
        this.let { AppEventsLogger.activateApp(it.application) }
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val facebookAccessToken = result.accessToken.token
                    Log.d("LoginFacebookToken", facebookAccessToken)

                }

                override fun onCancel() {
                    Log.w("Login Facebook Cancel", "onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.w("Login Facebook Error", error.toString())
                    Toast.makeText(this@LoginActivity, error.toString(), Toast.LENGTH_LONG).show()
                }
            })

        binding.btnFbLogin.setOnClickListener {
            facebookSignOut()
            LoginManager.getInstance().logInWithReadPermissions(
                this, listOf(FacebookOauthConstant.PUBLIC_PROFILE, FacebookOauthConstant.EMAIL)
            )
        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun facebookSignOut(message: String? = null) {
        LoginManager.getInstance().logOut()
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}