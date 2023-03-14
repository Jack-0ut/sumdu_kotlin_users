package com.example.sumdu_kotlin_userdb

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.sumdu_kotlin_userdb.databinding.ActivityAddUserBinding

class AddUser : AppCompatActivity() {
    private lateinit var binding: ActivityAddUserBinding
    private var selectedImageUri: Uri? = null
    private lateinit var db: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = UserDatabaseHelper.getInstance(this)
        binding.imageViewPhoto.setOnClickListener {
            selectImageFromGallery()
        }

        binding.buttonSave.setOnClickListener {
            if (validateInput()) {
                db.insertData(
                    this,
                    binding.editTextFirstName.text.toString(),
                    binding.editTextLastName.text.toString(),
                    binding.editTextPhoneNumber.text.toString(),
                    binding.editTextEmailAddress.text.toString(),
                    binding.editTextHomeAddress.text.toString(),
                    selectedImageUri
                )
                Toast.makeText(this, "The data has been added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill all fields and choose a photo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Do something with the selected image URI
                // For example, set the image in the ImageView
                selectedImageUri = uri
                binding.imageViewPhoto.setImageURI(uri)
            }
        }

    private fun selectImageFromGallery() {
        pickImage.launch("image/*")
    }

    private fun validateInput(): Boolean {
        return binding.editTextFirstName.text.isNotEmpty() &&
                binding.editTextLastName.text.isNotEmpty() &&
                binding.editTextPhoneNumber.text.isNotEmpty() &&
                binding.editTextEmailAddress.text.isNotEmpty() &&
                binding.editTextHomeAddress.text.isNotEmpty() &&
                selectedImageUri != null
    }
}