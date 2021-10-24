package com.kolosov.congratulations.ui.add

import android.app.*
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.kolosov.congratulations.R
import com.kolosov.congratulations.data.CongratulationDataBase
import com.kolosov.congratulations.data.entitys.Congratulation
import com.kolosov.congratulations.databinding.AddFragmentBinding
import com.kolosov.congratulations.ui.utils.YourHolidayAlarmReceiver
import java.util.*

class AddFragment : Fragment() {

    private val binding by viewBinding(AddFragmentBinding::bind)

    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarm: AlarmManager
    private lateinit var pendingIntent: PendingIntent

   // var title: String = ""

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

        createNotifyChanel()

        initListeners(db)
    }

    private fun initListeners(db: CongratulationDataBase?) {

        binding.addCongratulationButton?.setOnClickListener {

            if (binding.holidayNameEditView?.text?.toString().equals("")
                || binding.addDescriptionEditText?.text?.toString().equals("")
                || binding.addCalendarSpinner?.text?.toString().equals("")
            ) {
                Toast.makeText(this.requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT)
                    .show()
            } else {

                //title = binding.holidayNameEditView?.text?.toString()!!

                if (binding.switchButton?.isChecked == true) {
                    setAlarm()
                }

                val dates: Array<String> =
                    binding.addCalendarSpinner?.text.toString().split("\\.".toRegex())
                        .toTypedArray()

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
                    val date = "$dayOfMonth.${month1 + 1}.$year1"
                    binding.addCalendarSpinner?.text = date
                }, year, month, day
            )

            datePickerDialog.show()
        }


        binding.switchButton?.setOnCheckedChangeListener { _, b ->
            if(b) {
                showTimePicker()
            }
        }

    }

    private fun setAlarm() {
        alarm =
            this.requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this.requireContext(), YourHolidayAlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this.requireActivity(), 0, intent, 0)

        alarm.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }

    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(10)
            .setMinute(0)
            .setTitleText("Выберите время уведомления")
            .build()

        picker.show(this.requireActivity().supportFragmentManager, "your_holiday")

        picker.addOnPositiveButtonClickListener {

            calendar = Calendar.getInstance()
            calendar.set(2021, 9, 24, picker.hour, picker.minute)

        }

        picker.addOnNegativeButtonClickListener {
            binding.switchButton?.isChecked = false
        }

        picker.addOnCancelListener {
            binding.switchButton?.isChecked = false
        }
    }

    private fun createNotifyChanel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name: CharSequence = "youNotifyChannel"
            val description = "you_holiday_notify"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("your_holiday", name, importance)
            channel.description = description
            val manager = this.requireActivity().getSystemService(
                NotificationManager::class.java
            )

            manager.createNotificationChannel(channel)
        }
    }
}
