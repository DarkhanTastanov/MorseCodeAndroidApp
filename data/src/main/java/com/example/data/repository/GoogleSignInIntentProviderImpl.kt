package com.example.data.repository

import android.content.Context
import android.content.Intent
import com.example.data.R
import com.example.data.remote.GoogleSignInIntentProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleSignInIntentProviderImpl(private val context: Context) : GoogleSignInIntentProvider {
    override fun getGoogleSignInIntent(): Intent {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
        return googleSignInClient.signInIntent
    }
}