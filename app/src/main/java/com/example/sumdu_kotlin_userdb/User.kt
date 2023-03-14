package com.example.sumdu_kotlin_userdb

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val emailAddress: String,
    val homeAddress: String,
    val photo: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (phoneNumber != other.phoneNumber) return false
        if (emailAddress != other.emailAddress) return false
        if (homeAddress != other.homeAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + emailAddress.hashCode()
        result = 31 * result + homeAddress.hashCode()
        return result
    }
}