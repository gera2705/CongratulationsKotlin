package com.kolosov.congratulations.ui.search

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kolosov.congratulations.R
import com.kolosov.congratulations.data.CongratulationDataBase
import com.kolosov.congratulations.databinding.SearchFragmentBinding

class SearchFragment : Fragment() {

    private val binding by viewBinding(SearchFragmentBinding::bind)

    private var nameList: List<String>? = null

    private val genderList: List<String> = listOf("Мужской", "Женский", "Не важно")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg: SearchFragmentArgs? by navArgs()
        binding.holidayNameEditView?.text = arg?.holidayName

        val db: CongratulationDataBase? =
            CongratulationDataBase.getDbInstance(this.requireContext())

        nameList = db?.congratulationDao()?.getAllNames()

        initListeners()
    }

    private fun initListeners() {

        binding.holidayNameEditView?.setOnClickListener {
            loadNameSpinner(nameList!!, binding.holidayNameEditView!!)
        }

        binding.genderEditText?.setOnClickListener {
            loadNameSpinner(genderList, binding.genderEditText!!)
        }

        binding.searchCongratulation?.setOnClickListener {
            if (!binding.holidayNameEditView?.text?.toString().equals("")
                && !binding.humanName?.text?.toString().equals("")
                && !binding.genderEditText?.text?.toString().equals("")
            ) {
                val action: SearchFragmentDirections.ActionSearchFragmentToSearchResultFragment =
                    SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(
                        binding.holidayNameEditView?.text.toString(),
                        binding.humanName?.text.toString()
                    )
                view?.findNavController()?.navigate(action)
            } else {
                Toast.makeText(requireContext(), "Не все поля заполены!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.logo?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadNameSpinner(namesList: List<String>, result: TextView) {

        val dialog = Dialog(this.requireContext())

        dialog.setContentView(R.layout.dialog_search_spinner)
        dialog.window?.setLayout(1000, 1200)
        dialog.show()

        val editText: EditText = dialog.findViewById(R.id.dialog_search_edit_text)

        val listView: ListView = dialog.findViewById(R.id.holidays_names_list_view)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.simple_list_item_1,
            namesList
        )

        listView.adapter = adapter

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        listView.onItemClickListener =
            OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                result.text = adapter.getItem(position)
                dialog.dismiss()
            }
    }
}
