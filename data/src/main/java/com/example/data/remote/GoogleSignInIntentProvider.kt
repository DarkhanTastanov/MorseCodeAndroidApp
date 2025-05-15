package com.example.data.remote

import android.content.Intent

interface GoogleSignInIntentProvider {
    fun getGoogleSignInIntent(): Intent
}
