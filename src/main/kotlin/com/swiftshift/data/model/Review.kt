package com.swiftshift.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Review(
    val review: String,
    val star: Int,
    val gigWorkerId: String,
    val gigProviderId: String,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
)
