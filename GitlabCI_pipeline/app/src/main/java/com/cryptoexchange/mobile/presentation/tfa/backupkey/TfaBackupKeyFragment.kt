package com.cryptoexchange.mobile.presentation.tfa.backupkey

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTfaBackupKeyBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.tryToGetString
import com.cryptoexchange.mobile.presentation.tfa.code.TfaCodeFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TfaBackupKeyFragment : BaseFragment(), TfaBackupKeyView {

    private lateinit var binding: FragmentTfaBackupKeyBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: TfaBackupKeyPresenter

    @ProvidePresenter
    fun providePresenter(): TfaBackupKeyPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)

        val backupKey = tryToGetString(KEY_BACKUP_KEY)
        presenter.data(backupKey)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTfaBackupKeyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            buttonNext.setOnClickListener { presenter.onNextClicked() }
            backupKeyLayout.setEndIconOnClickListener { presenter.onCopyClicked() }
        }
    }

    override fun showBackupKey(key: String) {
        binding.tfaBackupKeyInput.setText(key)
    }

    override fun navigateToTfaCodeScreen(actionId: Int, key: String) {
        val arguments = TfaCodeFragment.getBundle(key)
        navigate(actionId, arguments)
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun showCopyResult(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val KEY_BACKUP_KEY = "KEY_BACKUP_KEY"

        fun getBundle(backupKey: String): Bundle =
            Bundle().apply {
                putString(KEY_BACKUP_KEY, backupKey)
            }
    }
}