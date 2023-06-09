package com.example.mycontacts.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mycontacts.data.model.Contact

@Dao
interface ContactDAO {

    @Query("SELECT * from contact ORDER By fullName ASC ")
    fun getAllContacts(): LiveData<List<Contact?>?>?

    @Query("DELETE FROM contact")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact?)

}