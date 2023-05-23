package com.cryptoexchange.mobile.presentation.trading.myorders

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.cryptoexchange.mobile.databinding.DialogCloseOrderBinding
import com.cryptoexchange.mobile.extensions.tryToGetInt

class CloseOrderDialogFragment : DialogFragment() {

    private lateinit var clickListener: OnClickListener
    private var position = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        position = tryToGetInt(KEY_ORDER_POSITION)
        clickListener = requireParentFragment() as? OnClickListener
            ?: throw IllegalArgumentException(
                "${requireParentFragment()::class.simpleName} should implement " +
                    "CloseOrderDialogFragment.OnClickListener"
            )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogCloseOrderBinding.inflate(layoutInflater, null, false)

        with(binding) {
            closeDialogButton.setOnClickListener { dismiss() }
            dialogNegativeButton.setOnClickListener { dismiss() }
            dialogPositiveButton.setOnClickListener {
                clickListener.suggest(position)
                dismiss()
            }
        }

        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        return dialog
    }

    companion object {
        private const val KEY_ORDER_POSITION = "KEY_ORDER_POSITION"

        fun getInstance(position: Int) =
            CloseOrderDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_ORDER_POSITION, position)
                }
            }
    }

    interface OnClickListener {

        fun suggest(position: Int)
    }
}