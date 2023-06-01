package com.example.mycontacts.service;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.IBinder;
import android.provider.ContactsContract;

import com.example.mycontacts.Utils;

import javax.annotation.Nullable;

public class ContactObserverService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyContactContentObserver extends ContentObserver {
        public MyContactContentObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            SharedPreferences sharedPreferences = getSharedPreferences("CONTACTS_CHANGE", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CONTACTS_CHANGE_STATE", Utils.ContactsChangeState.CONTACTS_HAVE_CHANGED.getState());
            editor.apply();

        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI,
                true, new MyContactContentObserver());
    }
}