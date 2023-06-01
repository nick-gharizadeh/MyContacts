package com.example.mycontacts.ui.contactsfragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mycontacts.data.model.Contact
import com.example.mycontacts.databinding.FragmentContactsBinding
import com.example.mycontacts.service.ContactObserverService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        val contactsAdapter = ContactsAdapter {
            goToContactDetailFragment(it)
        }
        binding.contactsRecyclerView.adapter = contactsAdapter

        contactsViewModel.contactsList?.observe(viewLifecycleOwner) {
            if (it != null)
                contactsAdapter.submitList(it)
        }


    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                requireActivity().startService(
                    Intent(
                        requireActivity(),
                        ContactObserverService::class.java
                    )
                )
                if (contactsViewModel.getChangeStateOfContacts()) {
                    contactsViewModel.deleteAllContacts()
                    val contacts = contactsViewModel.getContacts(requireContext().contentResolver)
                    contactsViewModel.insertContacts(contacts)
                    contactsViewModel.setChangeStateOfContacts(false)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied! Please grant the permission ... :)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun goToContactDetailFragment(contact: Contact) {
        val action =
            ContactsFragmentDirections.actionContactsFragmentToContactDetailFragment(contact)
        findNavController().navigate(action)

    }


}