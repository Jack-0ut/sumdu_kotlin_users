package com.example.sumdu_kotlin_userdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sumdu_kotlin_userdb.databinding.FragmentUserListBinding

class UserListFragment : Fragment() {
    private lateinit var binding: FragmentUserListBinding
    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var adapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        dbHelper = UserDatabaseHelper.getInstance(requireContext())

        // here we set the data which should be shown
        adapter = UserListAdapter(dummyGetUser())
        binding.userRecyclerView.adapter = adapter
        binding.userRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }
    fun dummyGetUser(): List<User> {
        val userList = mutableListOf<User>()
        for (i in 1..5) {
            val user = User(
                id = i,
                firstName = "User $i",
                lastName = "Lastname $i",
                phoneNumber = "123-456-789$i",
                emailAddress = "user$i@example.com",
                homeAddress = "1234 Main St, Anytown, USA",
                photo = ByteArray(0) // Empty byte array for now
            )
            userList.add(user)
        }
        return userList
    }

}

