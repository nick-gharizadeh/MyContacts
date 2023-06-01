package com.example.mycontacts.ui.contactsfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontacts.R
import com.example.mycontacts.data.model.Contact

typealias ClickHandler = (Contact) -> Unit

class ContactsAdapter(private var clickHandler: ClickHandler) :
    ListAdapter<Contact, ContactsAdapter.ViewHolder>(ContactDiffCallback) {

    object ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {

        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewContactName: TextView = view.findViewById(R.id.textView_contact_name)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.contact_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = getItem(position)
        holder.textViewContactName.text = contact.fullName
        holder.itemView.setOnClickListener {
            clickHandler.invoke(contact)
        }
    }
}


