package com.kolosov.congratulations.ui.search

import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.gson.Gson
import com.kolosov.congratulations.R
import com.kolosov.congratulations.data.CongratulationDataBase
import com.kolosov.congratulations.data.entitys.Favorites
import com.kolosov.congratulations.databinding.SearchResultFragmentBinding
import com.kolosov.congratulations.ui.favourites.FavouritesFragment

class SearchResultFragment : Fragment() {

    private val binding by viewBinding(SearchResultFragmentBinding::bind)

    private var currentCongratulationNumber: Int = 1

    private var congratulationsArray: List<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db: CongratulationDataBase? =
            CongratulationDataBase.getDbInstance(this.requireContext())


        init(db)

        initListeners(db)
    }

    private fun init(db: CongratulationDataBase?) {

        val arg: SearchResultFragmentArgs by navArgs()
        val holidayName = arg.holidayName
        val humanName = arg.humanName

        val congratulations = db?.congratulationDao()?.getCongratulation(holidayName)

        congratulationsArray = congratulations?.congratulationText?.split("\\|".toRegex())

        // Ультра костыль для демонстрации, позже исправлю :)
        if ( congratulationsArray?.get(currentCongratulationNumber - 1) == "К сожелению поздравления на этот праздник еще не добавленны :(") {
            currentCongratulationNumber = 0
            binding.smallCountTextView?.text =
                getString(R.string.search_res_small_counter_text, 0)
            binding.resultCounter?.text = getString(
                R.string.counter_text,
                currentCongratulationNumber,
                0
            )
            binding.congratulationText?.text = "Тут пока пусто :("
        // Ультра костыль для демонстрации, позже исправлю :)
        } else {

            currentCongratulationNumber = 1

            binding.smallCountTextView?.text =
                getString(R.string.search_res_small_counter_text, congratulationsArray?.size)

            binding.congratulationText?.text = getString(
                R.string.search_res_congratulation_text,
                humanName,
                congratulationsArray?.get(currentCongratulationNumber - 1)
            )

            binding.resultCounter?.text = getString(
                R.string.counter_text,
                currentCongratulationNumber,
                congratulationsArray?.size
            )

            binding.congratulationText?.movementMethod = ScrollingMovementMethod()

            binding.forwardArrowImageButton?.setOnClickListener {

                if (currentCongratulationNumber != congratulationsArray?.size) {
                    currentCongratulationNumber++
                }

                binding.congratulationText?.text = getString(
                    R.string.search_res_congratulation_text,
                    humanName,
                    congratulationsArray?.get(currentCongratulationNumber - 1)
                )

                binding.resultCounter?.text = getString(
                    R.string.counter_text,
                    currentCongratulationNumber,
                    congratulationsArray?.size
                )
            }

            binding.backArrowImageButton?.setOnClickListener {

                if (currentCongratulationNumber > 1) {
                    currentCongratulationNumber--
                }

                binding.congratulationText?.text = getString(
                    R.string.search_res_congratulation_text,
                    humanName,
                    congratulationsArray?.get(currentCongratulationNumber - 1)
                )

                binding.resultCounter?.text = getString(
                    R.string.counter_text,
                    currentCongratulationNumber,
                    congratulationsArray?.size
                )

            }
        }
    }

    private fun initListeners(db: CongratulationDataBase?) {

        binding.buttonShare?.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, binding.congratulationText?.text.toString())
            startActivity(Intent.createChooser(sharingIntent, "Поделиться поздравлением"))
        }

        binding.favoriteButton?.setOnClickListener {
            try {
                db?.congratulationDao()?.insertFavorites(
                    Favorites(
                        congratulationsArray?.get(currentCongratulationNumber - 1)!!
                    )
                )
                Toast.makeText(
                    requireContext(),
                    "Поздраление добавлено в избранное!",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: SQLiteException) {
                Toast.makeText(requireContext(), "Поздраление уже добавлено!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.buttonCopy?.setOnClickListener {

            val clipboard = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(
                "your_text_to_be_copied",
                binding.congratulationText?.text.toString()
            )
            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                requireContext(),
                "Поздравление скопировано",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.backButton?.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
