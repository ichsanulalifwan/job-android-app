package com.app.dans_android.data.job.implementation.mapper

import com.app.dans_android.data.job.api.model.Job
import com.app.dans_android.data.job.implementation.response.JobResponse

internal fun JobResponse?.toJob(): Job {
    return Job(
        id = this?.id.orEmpty(),
        companyLogo = this?.companyLogo.orEmpty(),
        howToApply = this?.howToApply.orEmpty(),
        description = this?.description.orEmpty(),
        company = this?.company.orEmpty(),
        companyUrl = this?.companyUrl.orEmpty(),
        location = this?.location.orEmpty(),
        type = this?.type.orEmpty(),
        title = this?.title.orEmpty(),
        url = this?.url.orEmpty(),
    )
}
