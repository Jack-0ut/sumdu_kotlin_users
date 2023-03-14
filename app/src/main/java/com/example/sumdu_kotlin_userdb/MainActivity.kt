package com.example.sumdu_kotlin_userdb

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sumdu_kotlin_userdb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: UserDatabaseHelper
    private var currentSortOrder = "ID"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = UserDatabaseHelper.getInstance(this)

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddUser::class.java)
            startActivity(intent)

        }

        // delete from Users table with given index
        binding.buttonDelete.setOnClickListener {
            if(binding.editTextId.text.isDigitsOnly()){
                db.deleteUser(binding.editTextId.text.toString().toInt())
                Toast.makeText(this,"The User has been deleted!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Index is only digits value",Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonClear.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to clear the database?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    // User clicked on Yes button
                    db.clearTable()
                    Toast.makeText(this, "Database cleared successfully", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    // User clicked on No button, so we will dismiss the dialog and do nothing
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        binding.buttonEdit.setOnClickListener {
            if(binding.editTextId.text.isDigitsOnly() || binding.editTextId.text.isNotBlank()){
                val intent = Intent(this, EditUserActivity::class.java)
                intent.putExtra("USER_INDEX", binding.editTextId.text.toString().toInt())
                startActivity(intent)
            }else{
                Toast.makeText(this,"Index is only digits value",Toast.LENGTH_SHORT).show()
            }

        }
        binding.buttonShowList.setOnClickListener {
            val userListFragment = UserListFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, userListFragment)
                .commit()
        }

    }
    // do refresh the list, when the spinner option is choosen
    private fun refreshUserList() {
        val userList = db.getAllUsers()
        val userRecyclerView = findViewById<RecyclerView>(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        val userListAdapter = UserListAdapter(userList)
        userRecyclerView.adapter = userListAdapter
    }
}
