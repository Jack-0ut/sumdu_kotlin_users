package com.example.sumdu_kotlin_userdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sumdu_kotlin_userdb.databinding.FragmentUserListBinding

class UserListFragment(private var sortOption: String) : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var adapter: UserListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        dbHelper = UserDatabaseHelper.getInstance(requireContext())

        adapter = UserListAdapter(dbHelper.getAllUsers(sortOption))
        binding.userRecyclerView.adapter = adapter
        binding.userRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    fun sortUsers(sortOrder: String) {
        adapter.updateData(dbHelper.getAllUsers(sortOrder))
    }
}
