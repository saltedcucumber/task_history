package com.cryptoexchange.mobile.presentation.more.activitylog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentActivityLogBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.presentation.more.activitylog.adapter.ActivityLogAdapter
import com.cryptoexchange.source.entity.user.activities.UserActivityModel
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class ActivityLogFragment : BaseFragment(), ActivityLogView {

    private lateinit var binding: FragmentActivityLogBinding
    private lateinit var adapter: ActivityLogAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: ActivityLogPresenter

    @ProvidePresenter
    fun providePresenter(): ActivityLogPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityLogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun setupUI() {
        super.setupUI()

        with(binding) {
            activityLogSwipeToRefresh.setOnRefreshListener { presenter.onSwipeToRefresh() }
            buttonBack.setOnClickListener { presenter.onBackPressed() }

            adapter = ActivityLogAdapter()
            activityLogRecycler.layoutManager = LinearLayoutManager(requireContext())
            activityLogRecycler.adapter = adapter
        }
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun showUserActivities(activities: List<UserActivityModel>) {
        adapter.setItems(activities)

        binding.activityLogSwipeToRefresh.isRefreshing = false
    }
}