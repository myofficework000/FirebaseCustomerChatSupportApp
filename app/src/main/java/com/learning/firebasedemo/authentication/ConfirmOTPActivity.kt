package com.learning.firebasedemo.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.learning.firebasedemo.databinding.ActivityConfirmOtpactivityBinding

class ConfirmOTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmOtpactivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val storedVerificationId = intent.getStringExtra("storedVerificationId")

        binding.btnConfirmOtp.setOnClickListener {
            val otp = binding.edPhone.text.toString()
            if (otp.isNotEmpty()) {
                val credentials: PhoneAuthCredential =
                    PhoneAuthProvider.getCredential(storedVerificationId.toString(), otp)
                signInWithCredentials(credentials)
            }
        }
    }

    private fun signInWithCredentials(credentials: PhoneAuthCredential) {
        auth.signInWithCredential(credentials)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}