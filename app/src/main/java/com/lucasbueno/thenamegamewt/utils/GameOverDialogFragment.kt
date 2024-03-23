package com.lucasbueno.thenamegamewt.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.lucasbueno.thenamegamewt.R

class GameOverDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_COUNTER = "counter"
        private const val ARG_LIST_LENGTH = "listLength"

        fun newInstance(counter: Int, listLength: Int): GameOverDialogFragment {
            val args = Bundle().apply {
                putInt(ARG_COUNTER, counter)
                putInt(ARG_LIST_LENGTH, listLength)
            }
            return GameOverDialogFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val counter = arguments?.getInt(ARG_COUNTER) ?: 0
        val listLength = arguments?.getInt(ARG_LIST_LENGTH) ?: 0
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.game_over))
            .setMessage(getString(R.string.you_scored, counter, listLength))
            .setPositiveButton(getString(R.string.confirm_button_text)) { _, _ ->
                // Handle positive button click here
                dismiss()
                findNavController().navigateUp()
            }
            .create()
    }
}
