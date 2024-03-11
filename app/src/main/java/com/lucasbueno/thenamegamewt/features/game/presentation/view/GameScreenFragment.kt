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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GameScreenFragment : Fragment(R.layout.fragment_game), PersonCardAdapter.OnItemClickListener {

    private val viewModel: GameScreenViewModel by viewModels()

    private var _binding: FragmentGameBinding? = null

    private val personCardAdapter: PersonCardAdapter = PersonCardAdapter(this)
    private val binding get() = _binding!!

    private var correctAnswerId: String? = null

    private var isCorrectAnswer: Boolean = false

    private var counter = 0

    private val args: GameScreenFragmentArgs by navArgs()

    private var gameOverDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameBinding.bind(view)
        setupToolbar()
        setupRecyclerView()
        setupGameMode(args.isPractice)
        initObservers()

        if (savedInstanceState == null) {
            viewModel.getGameData()
        }
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
//        TODO("Move Call to viewmodel and let it decide")
        val toolBarTitle = if (isPracticeMode) "Practice Mode" else "Timed Mode"
        viewModel.setGameMode(isPracticeMode = isPracticeMode)

        if (!isPracticeMode) {
            setupTimerMode()
        }

        binding.toolbar.title = toolBarTitle
    }

    private fun setupTimerMode() {
        binding.progressBar.visibility = View.VISIBLE
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
        handleList(state.data, state.correctAnswerId)
        updateTimer(state.progressState)
        updateCounter(state.counter)
    }

    private fun handleEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Error -> handleError(event.error)
            is UiEvent.ShowGameOverDialog -> showDialog()
            is UiEvent.ShowSuccessAction -> updateList()
        }
    }

    private fun updateCounter(num: Int) {
        counter = num
    }

    private fun updateTimer(progress: Int) {
        binding.progressBar.progress = progress
        viewModel.onVerifyTimer()
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
        correctAnswerObject: GameDataItem?
    ) {
        binding.personNameTv.text =
            correctAnswerObject?.firstName + " " + correctAnswerObject?.lastName

        correctAnswerId = correctAnswerObject?.id.toString()

        personCardAdapter.setCorrectAnswerId(correctAnswerId!!)

        personCardAdapter.submitList(data)
    }

    override fun onItemClick(position: Int, gameData: GameDataItem) {
        viewModel.onItemClicked(gameData, correctAnswerId)
        personCardAdapter.isCorrectAnswer(isCorrectAnswer)
//        updateUiForSelectedItem(position, isCorrectAnswer)

        Toast.makeText(context, "Clicked on ${gameData.firstName}", Toast.LENGTH_SHORT).show()
    }

//    private fun updateUiForSelectedItem(position: Int, isCorrectAnswer: Boolean) {
//        // Find the ViewHolder for the clicked position
//        val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(position)
//                as? PersonCardAdapter.PersonCardViewHolder
//
//        viewHolder?.let { holder ->
//            // Update the overlay frame background color
//            holder.binding.overlayFrame.apply {
//                visibility = View.VISIBLE
//                setBackgroundColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        if (isCorrectAnswer) {
//                            R.color.checkmark_green
//                        } else {
//                            R.color.checkmark_red
//                        }
//                    )
//                )
//            }
//
//            // Update the checkmark image
//            holder.binding.checkmarkImageView.setImageResource(
//                if (isCorrectAnswer) R.drawable.checkmark_ic else R.drawable.checkmark_error_ic
//            )
//
//            // Set a delay to hide the overlay frame
//            val handler = Handler(Looper.getMainLooper())
//            handler.postDelayed({
//                holder.binding.overlayFrame.visibility = View.GONE
//            }, 1000)
//        }
//    }

    private fun updateList() {
        viewModel.getGameData()
    }

    private fun showDialog() {
        if (gameOverDialog?.isShowing == true) {
            return
        }

        gameOverDialog = AlertDialog.Builder(requireContext())
            .setTitle("Game Over")
            .setMessage("You scored ${counter}/5.")
            .setPositiveButton("OK") { dialogInterface, _ ->
                dialogInterface.dismiss()
                findNavController().navigateUp()
            }
            .setOnDismissListener {
                gameOverDialog = null
            }
            .create()

        gameOverDialog?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}