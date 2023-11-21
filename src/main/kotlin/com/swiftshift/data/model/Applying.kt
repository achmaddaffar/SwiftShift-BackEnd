package com.swiftshift.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Applying(
    val gigWorkerId: String,
    val gigId: String,
    val status: Int,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
)
