package com.lucasbueno.thenamegamewt.features.game.presentation.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lucasbueno.thenamegamewt.R
import com.lucasbueno.thenamegamewt.databinding.FragmentGameBinding
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.features.game.presentation.view.adapter.PersonCardAdapter
import com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel.GameScreenState
import com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel.GameScreenViewModel
import com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GameScreenFragment : Fragment(R.layout.fragment_game), PersonCardAdapter.OnItemClickListener {

    private val viewModel: GameScreenViewModel by viewModels<GameScreenViewModel>()

    private var _binding: FragmentGameBinding? = null

    private val personCardAdapter: PersonCardAdapter = PersonCardAdapter(this)

    private val binding get() = _binding!!

    private var correctAnswerId: String? = null

    private var counter = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameBinding.bind(view)
        setupToolbar()
        setupRecyclerView()
        initObservers()
        viewModel.getGameData()
    }

    private fun setupToolbar() {
        val appCompatActivity = requireActivity() as? AppCompatActivity
        appCompatActivity?.let {
            it.setSupportActionBar(binding.toolbar)
            it.supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = personCardAdapter
    }

    private fun initObservers() {
        viewModel.stateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)

        viewModel.eventFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleEvent(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: GameScreenState) {
        when (state) {
            is GameScreenState.Success -> handleList(
                state.data,
                state.correctAnswerId,
                state.toolBarTitle
            )

            is GameScreenState.Empty -> Unit
            else -> {}
        }
    }

    private fun handleEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Error -> handleError(event.error)
            is UiEvent.ShowGameOVerDialog -> showDialog()
            is UiEvent.ShowSnackBar -> showSnackBar()
            else -> {}
        }
    }

    private fun handleError(error: Throwable) {
        Snackbar.make(
            binding.root,
            error.message.orEmpty(),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun handleList(
        data: List<GameDataItem>,
        correctAnswerObject: GameDataItem,
        toolBarTitle: String
    ) {

        binding.toolbar.title = toolBarTitle

        binding.personNameTv.text =
            correctAnswerObject.firstName + " " + correctAnswerObject.lastName

        correctAnswerId = correctAnswerObject.id.toString()

        personCardAdapter.setCorrectAnswerId(correctAnswerId!!)

        personCardAdapter.submitList(data)
    }

    override fun onItemClick(position: Int, gameData: GameDataItem) {
        viewModel.onItemClicked(gameData, correctAnswerId)

        Toast.makeText(context, "Clicked on ${gameData.firstName}", Toast.LENGTH_SHORT).show()
    }

    private fun showSnackBar() {
        viewModel.getGameData()

        Snackbar.make(
            binding.root,
            "Correct!",
            Snackbar.LENGTH_SHORT
        ).show()

        counter += 1
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Game Over")
            .setMessage("You scored ${counter}/5.")
            .setPositiveButton("OK") { _, _ ->
                findNavController().navigateUp()
            }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}