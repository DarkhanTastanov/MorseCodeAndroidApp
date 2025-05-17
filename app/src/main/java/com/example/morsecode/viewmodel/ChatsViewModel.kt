package com.example.morsecode.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AuthUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatsViewModel(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _users = MutableStateFlow<List<AuthUser>>(emptyList())
    val users: StateFlow<List<AuthUser>> get() = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private var searchJob: kotlinx.coroutines.Job? = null

    fun searchUsers(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            if (query.isBlank()) {
                _users.value = emptyList()
                return@launch
            }

            _isLoading.value = true
            firestore.collection("users")
                .get()
                .addOnSuccessListener { documents ->
                    val userList = documents.mapNotNull { doc ->
                        doc.toObject(AuthUser::class.java)
                    }.filter { user ->
                        user.name?.contains(query, ignoreCase = true) == true ||
                                user.email?.contains(query, ignoreCase = true) == true
                    }
                    _users.value = userList
                }
                .addOnFailureListener {
                    _users.value = emptyList()
                }
                .addOnCompleteListener {
                    _isLoading.value = false
                }
        }
    }

}
