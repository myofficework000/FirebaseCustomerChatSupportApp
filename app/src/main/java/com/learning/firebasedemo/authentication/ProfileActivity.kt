package com.learning.firebasedemo.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.learning.firebasedemo.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        binding.apply {
            bundle?.apply {
                txtEmail.text = get("email").toString()
                txtName.text = get("name").toString()

                Glide.with(this@ProfileActivity)
                    .load(get("profilePicture").toString())
                    .into(profilePicture)
            }
        }
    }
}