package com.swiftshift.util

object Constants {

    const val DATABASE_NAME = "SwiftShift_Database"
    const val DEFAULT_NEARBY_GIGS_PAGE_SIZE = 15
    const val DEFAULT_GIGS_BY_PROVIDER_SIZE = 15
    const val BASE_URL = "http://localhost:8005"
    const val PROFILE_PICTURE_PATH = "build/resources/main/static/profile_pictures/"
    const val GIG_IMAGE_PATH = "build/resources/main/static/gig_images"

    const val GIG_STATUS_PENDING = 1
    const val GIG_STATUS_REJECTED = 2
    const val GIG_STATUS_ACCEPTED = 3

    const val GENDER_MALE = "Male"
    const val GENDER_FEMALE = "Female"

    inline val String.Companion.Empty
        get() = ""
}