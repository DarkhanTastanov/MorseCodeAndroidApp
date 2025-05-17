package com.example.morsecode.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import com.example.morsecode.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onEditProfile: () -> Unit,
    onAppSettings: () -> Unit
) {
    val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user?.let {
            AsyncImage(
                model = it.photoUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it.name ?: "No Name",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it.email ?: "No Email", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onEditProfile, modifier = Modifier.fillMaxWidth()) {
            Text("Edit Profile")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onAppSettings, modifier = Modifier.fillMaxWidth()) {
            Text("App Settings")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { viewModel.signOut() }, modifier = Modifier.fillMaxWidth()) {
            Text("Sign Out")
        }
    }
}
