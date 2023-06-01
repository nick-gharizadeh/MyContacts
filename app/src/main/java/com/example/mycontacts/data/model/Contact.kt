package com.example.mycontacts.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Contact(
    @PrimaryKey val id: Int,
    @ColumnInfo val fullName: String,
    @ColumnInfo val phoneNumber: String,
) : Serializable

