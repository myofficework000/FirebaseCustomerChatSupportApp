package com.learning.firebasedemo.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.learning.firebasedemo.databinding.ActivityPhoneBinding
import java.util.concurrent.TimeUnit

class PhoneActivity : AppCompatActivity() {

    private var number: String = ""
    private lateinit var binding: ActivityPhoneBinding
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener {
            register()
        }
        setUpCallBack()

    }

    private fun setUpCallBack() {
        callBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.i("tag", "onVerificationCompleted")
                finish()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.i("tag", "onVerificationFailed")
            }

            override fun onCodeSent(
                p0: String, p1:
                PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = p0
                resendToken = p1
                val intent = Intent(
                    this@PhoneActivity,
                    ConfirmOTPActivity::class.java
                )
                intent.putExtra("storedVerificationId", storedVerificationId)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun register() {
        number = binding.edPhone.text.toString()
        if (number.isNotEmpty()) {

            number = "+91 $number"
            sendVerification(number)
        }
    }

    private fun sendVerification(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callBack)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}