package com.example.mycontacts.ui.contactsfragment

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mycontacts.data.model.Contact
import com.example.mycontacts.data.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactsViewModel @Inject constructor(
    val contactsRepository: ContactsRepository,
    application: Application
) : AndroidViewModel(application) {

    var contactsList: LiveData<List<Contact?>?>?
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("CONTACTS_CHANGE", Context.MODE_PRIVATE)

    init {
        contactsList = contactsRepository.getAllContacts()
    }

    @SuppressLint("Range")
    fun getContacts(contentResolver: ContentResolver): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            "DISPLAY_NAME ASC"
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                if (hasPhoneNumber > 0) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id),
                        null
                    )
                    if (phoneCursor != null && phoneCursor.moveToFirst()) {
                        val phoneNumber =
                            phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        contacts.add(Contact(id.toInt(), name, phoneNumber))
                        phoneCursor.close()
                    }
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
        return contacts
    }


    fun insertContacts(contacts: List<Contact>) {
        viewModelScope.launch {
            for (contact in contacts) {
                contactsRepository.insertContact(contact)
            }
        }
    }


    fun getChangeStateOfContacts(): Boolean {
        return sharedPreferences.getBoolean("DID_CONTACTS_CHANGE", true)
    }

    override fun onCleared() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("DID_CONTACTS_CHANGE", false)
        editor.apply()
        super.onCleared()
    }
}