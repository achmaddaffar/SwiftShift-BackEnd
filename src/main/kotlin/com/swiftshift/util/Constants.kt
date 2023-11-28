package com.swiftshift.util

object Constants {

    const val DATABASE_NAME = "SwiftShift_Database"
    const val BASE_URL = "http://localhost:8005/"
    const val PROFILE_PICTURE_PATH = "build/resources/main/static/profile_pictures/"
    const val GIG_IMAGE_PATH = "build/resources/main/static/gig_images/"

    const val CREATE_ACCOUNT_PART_DATA = "create_account_data"
    const val UPDATE_PROFILE_PART_DATA = "update_profile_data"
    const val CREATE_GIG_PART_DATA = "create_gig_data"

    const val GIG_STATUS_PENDING = 1
    const val GIG_STATUS_REJECTED = 2
    const val GIG_STATUS_ACCEPTED = 3

    const val DEFAULT_NEARBY_GIGS_PAGE_SIZE = 15
    const val DEFAULT_GIGS_BY_PROVIDER_SIZE = 15
    const val DEFAULT_REVIEWS_TO_GIG_PROVIDER_SIZE = 15

    const val MAX_REVIEW_LENGTH = 2000

    const val GENDER_MALE = "Male"
    const val GENDER_FEMALE = "Female"

    const val GIG_WORKER_ROLE = "gigWorker"
    const val GIG_PROVIDER_ROLE = "gigProvider"

    const val JWT_CLAIM_GIG_WORKER_ID = "gigWorkerId"
    const val JWT_CLAIM_GIG_PROVIDER_ID = "gigProviderId"

    const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"

    inline val String.Companion.Empty
        get() = ""
}