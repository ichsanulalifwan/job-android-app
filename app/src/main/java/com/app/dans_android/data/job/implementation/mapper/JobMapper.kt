package com.app.dans_android.data.job.implementation.mapper

import com.app.dans_android.data.job.api.model.Job
import com.app.dans_android.data.job.implementation.response.JobResponse

internal fun JobResponse.toJob(): Job {
    return Job(
        id = id.orEmpty(),
        companyLogo = companyLogo.orEmpty(),
        howToApply = howToApply.orEmpty(),
        description = description.orEmpty(),
        company = company.orEmpty(),
        companyUrl = companyUrl.orEmpty(),
        location = location.orEmpty(),
        type = type.orEmpty(),
        title = title.orEmpty(),
        url = url.orEmpty(),
    )
}
