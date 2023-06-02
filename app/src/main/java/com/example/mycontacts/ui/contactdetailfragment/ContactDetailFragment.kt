package com.example.mycontacts.ui.contactdetailfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.mycontacts.databinding.FragmentContactDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactDetailFragment : Fragment() {

    private lateinit var binding: FragmentContactDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ContactDetailFragmentArgs by navArgs()
        val contactSafeArg = args.contactSafeArg
        binding.textViewContactNameDetail.text = contactSafeArg.fullName
        binding.textViewPhoneNumber.text = contactSafeArg.phoneNumber
        binding.myCircleTextView.setText(contactSafeArg.fullName[0])
    }
}