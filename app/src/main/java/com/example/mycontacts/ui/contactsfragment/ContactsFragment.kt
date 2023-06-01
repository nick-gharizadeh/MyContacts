package com.example.mycontacts.ui.contactsfragment

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
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
        val sharedPreferences =
            requireActivity().getSharedPreferences("CONTACTS_CHANGE", Context.MODE_PRIVATE);
        val didContactsChange = sharedPreferences.getBoolean("DID_CONTACTS_CHANGE", false);
        Toast.makeText(requireContext(), "$didContactsChange", Toast.LENGTH_SHORT).show()

    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val contacts = contactsViewModel.getContacts(requireContext().contentResolver)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied! Please grant the permission ... :)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onDestroy() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("CONTACTS_CHANGE", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("DID_CONTACTS_CHANGE", false)
        editor.apply()
        super.onDestroy()

    }
}