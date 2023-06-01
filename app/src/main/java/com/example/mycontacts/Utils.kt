package com.example.mycontacts

class Utils {
    enum class ContactsChangeState(val state: String) {
        CONTACTS_HAVE_CHANGED(App.resourses.getString(R.string.contacts_have_changed)),
        CONTACTS_HAVE_NOT_CHANGED(App.resourses.getString(R.string.contacts_have_not_changed)),

    }
}