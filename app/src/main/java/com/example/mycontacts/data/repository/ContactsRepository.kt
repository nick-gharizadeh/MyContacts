package com.example.mycontacts.data.repository

import androidx.lifecycle.LiveData
import com.example.mycontacts.data.database.AppDatabase
import com.example.mycontacts.data.model.Contact
import javax.inject.Inject

class ContactsRepository @Inject constructor(private val database: AppDatabase) {
    private val allContacts: LiveData<List<Contact?>?>?


    init {
        allContacts = database.contactDao()?.getAllContacts()
    }

    fun getAllContacts(): LiveData<List<Contact?>?>? {
        return allContacts
    }

}