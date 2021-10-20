package com.kolosov.congratulations.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kolosov.congratulations.R
import com.kolosov.congratulations.data.CongratulationDataBase
import com.kolosov.congratulations.databinding.HomeFragmentBinding
import com.kolosov.congratulations.ui.utils.CustomDateFormat
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class HomeFragment : Fragment() {

    private val binding by viewBinding(HomeFragmentBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = CongratulationDataBase.getDbInstance(this.requireContext())

        val congratulationList = db?.congratulationDao()?.getAllCongratulationNameByDate(
            CustomDateFormat.getCurrentDate())

        initSliderView(congratulationList)

        initListeners()

    }

    private fun initListeners() {

        binding.homeAddButton?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }

        binding.homeSearchButton?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.smallButton1?.setOnClickListener {
           smallCardAction(getString(R.string.home_card_one_text))
        }

        binding.smallButton2?.setOnClickListener {
            smallCardAction(getString(R.string.home_card_two_text))
        }

        binding.smallButton3?.setOnClickListener {
            smallCardAction(getString(R.string.home_card_three_text))
        }

        binding.smallButton4?.setOnClickListener {
            smallCardAction(getString(R.string.home_card_four_text))
        }
    }

    private fun smallCardAction(text: String){
        val action: HomeFragmentDirections.ActionHomeFragmentToSearchFragment =
            HomeFragmentDirections.actionHomeFragmentToSearchFragment()
        action.holidayName = text
        findNavController().navigate(action)
    }

    private fun initSliderView(congratulationList : List<String>?){

        val date = CustomDateFormat.getTitleDate(requireContext())
        val bigDate = CustomDateFormat.getBigDate(requireContext())

        val sliderAdapter = SliderAdapter(congratulationList, date, bigDate, requireContext())
        val sliderView: SliderView? = binding.homeImageSlider

        sliderView?.setSliderAdapter(sliderAdapter)
        sliderView?.setIndicatorAnimation(IndicatorAnimationType.WORM)
        sliderView?.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
        sliderView?.startAutoCycle()
    }
}
