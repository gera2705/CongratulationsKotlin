package com.kolosov.congratulations.ui.add

import android.app.DatePickerDialog
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kolosov.congratulations.R
import com.kolosov.congratulations.data.CongratulationDataBase
import com.kolosov.congratulations.data.entitys.Congratulation
import com.kolosov.congratulations.databinding.AddFragmentBinding
import java.util.*

class AddFragment : Fragment() {

    private val binding by viewBinding(AddFragmentBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db: CongratulationDataBase? =
            CongratulationDataBase.getDbInstance(this.requireContext())

        initListeners(db)
    }

    private fun initListeners(db: CongratulationDataBase?) {

        binding.addCongratulationButton?.setOnClickListener {

            if (binding.holidayNameEditView?.text?.toString().equals("")
                || binding.addDescriptionEditText?.text?.toString().equals("")
                || binding.addCalendarSpinner?.text?.toString().equals("")) {
                Toast.makeText(this.requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show()
            } else {

                val dates: Array<String> =
                    binding.addCalendarSpinner?.text.toString().split("\\.".toRegex()).toTypedArray()

                val congratulation = Congratulation(
                    name = binding.holidayNameEditView?.text?.toString()!!,
                    description = binding.addDescriptionEditText?.text?.toString()!!,
                    date = dates[1] + "/" + dates[0] + "/" + dates[2].substring(2),
                    congratulationText = ""
                )

                try {
                    db?.congratulationDao()?.insertCongratulation(congratulation)
                    findNavController().navigate(R.id.action_addFragment_to_addResultFragment)
                } catch (e: SQLiteException) {
                    Toast.makeText(
                        this.requireContext(),
                        "Запись с таким название уже существует!\nИзмените название праздника.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.logo?.setOnClickListener {
            findNavController().popBackStack()
        }

        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        binding.addCalendarSpinner?.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                this.requireContext(), R.style.DialogTheme,
                { _: DatePicker?, year1: Int, month1: Int, dayOfMonth: Int ->
                    val date = "$dayOfMonth.${month1+1}.$year1"
                    binding.addCalendarSpinner?.text = date
                }, year, month, day
            )

            datePickerDialog.show()
        }
    }
}
