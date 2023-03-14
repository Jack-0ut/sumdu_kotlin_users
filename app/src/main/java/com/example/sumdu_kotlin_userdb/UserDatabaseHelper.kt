package com.example.sumdu_kotlin_userdb

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private var onDatabaseChangeListener: OnDatabaseChangeListener? = null
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDatabase"
        private const val TABLE_NAME = "Users"
        private const val KEY_FIRST_NAME = "FirstName"
        private const val KEY_LAST_NAME = "LastName"
        private const val KEY_PHONE_NUMBER = "PhoneNumber"
        private const val KEY_EMAIL_ADDRESS = "EmailAddress"
        private const val KEY_HOME_ADDRESS = "HomeAddress"
        private const val KEY_PHOTO = "Photo"

        private var instance: UserDatabaseHelper? = null

        fun getInstance(context: Context): UserDatabaseHelper {
            if (instance == null) {
                instance = UserDatabaseHelper(context)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTableSql = ("CREATE TABLE $TABLE_NAME ("
                + "ID INTEGER PRIMARY KEY,"
                + "$KEY_FIRST_NAME TEXT,"
                + "$KEY_LAST_NAME TEXT,"
                + "$KEY_PHONE_NUMBER TEXT,"
                + "$KEY_EMAIL_ADDRESS TEXT,"
                + "$KEY_HOME_ADDRESS TEXT,"
                + "$KEY_PHOTO BLOB" + ")")
        db.execSQL(createUserTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(
        context: Context, firstName: String, lastName: String, phoneNumber: String, emailAddress: String,
        homeAddress: String, photo: ByteArray?
    ): Long {
        val values = ContentValues()
        values.put(KEY_FIRST_NAME, firstName)
        values.put(KEY_LAST_NAME, lastName)
        values.put(KEY_PHONE_NUMBER, phoneNumber)
        values.put(KEY_EMAIL_ADDRESS, emailAddress)
        values.put(KEY_HOME_ADDRESS, homeAddress)
        values.put(KEY_PHOTO, photo)

        val db = this.writableDatabase
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }
    @SuppressLint("Range")
    fun getUserById(id: Int): User? {
        val db = this.readableDatabase

        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE ID = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(id.toString()))

        if (cursor.moveToFirst()) {
            val user = User(
                cursor.getInt(cursor.getColumnIndex("ID")),
                cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(KEY_EMAIL_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(KEY_HOME_ADDRESS)),
                cursor.getBlob(cursor.getColumnIndex(KEY_PHOTO))
            )
            cursor.close()
            db.close()
            return user
        } else {
            cursor.close()
            db.close()
            return null
        }
    }

    @SuppressLint("Range")
    fun getAllUsers(sortByOption:String): List<User> {
        val sortOrder = when (sortByOption) {
            "First Name" -> KEY_FIRST_NAME
            "Last Name" -> KEY_LAST_NAME
            "Email" -> KEY_EMAIL_ADDRESS
            else -> "ID"
        }
        val userList = mutableListOf<User>()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $sortOrder"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("ID"))
                val firstName = cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER))
                val emailAddress = cursor.getString(cursor.getColumnIndex(KEY_EMAIL_ADDRESS))
                val homeAddress = cursor.getString(cursor.getColumnIndex(KEY_HOME_ADDRESS))
                userList.add(User(id, firstName, lastName, phoneNumber, emailAddress, homeAddress))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

    // Clear the table "users"
    fun clearTable() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
    // Delete User with given index
    fun deleteUser(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "ID=?", arrayOf(id.toString()))
        db.close()
    }
    // update User
    fun updateUser(id: Int, firstName: String, lastName: String, phoneNumber: String, emailAddress: String, homeAddress: String, photo: ByteArray?) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_FIRST_NAME, firstName)
        values.put(KEY_LAST_NAME, lastName)
        values.put(KEY_PHONE_NUMBER, phoneNumber)
        values.put(KEY_EMAIL_ADDRESS, emailAddress)
        values.put(KEY_HOME_ADDRESS, homeAddress)
        values.put(KEY_PHOTO, photo)
        db.update(TABLE_NAME, values, "ID=?", arrayOf(id.toString()))
        db.close()
    }
    /*
    fun getUsersSortedByFirstName(): List<User> {
        return getAllUsers().sortedBy { it.firstName }
    }
    fun getUsersSortedByLastName(): List<User>{
        return getAllUsers().sortedBy { it.lastName }
    }
    fun getUsersSortedByEmailAddress():List<User>{
        return getAllUsers().sortedBy { it.phoneNumber }
    }
    */
    // Listeners to track the changes in the database
    fun setOnDatabaseChangeListener(listener: OnDatabaseChangeListener?) {
        onDatabaseChangeListener = listener
    }

    // Helper method to notify the listener when the database changes
    private fun notifyDatabaseChanged() {
        onDatabaseChangeListener?.onDatabaseChanged()
    }

    interface OnDatabaseChangeListener {
        fun onDatabaseChanged()
    }
}

