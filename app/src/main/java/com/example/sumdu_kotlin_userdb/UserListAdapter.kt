package com.example.sumdu_kotlin_userdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
/**
 * This is adapter class that gets the data in form of the List<Users>
 * and transform that data in the user-friendly format to be shown to the User
 **/
class UserListAdapter(private var userList: List<User>) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Get references to views in the layout
        val idTextView:TextView = itemView.findViewById(R.id.textViewID)
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val phoneTextView:TextView = itemView.findViewById(R.id.textViewPhone)
        val emailTextView:TextView = itemView.findViewById(R.id.textViewEmail)
        val addressTextView : TextView = itemView.findViewById(R.id.textViewAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // Inflate the item layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // Bind the user data to the views in the view holder
        val user = userList[position]
        holder.idTextView.text = user.id.toString()
        holder.nameTextView.text = "${user.firstName} ${user.lastName}"
        holder.phoneTextView.text = user.phoneNumber
        holder.emailTextView.text = user.emailAddress
        holder.addressTextView.text = user.homeAddress
    }
    fun updateData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return userList.size
    }
}
