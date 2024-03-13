package com.lucasbueno.thenamegamewt.features.game.presentation.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lucasbueno.thenamegamewt.R
import com.lucasbueno.thenamegamewt.databinding.FragmentGameBinding
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.features.game.presentation.view.adapter.GameScreenAdapter
import com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel.GameDataState
import com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel.GameScreenViewModel
import com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel.GameUiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GameScreenFragment : Fragment(R.layout.fragment_game) {

    private val viewModel: GameScreenViewModel by viewModels()

    private val adapter: GameScreenAdapter = GameScreenAdapter()

    private var _binding: FragmentGameBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameBinding.bind(view)
        initObservers()

        if (savedInstanceState == null) {
            viewModel.getData()
        }


    }

    private fun setupRecyclerView() {
        val columns =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3

        binding.recyclerView.layoutManager = GridLayoutManager(context, columns)
        binding.recyclerView.adapter = adapter

        adapter.itemClicked { gameDataItem, pos ->
            onClick(gameDataItem, pos)
        }
    }

    private fun onClick(gameDataItem: GameDataItem, position: Int) {

    }

    private fun initObservers() {
        viewModel.stateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)

        viewModel.eventFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleEvent(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: GameDataState) {
        handleList(state.gameDataList)
    }

    private fun handleEvent(event: GameUiEvent) {
        when (event) {
            is GameUiEvent.Error -> displayError(event.error)
        }
    }

    private fun handleList(data: List<GameDataItem>) {

    }

    private fun displayError(error: Throwable) {
        Snackbar.make(binding.root, error.message.orEmpty(), Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}