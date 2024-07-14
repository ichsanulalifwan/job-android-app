package com.app.dans_android.data.implementation.mapper

import com.app.dans_android.data.api.model.Job
import com.app.dans_android.data.implementation.response.JobResponse

internal fun JobResponse.toJob(): Job {
    return Job(
        id = id.orEmpty(),
        companyLogo = companyLogo.orEmpty(),
        howToApply = howToApply.orEmpty(),
        createdAt = createdAt.orEmpty(),
        description = description.orEmpty(),
        company = company.orEmpty(),
        companyUrl = companyUrl.orEmpty(),
        location = location.orEmpty(),
        type = type.orEmpty(),
        title = title.orEmpty(),
        url = url.orEmpty(),
    )
}
