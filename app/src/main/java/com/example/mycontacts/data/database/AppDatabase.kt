package com.example.mycontacts.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mycontacts.data.model.Contact


@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDAO?

    companion object {
        const val DATABASE_NAME: String = "MyContactsDB"
    }
}