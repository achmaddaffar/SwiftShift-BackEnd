package com.swiftshift.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class GigWorker(
    val fullName: String,
    val email: String,
    val password: String,
    val profileImageUrl: String,
    @BsonId
    val id: String = ObjectId().toString()
)
