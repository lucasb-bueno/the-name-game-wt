package com.lucasbueno.thenamegamewt.features.game.presentation.view

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lucasbueno.thenamegamewt.R
import com.lucasbueno.thenamegamewt.databinding.FragmentGameBinding
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.features.game.presentation.view.adapter.PersonCardAdapter
import com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel.GameScreenState
import com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel.GameScreenViewModel
import com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel.UiEvent
import com.lucasbueno.thenamegamewt.utils.GameOverDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val HANDLER_DELAY = 500L

@AndroidEntryPoint
class GameScreenFragment : Fragment(R.layout.fragment_game), PersonCardAdapter.OnItemClickListener {

    private val viewModel: GameScreenViewModel by viewModels()

    private var _binding: FragmentGameBinding? = null

    private val personCardAdapter: PersonCardAdapter = PersonCardAdapter(this)
    private val binding get() = _binding!!

    private val args: GameScreenFragmentArgs by navArgs()

    private var correctAnswerId: String? = null

    private var isCorrectAnswer: Boolean = false

    private var counter = 0

    private var listLength = 0

    private var gameOverDialog: GameOverDialogFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameBinding.bind(view)
        setupToolbar()
        setupRecyclerView()
        if (savedInstanceState == null) {
            viewModel.getGameData()
            setupGameMode(args.isPractice)
        }
        initObservers()
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
        val columns =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3

        binding.recyclerView.layoutManager = GridLayoutManager(context, columns)
        binding.recyclerView.adapter = personCardAdapter
    }

    private fun setupGameMode(isPracticeMode: Boolean) {
        viewModel.setGameMode(isPracticeMode)

        if (!isPracticeMode) {
            setupTimerMode()
        }
    }

    private fun setupTimerMode() {
        viewModel.startTimer()
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
        isCorrectAnswer = state.isAnswerCorrectState
        handleList(state.data, state.correctAnswerItem)
        updateTimer(state.progressState, state.isTimerVisible)
        updateCounter(state.counter)
        updateUi(state.isPracticeMode)
    }

    private fun handleEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Error -> handleError(event.error)
            is UiEvent.ShowGameOverDialog -> showDialog()
            is UiEvent.ShowSuccessAction -> updateList()
            is UiEvent.SetTimerMode -> setupTimerMode()
        }
    }

    private fun handleError(error: Throwable) {
        Snackbar.make(
            binding.root,
            error.message.orEmpty(),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun updateUi(isPracticeMode: Boolean) {
        binding.toolbar.title =
            if (isPracticeMode) getString(R.string.practice_mode) else getString(R.string.timed_mode)
    }

    private fun handleList(
        data: List<GameDataItem>,
        correctAnswerItem: GameDataItem?
    ) {
        val fullName =
            "${correctAnswerItem?.firstName.orEmpty()} ${correctAnswerItem?.lastName.orEmpty()}".trim()
        binding.personNameTv.text = fullName

        correctAnswerId = correctAnswerItem?.id
        listLength = data.size

        personCardAdapter.submitList(data)
    }

    private fun updateCounter(num: Int) {
        counter = num
    }

    private fun updateTimer(progress: Int, isVisible: Boolean) {
        binding.progressBar.progress = progress
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
        viewModel.onVerifyTimer()
    }

    override fun onItemClick(position: Int, gameData: GameDataItem) {
        viewModel.onItemClicked(gameData, correctAnswerId)
        updateUiForSelectedItem(position, isCorrectAnswer)

        Toast.makeText(context, "Clicked on ${gameData.firstName}", Toast.LENGTH_SHORT).show()
    }

    private fun updateUiForSelectedItem(position: Int, isCorrectAnswer: Boolean) {
        val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(position)
                as? PersonCardAdapter.PersonCardViewHolder

        viewHolder?.let { holder ->
            holder.binding.overlayFrame.apply {
                visibility = View.VISIBLE
                setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (isCorrectAnswer) {
                            R.color.checkmark_green
                        } else {
                            R.color.checkmark_red
                        }
                    )
                )
            }

            holder.binding.checkmarkImageView.setImageResource(
                if (isCorrectAnswer) R.drawable.checkmark_ic else R.drawable.checkmark_error_ic
            )

            lifecycleScope.launch {
                delay(HANDLER_DELAY)
                holder.binding.overlayFrame.visibility = View.GONE
            }
        }
    }

    private fun updateList() {
        viewModel.getGameData()
    }

    private fun showDialog() {
        if (gameOverDialog != null) return

        gameOverDialog = GameOverDialogFragment.newInstance(counter, listLength)
        GameOverDialogFragment.newInstance(counter, listLength)
            .show(parentFragmentManager, "GameOverDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}