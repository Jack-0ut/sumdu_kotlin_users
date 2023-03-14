package com.example.sumdu_kotlin_userdb

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.sumdu_kotlin_userdb.databinding.ActivityEditUserBinding
import java.io.ByteArrayOutputStream

class EditUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserBinding
    private lateinit var dbHelper: UserDatabaseHelper
    private var userIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDatabaseHelper.getInstance(this)

        userIndex = intent.getIntExtra("USER_INDEX", -1)

        if (userIndex != -1) {
            val user = dbHelper.getUserById(userIndex)
            if (user != null) {
                binding.editTextFirstName.setText(user.firstName)
                binding.editTextLastName.setText(user.lastName)
                binding.editTextPhoneNumber.setText(user.phoneNumber)
                binding.editTextEmailAddress.setText(user.emailAddress)
                binding.editTextHomeAddress.setText(user.homeAddress)
                if (user.photo.isNotEmpty()) {
                    val photoBitmap = BitmapFactory.decodeByteArray(user.photo, 0, user.photo.size)
                    binding.imageViewPhoto.setImageBitmap(photoBitmap)
                }
            }
        }

        binding.buttonSave.setOnClickListener {
            dbHelper.updateUser(
                userIndex,
                binding.editTextFirstName.text.toString(),
                binding.editTextLastName.text.toString(),
                binding.editTextPhoneNumber.text.toString(),
                binding.editTextEmailAddress.text.toString(),
                binding.editTextHomeAddress.text.toString(),
                getPhotoByteArray(binding.imageViewPhoto.drawable)
            )

            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.buttonCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.imageViewPhoto.setOnClickListener {
            val pickPhotoIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhotoIntent, PICK_PHOTO_REQUEST_CODE)
        }
    }

    private fun getPhotoByteArray(drawable: Drawable?): ByteArray? {
        drawable ?: return null
        val bitmap = (drawable as BitmapDrawable).bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            binding.imageViewPhoto.setImageURI(selectedImageUri)
        }
    }

    companion object {
        const val PICK_PHOTO_REQUEST_CODE = 100
    }
}