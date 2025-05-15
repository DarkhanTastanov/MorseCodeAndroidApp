package com.example.morsecode.compose.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.morsecode.R
import com.example.morsecode.viewmodel.AuthViewModel
import com.example.morsecode.viewmodel.AuthResult
import com.google.android.gms.auth.api.signin.GoogleSignIn

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val authState by authViewModel.authState.observeAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.result
            val idToken = account.idToken
            idToken?.let { authViewModel.signInWithGoogle(it) }
//            navController.navigate("translator") // Navigate to translator

        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val googleIntent = authViewModel.getGoogleSignInIntent()
                googleSignInLauncher.launch(googleIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Google Sign-In",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign in with Google",
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (authState) {
            is AuthResult.Loading -> CircularProgressIndicator()
            is AuthResult.Success -> {
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                navController.navigate("translator") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthResult.Error ->
                Toast.makeText(context, (authState as AuthResult.Error).message, Toast.LENGTH_SHORT).show()
            else -> {}
        }
    }
}
