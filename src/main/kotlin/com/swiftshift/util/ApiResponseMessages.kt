package com.swiftshift.util

object ApiResponseMessages {

    const val USER_ALREADY_EXISTS = "A user with this email already exists."
    const val USER_NOT_FOUND = "The user couldn't be found."
    const val INVALID_CREDENTIAL = "Your username or password is not correct. Please try again."
    const val FIELDS_BLANK = "The fields may not be empty."
    const val REVIEW_TOO_LONG = "The review length must not exceed ${Constants.MAX_REVIEW_LENGTH} characters."
    const val INVALID_EMAIL = "Please use a valid email address."
    const val REVIEW_CREATED_SUCCESSFULLY = "Your review has been created successfully"
    const val GIG_NOT_FOUND = "The gig couldn't be found."
    const val GIG_REGISTRATION_PASSED_DEADLINE = "Gig registration has passed the registration deadline."
    const val GIG_REACHED_MAX_APPLIER = "The list of Gig applicants is already full."
}