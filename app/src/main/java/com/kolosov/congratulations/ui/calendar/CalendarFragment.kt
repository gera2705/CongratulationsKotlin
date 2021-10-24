package com.kolosov.congratulations.ui.calendar

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kolosov.congratulations.R
import com.kolosov.congratulations.data.CongratulationDataBase
import com.kolosov.congratulations.data.entitys.Congratulation
import com.kolosov.congratulations.databinding.CalendarFragmentBinding
import com.kolosov.congratulations.ui.utils.CustomDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private val binding by viewBinding(CalendarFragmentBinding::bind)

    private var currentDatesNumber = 1

    private var description: ArrayList<String>? = ArrayList()

    private var congratulationList: ArrayList<Congratulation>? = ArrayList()

    private var date: String? = null

    private var db: CongratulationDataBase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = CongratulationDataBase.getDbInstance(this.requireContext())

        getNewList(CustomDateFormat.getCurrentDate())

        getDescriptions()

        setResultText(congratulationList!! , description!!)

        setCounter()

        initListeners(congratulationList!!)

        initPagesButtons()

        initCalendarListener()
    }

    private fun getDescriptions(){

        for (congratulation in congratulationList!!) {
            description!!.add(congratulation.description)
        }
    }

    private fun getNewList(date: String?){
        congratulationList = db?.congratulationDao()?.getAllCongratulationByDate(date) as ArrayList<Congratulation>?
    }

    private fun initPagesButtons(){

        binding.forwardDescriptionArrowImageButton?.setOnClickListener {

            if (currentDatesNumber != description!!.size) {
               currentDatesNumber++
            }
            setResultText(congratulationList!!, description!!)
            setCounter()
        }

        binding.backDescriptionArrowImageButton?.setOnClickListener {

            if (currentDatesNumber > 1) {
                currentDatesNumber--
            }
            setResultText(congratulationList!!, description!!)

            setCounter()
        }
    }

    private fun initCalendarListener(){

        binding.calendar?.setOnDateChangeListener { view, year, month, dayOfMonth ->

            date = (month + 1).toString() + "/" + dayOfMonth + "/" + year.toString().substring(2)
            currentDatesNumber = 1

            congratulationList?.clear()
            description?.clear()

            getNewList(date)

            getDescriptions()

            setResultText(congratulationList!!, description!!)

            setCounter()

            initPagesButtons()

        }
    }

    private fun setCounter(){
        binding.resultCounter?.text = getString(
            R.string.counter_text,
            currentDatesNumber,
            description!!.size
        )
    }

    private fun setResultText(
        congratulationsOnDate: List<Congratulation>,
        description: List<String>
    ) {
        val dates: Array<String> = congratulationsOnDate[currentDatesNumber-1].date.split("/").toTypedArray()
        val date =dates[1] + "." + dates[0] + "." + dates[2]

        val sizeStartIndex: Int =
            congratulationsOnDate[currentDatesNumber - 1].date.length
        val sizeEndIndex: Int =
            congratulationsOnDate[currentDatesNumber - 1].name.length + 1 + congratulationsOnDate[currentDatesNumber - 1].date.length
        val colorEndIndex: Int =
            congratulationsOnDate[currentDatesNumber - 1].date.length

        val ss = SpannableString(
            "$date\n${congratulationsOnDate[currentDatesNumber - 1].name}\n${description[currentDatesNumber - 1]}"
        )
        ss.setSpan(
            RelativeSizeSpan(1.5f),
            sizeStartIndex,
            sizeEndIndex,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        ) // устанавливаем размер

        ss.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.primary_2)),
            0,
            colorEndIndex,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        ) // устанавливаем цвет

        binding.calendarResultText?.text = ss
    }

    private fun initListeners(congratulationsOnDate: List<Congratulation>) {
        binding.logo?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchButtonOnCalendar?.setOnClickListener {
            val action : CalendarFragmentDirections.ActionCalendarFragmentToSearchFragment =
                CalendarFragmentDirections.actionCalendarFragmentToSearchFragment()
            action.holidayName = congratulationsOnDate[currentDatesNumber-1].name

            findNavController().navigate(action)
        }
    }
}
