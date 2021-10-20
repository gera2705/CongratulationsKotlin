package com.kolosov.congratulations.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kolosov.congratulations.R
import com.kolosov.congratulations.data.CongratulationDataBase
import com.kolosov.congratulations.databinding.FavouritesFragmentBinding

class FavouritesFragment : Fragment() {

    private val binding by viewBinding(FavouritesFragmentBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourites_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db: CongratulationDataBase? =
            CongratulationDataBase.getDbInstance(this.requireContext())

        loadData(db)

        initListeners()

    }

    private fun initListeners() {

        binding.favoriteToHomeButton?.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.favoriteToSearchButton?.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        binding.logo?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadData(db: CongratulationDataBase?) {

        val favoriteList = db?.congratulationDao()?.getAllFavorites()

        if(favoriteList != null && favoriteList.isNotEmpty()) {
            binding.stylusButton?.visibility = View.INVISIBLE
            binding.favoriteSubtitle?.visibility = View.INVISIBLE
            binding.favoriteToSearchButton?.visibility = View.INVISIBLE
            binding.favoriteToHomeButton?.visibility = View.INVISIBLE
            binding.favoriteRecycleView?.visibility = View.VISIBLE

            val recyclerView: RecyclerView? = binding.favoriteRecycleView
            recyclerView?.setHasFixedSize(true)
            val layoutManager: RecyclerView.LayoutManager =
                LinearLayoutManager(this.requireContext())
            val adapter = FavoriteRecyclerAdapter(
                favoriteList,
                this.requireContext()
            )

            recyclerView?.layoutManager = layoutManager
            recyclerView?.adapter = adapter
        }
    }
}