package com.example.mycontacts.ui.contactsfragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mycontacts.databinding.FragmentContactsBinding


class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding
    private val contactsViewModel: ContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission.launch(Manifest.permission.READ_CONTACTS)
        val contactsAdapter = ContactsAdapter()
        binding.contactsRecyclerView.adapter = contactsAdapter
        contactsViewModel.contactsList.observe(viewLifecycleOwner) {
            contactsAdapter.submitList(it)
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val contacts = contactsViewModel.getContacts(requireContext().contentResolver)
                Toast.makeText(requireContext(), "$contacts", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied! Please grant the permission ... :)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

}