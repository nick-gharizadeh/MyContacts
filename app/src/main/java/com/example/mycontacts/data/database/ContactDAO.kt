package com.example.mycontacts.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface ContactDAO {

    @Query("SELECT * from contact ORDER By fullName ASC ")
    fun getAllContacts(): LiveData<List<ContactDAO?>?>?

}