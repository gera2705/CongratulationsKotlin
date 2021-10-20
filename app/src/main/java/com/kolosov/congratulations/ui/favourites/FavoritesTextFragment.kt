package com.kolosov.congratulations.ui.favourites

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kolosov.congratulations.R
import com.kolosov.congratulations.data.CongratulationDataBase
import com.kolosov.congratulations.data.entitys.Favorites
import com.kolosov.congratulations.databinding.FavoritesTextFragmentBinding

class FavoritesTextFragment : Fragment() {

    private val binding by viewBinding(FavoritesTextFragmentBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_text_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db: CongratulationDataBase? =
            CongratulationDataBase.getDbInstance(this.requireContext())

        val arg: FavoritesTextFragmentArgs by navArgs()
        binding.congratulationText?.text = arg.favoritesText

        initListeners(db, arg.favoritesText)
    }

    private fun initListeners(db: CongratulationDataBase?, text: String) {

        binding.favoriteButton?.setOnClickListener {
            db?.congratulationDao()?.deleteFavorites(Favorites(text))
            findNavController().popBackStack()
            Toast.makeText(requireContext(), "Удалено", Toast.LENGTH_SHORT).show()
        }

        binding.backButton?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonShare?.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, binding.congratulationText?.text.toString())
            startActivity(Intent.createChooser(sharingIntent, "Поделиться поздравлением"))
        }

        binding.buttonCopy?.setOnClickListener {

            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
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
    }



}