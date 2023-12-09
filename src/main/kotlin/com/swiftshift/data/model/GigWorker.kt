package com.swiftshift.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class GigWorker(
    val fullName: String,
    val email: String,
    val password: String,
    val timestamp: Long,
    val profileImageUrl: String? = null,
    val totalIncome: Double? = null,
    val gender: String? = null,
    val highestEducation: String? = null,
    val cvUrl: String? = null,
    @BsonId
    val id: String = ObjectId().toString()
)
