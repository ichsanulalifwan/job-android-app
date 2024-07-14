package com.app.dans_android.ui.job

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.dans_android.R
import com.app.dans_android.data.job.api.model.Job
import com.app.dans_android.databinding.FragmentJobBinding
import com.app.dans_android.ui.detail.JobDetailActivity
import com.app.dans_android.ui.job.adapter.JobAdapter
import com.app.dans_android.util.constant.JobConstant.EXTRA_JOB_ID
import com.app.dans_android.util.ext.isLastVisible
import com.app.dans_android.util.ext.observeState
import com.app.dans_android.util.ext.orEmpty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class JobFragment : Fragment() {

    private var _binding: FragmentJobBinding? = null
    private val binding get() = _binding

    private val viewModel: JobViewModel by viewModels()

    private val jobAdapter: JobAdapter by lazy {
        JobAdapter(
            onSelectJob = {
                navigateToDetailPage(jobId = it)
            },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initObserver()
        initLoadMore()
    }

    private fun initView() {
        binding?.rvJobList?.also { view ->
            view.adapter = jobAdapter
            view.itemAnimator = null
            view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        handleSearch()
        viewModel.onEvent(JobEvent.DefaultJob)
    }

    private fun initListener() {
        binding?.btnShowFilter?.setOnClickListener {
            viewModel.onEvent(JobEvent.ShowFilter)
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.observeState(source = viewModel.state) { state ->
            renderLoadingJobs(isLoadingJob = state.isLoading)
            renderLoadingPaginate(isLoadingPaginate = state.isLoadingPaginate)
            renderUIViewSuccess(isShowing = state.isUISuccessShowing)
            renderJobs(jobs = state.jobList)
            renderFilter(isShowing = state.isShowFilter)
            handleJobsError(isError = state.isError, message = state.errorMessage)
        }
    }

    @OptIn(FlowPreview::class)
    private fun handleSearch() {
        binding?.apply {
            searchEditText.also { input ->
                callbackFlow {
                    val textWatcher = object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int,
                        ) {
                        }

                        override fun onTextChanged(
                            search: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int,
                        ) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            trySend(input.text.toString())
                        }
                    }

                    input.addTextChangedListener(textWatcher)
                    awaitClose { input.removeTextChangedListener(textWatcher) }
                }.debounce(500L).onEach { strSearch ->
                    if (strSearch.isNotEmpty()) {
                        viewModel.onEvent(
                            JobEvent.SearchJob(
                                searchDescription = strSearch,
                                searchLocation = binding?.searchLocation?.text.toString(),
                                isFullTime = binding?.switchFullTime?.isChecked.orEmpty(),
                            )
                        )
                    } else if (strSearch.isEmpty())
                        viewModel.onEvent(JobEvent.DefaultJob)
                }.filter { textSearch ->
                    textSearch.isNotBlank()
                }.launchIn(scope = lifecycleScope)
            }

            btnFilter.setOnClickListener {
                viewModel.onEvent(
                    JobEvent.SearchJob(
                        searchDescription = searchEditText.text.toString(),
                        searchLocation = searchLocation.text.toString(),
                        isFullTime = switchFullTime.isChecked.orEmpty(),
                    )
                )
            }
        }
    }

    private fun renderLoadingJobs(isLoadingJob: Boolean) {
        binding?.loadingItem?.also { loading ->
            loading.isVisible = isLoadingJob
            if (isLoadingJob) loading.startShimmer() else loading.stopShimmer()
        }
    }

    private fun initLoadMore() {
        binding?.rvJobList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.isLastVisible()) {
                    viewModel.onEvent(JobEvent.LoadMore)
                }
            }
        })
    }

    private fun renderLoadingPaginate(isLoadingPaginate: Boolean) {
        binding?.loadingBarPagination?.isVisible = isLoadingPaginate
    }

    private fun renderUIViewSuccess(isShowing: Boolean) {
        binding?.rvJobList?.isVisible = isShowing
    }

    private fun renderFilter(isShowing: Boolean) {
        binding?.apply {
            lytFilter.isVisible = isShowing
            btnShowFilter.background = if (isShowing) ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_arrow_up
            ) else ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)
        }
    }

    private fun renderJobs(jobs: List<Job>) {
        jobAdapter.submitList(jobs)
    }

    private fun handleJobsError(isError: Boolean, message: String) {
        if (!isError) return

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun navigateToDetailPage(jobId: String) {
        val intent = Intent(activity, JobDetailActivity::class.java).apply {
            putExtra(EXTRA_JOB_ID, jobId)
        }
        activity?.startActivity(intent)
    }
}
