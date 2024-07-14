package com.app.dans_android.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.dans_android.R
import com.app.dans_android.data.job.api.model.Job
import com.app.dans_android.databinding.ActivityJobDetailBinding
import com.app.dans_android.ui.component.ProgressDialog
import com.app.dans_android.ui.job.JobEvent
import com.app.dans_android.ui.job.JobViewModel
import com.app.dans_android.util.constant.JobConstant
import com.app.dans_android.util.constant.JobConstant.FULL_TIME
import com.app.dans_android.util.ext.observeState
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobDetailBinding

    private val viewModel: JobViewModel by viewModels()
    private val jobId: String by lazy {
        intent.getStringExtra(JobConstant.EXTRA_JOB_ID).orEmpty()
    }
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initData()
        initListener()
        initObserver()
    }

    private fun initData() {
        viewModel.onEvent(JobEvent.JobDetail(jobId = jobId))
    }

    private fun initListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun initObserver() {
        observeState(source = viewModel.state) { state ->
            renderLoading(isLoading = state.isLoading)
            renderJobDetail(job = state.jobDetail)
            handleJobsError(isError = state.isError, message = state.errorMessage)
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        progressDialog.show().takeIf { isLoading } ?: progressDialog.dismiss()
    }

    private fun renderJobDetail(job: Job?) {
        job?.let {
            val fullTimeText = if (it.type.equals(FULL_TIME, ignoreCase = true)) {
                getString(R.string.yes)
            } else {
                getString(R.string.no)
            }

            bindJobDetails(it, fullTimeText)
            setupCompanyLogo(it.companyLogo)
            setupJobLink(it.companyUrl)
        }
    }

    private fun bindJobDetails(job: Job, fullTimeText: String) {
        binding.apply {
            jobCompany.text = job.company
            jobLocation.text = job.location
            jobTitle.text = job.title
            jobFulltime.text = fullTimeText
            jobDesc.text = job.description
        }
    }

    private fun setupCompanyLogo(companyLogo: String?) {
        if (!companyLogo.isNullOrEmpty()) {
            Glide.with(this)
                .load(companyLogo)
                .placeholder(R.drawable.ic_image_not_supported_24)
                .error(R.drawable.baseline_error_24)
                .into(binding.imgCompany)
        } else {
            binding.imgCompany.setImageResource(R.drawable.ic_image_not_supported_24)
        }
    }

    private fun setupJobLink(companyUrl: String?) {
        binding.jobLink.setOnClickListener {
            companyUrl?.let { url ->
                openWebPage(url)
            } ?: run {
                Toast.makeText(this, getString(R.string.url_not_available), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun handleJobsError(isError: Boolean, message: String) {
        if (!isError) return

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }
}
