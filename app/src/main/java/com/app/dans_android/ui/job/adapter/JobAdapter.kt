package com.app.dans_android.ui.job.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.dans_android.data.api.model.Job
import com.app.dans_android.databinding.ItemJobListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

internal class JobAdapter(
    private val onSelectJob: (String) -> Unit,
) : ListAdapter<Job, JobAdapter.JobViewHolder>(ProvinceDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JobViewHolder {
        val viewBinding =
            ItemJobListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(viewBinding, onSelectJob)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    internal class JobViewHolder(
        private val viewBinding: ItemJobListBinding,
        private val onSelectJob: (String) -> Unit,
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: Job) {
            viewBinding.apply {
                titleJob.text = item.title
                jobCompany.text = item.company
                jobLocation.text = item.location

                Glide.with(this.root)
                    .load(item.url)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imgCompany)

                root.setOnClickListener {
                    onSelectJob(item.id)
                }
            }
        }
    }

    object ProvinceDiffCallback : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem == newItem
        }
    }
}