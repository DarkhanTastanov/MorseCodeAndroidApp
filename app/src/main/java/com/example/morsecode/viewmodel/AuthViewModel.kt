package com.example.morsecode.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.remote.GoogleSignInIntentProvider
import com.example.domain.usecase.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthUseCase,
    private val googleSignInIntentProvider: GoogleSignInIntentProvider

) : ViewModel() {

    private val _authState = MutableLiveData<AuthResult>()
    val authState: LiveData<AuthResult> = _authState

    fun isUserSignedIn(): Boolean {
        return authRepository.isUserSignedIn()
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthResult.Loading
            val result = authRepository.signInWithGoogle(idToken)
            _authState.value = result.fold(
                onSuccess = { AuthResult.Success },
                onFailure = { AuthResult.Error(it.message ?: "Google sign in failed") }
            )
        }
    }

    fun getGoogleSignInIntent(): Intent {
        return googleSignInIntentProvider.getGoogleSignInIntent()
    }

    suspend fun getCurrentUserId(): String? = authRepository.getCurrentUser().uid

}

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
}