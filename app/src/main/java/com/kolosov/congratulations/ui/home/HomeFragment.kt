package com.kolosov.congratulations.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kolosov.congratulations.MainActivity
import com.kolosov.congratulations.R
import com.kolosov.congratulations.data.CongratulationDataBase
import com.kolosov.congratulations.databinding.ActivityMainBinding
import com.kolosov.congratulations.databinding.HomeFragmentBinding
import com.kolosov.congratulations.ui.utils.CustomDateFormat
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig

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

        binding.questionButton?.setOnClickListener {
            val config = ShowcaseConfig()
            config.maskColor = Color.BLUE
            config.contentTextColor

            val sequence = MaterialShowcaseSequence(requireActivity())

            sequence.setConfig(config)

            sequence.addSequenceItem(
                binding.homeImageSlider,
                "Здесь находится информация о всех праздников сегодня!\nВы можете листать этот список вправо и влево!",
                "ПОНЯТНО"
            )

            sequence.addSequenceItem(
                binding.homeSearchButton,
                "Нажав на эту кнопку, Вы можете приступить к поиску поздравления!",
                "ПОНЯТНО"
            )

            sequence.addSequenceItem(
                binding.homeAddButton,
                "Нажав на эту кнопку, Вы можете добавить свою личную значимую дату!",
                "ПОНЯТНО"
            )

            sequence.addSequenceItem(
                binding.homeHorizontalScroll,
                "Тут вы можете воспользоваться быстрым поиском нажав на карточку с нужным названием! Вы можете прокручивать список карточек!",
                "ПОНЯТНО"
            )

            sequence.addSequenceItem(
                this.activity?.findViewById(R.id.menu_navigation),
                "Это меню приложения с помощью которого вы можете переключаться между разделами приложения!", "ПОЕХАЛИ!"
            )

            //sequence.singleUse("wallet_info")
            sequence.start()
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
