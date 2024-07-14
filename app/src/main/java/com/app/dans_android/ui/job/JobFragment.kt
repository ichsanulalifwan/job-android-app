package com.app.dans_android.ui.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.dans_android.databinding.FragmentJobBinding
import com.app.dans_android.observer.observeState
import com.app.dans_android.ui.job.adapter.JobAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobFragment : Fragment() {

    private var _binding: FragmentJobBinding? = null
    private val binding get() = _binding

    private val viewModel: JobViewModel by viewModels()

    private val jobAdapter: JobAdapter by lazy {
        JobAdapter(
            onSelectJob = {

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
//        initView()
        initObserver()
//        initLoadMore()
    }

    private fun initObserver() {
        viewLifecycleOwner.observeState(source = viewModel.state) { state ->

        }
    }
//
//    private fun renderLoadingProvinces(isLoadingProvince: Boolean) {
//        binding?.loadingItem?.also {loading ->
//            loading.isVisible = isLoadingProvince
//            if (isLoadingProvince) loading.startShimmer() else loading.stopShimmer()
//        }
//    }
//
//    private fun renderLoadingPaginate(isLoadingPaginate: Boolean) {
//        binding?.loadingBarPagination?.isVisible = isLoadingPaginate
//    }
//
//    private fun renderUIViewSuccess(isShowing: Boolean) {
//        binding?.rvJobList?.isVisible = isShowing
//    }
//
//    private fun renderJobs(jobs: List<Job>) {
//        jobAdapter.submitList(jobs)
//    }
//
//    private fun handleJobsError(isError: Boolean, message: String) {
//        if (!isError) return
//
//        errorListener(message)
//    }
}