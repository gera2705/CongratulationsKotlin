package com.kolosov.congratulations.ui.add

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kolosov.congratulations.R
import com.kolosov.congratulations.databinding.AddResultFragmentBinding

class AddResultFragment : Fragment() {

    private val binding by viewBinding(AddResultFragmentBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {

        binding.addResultAddMoreButton?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addResultToMainButton?.setOnClickListener {
            findNavController().navigate(R.id.action_addResultFragment_to_homeFragment)
        }


    }
}
