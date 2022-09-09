package com.learning.firebasedemo.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.learning.firebasedemo.databinding.ActivityMainBinding
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var googleSignInClient: GoogleSignInClient
    var REQ_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareSignInOptions()
        initViews()
    }

    private fun prepareSignInOptions() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.btnGoogle.setOnClickListener {
            googleSignIntent()
        }
        binding.btnCrashApp.setOnClickListener {
            throw RuntimeException("Test crash :: Crash app when i got clicked")
        }
    }

    private fun googleSignIntent() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleTask(task)
        }
    }

    private fun handleTask(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            account?.let {
                updateUi(account)
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    private fun updateUi(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credentials)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    makeToast("login success")
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.apply {
                        account.apply {
                            putExtra("name", displayName)
                            putExtra("email", email)
                            putExtra("profilePicture", photoUrl)
                        }
                    }
                    startActivity(intent)
                } else {
                    makeToast("Login failed")
                }
            }
    }


    private fun initViews() {
        binding.btnRegister.setOnClickListener {
            doRegister()
        }
        binding.btnPhone.setOnClickListener {
            startActivity(Intent(this, PhoneActivity::class.java))
        }

    }

    private fun doRegister() {
        binding.apply {
            circularProgressBar.visibility = View.VISIBLE
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        makeToast("Successfully registered")
                        if (auth.currentUser != null) {
                            currentUser = auth.currentUser as FirebaseUser
                        }
                        sendEmailVerification()
                    } else {
                        makeToast("Failed to register!!")
                    }
                }
        }
    }

    private fun makeToast(s: String) {
        if (binding.circularProgressBar.visibility == View.VISIBLE)
            binding.circularProgressBar.visibility = View.GONE
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun sendEmailVerification() {
        currentUser.let {
            currentUser.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        makeToast("Email verification sent!!")
                    } else {
                        makeToast("Failed to send email verification")
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        if (this::currentUser.isInitialized) {
            if (auth.currentUser != null && currentUser != auth) {
                currentUser = auth.currentUser as FirebaseUser
            }
        }
    }

    companion object {
        const val CLIENT_ID =
            "194974599457-5lppmd0s76rn8nt6nhtu8sdkkkru67be.apps.googleusercontent.com"
    }
}