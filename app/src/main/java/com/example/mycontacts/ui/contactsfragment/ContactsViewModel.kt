package com.example.mycontacts.ui.contactsfragment

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycontacts.data.model.Contact

class ContactsViewModel : ViewModel() {

    val contactsList = MutableLiveData<List<Contact>?>()


    @SuppressLint("Range")
    fun getContacts(contentResolver: ContentResolver) {
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
                        do {
                            val phoneNumber =
                                phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contacts.add(Contact(id.toInt(), name, phoneNumber))
                        } while (phoneCursor.moveToNext())
                        phoneCursor.close()
                    }
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
        contactsList.value = contacts
    }

}