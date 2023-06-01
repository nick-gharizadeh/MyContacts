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
import com.example.mycontacts.Utils
import com.example.mycontacts.data.model.Contact
import com.example.mycontacts.data.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val applicationContext: Application
) : AndroidViewModel(applicationContext) {

    var contactsList: LiveData<List<Contact?>?>?
    private val sharedPreferences: SharedPreferences =
        applicationContext.getSharedPreferences("CONTACTS_CHANGE", Context.MODE_PRIVATE)

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


    fun fetchContacts() {
        if (getChangeStateOfContacts() == Utils.ContactsChangeState.CONTACTS_HAVE_CHANGED.state) {
            deleteAllContacts()
            val contacts = getContacts(applicationContext.contentResolver)
            insertContacts(contacts)
            setChangeStateOfContacts(Utils.ContactsChangeState.CONTACTS_HAVE_NOT_CHANGED.state)
        }
    }

    private fun insertContacts(contacts: List<Contact>) {
        viewModelScope.launch {
            for (contact in contacts) {
                contactsRepository.insertContact(contact)
            }
        }
    }

    private fun deleteAllContacts() {
        viewModelScope.launch {
            contactsRepository.deleteAllContacts()
        }
    }


    private fun getChangeStateOfContacts(): String? {
        return sharedPreferences.getString(
            "CONTACTS_CHANGE_STATE",
            Utils.ContactsChangeState.CONTACTS_HAVE_CHANGED.state
        )
    }

    private fun setChangeStateOfContacts(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("CONTACTS_CHANGE_STATE", state)
        editor.apply()
        super.onCleared()
    }

    override fun onCleared() {
        setChangeStateOfContacts(Utils.ContactsChangeState.CONTACTS_HAVE_NOT_CHANGED.state)
    }
}