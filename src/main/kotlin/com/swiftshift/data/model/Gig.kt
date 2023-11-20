package com.swiftshift.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Gig(
    val imageUrl: String,
    val description: String,
    val gigProviderId: String,
    val maxApplier: String,
    val currentApplier: String,
    val deadlineDate: Long,
    val salary: Double,
    val latitude: Long,
    val longitude: Long,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
)
