package com.swiftshift.util

object ApiResponseMessages {

    const val USER_ALREADY_EXISTS = "A user with this email already exists."
    const val USER_NOT_FOUND = "The user couldn't be found."
    const val INVALID_CREDENTIAL = "Your username or password is not correct. Please try again."
    const val FIELDS_BLANK = "The fields may not be empty."
    const val REVIEW_TOO_LONG = "The review length must not exceed ${Constants.MAX_REVIEW_LENGTH} characters."
    const val INVALID_EMAIL = "Please use a valid email address."
    const val REVIEW_CREATED_SUCCESSFULLY = "Your review has been created successfully"

}