package com.example.morsecode.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AuthUser
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<AuthUser>>(emptyList())
    val users: StateFlow<List<AuthUser>> get() = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private var searchJob: kotlinx.coroutines.Job? = null

    fun searchUsers(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isBlank()) {
                _users.value = emptyList()
                return@launch
            }

            _isLoading.value = true
            val result = userRepository.searchUsers(query)
            _users.value = result
            _isLoading.value = false
        }
    }
}
