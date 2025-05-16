package com.example.morsecode.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AuthUser
import com.example.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: AuthUseCase,
) : ViewModel() {

    private val _user = MutableStateFlow<AuthUser?>(null)
    val user: StateFlow<AuthUser?> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            _user.value = getCurrentUserUseCase.getCurrentUser()
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOut()
            _user.value = null
        }
    }
}
