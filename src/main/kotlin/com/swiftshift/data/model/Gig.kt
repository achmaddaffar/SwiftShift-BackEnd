package com.swiftshift.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Gig(
    val title: String,
    val imageUrl: String,
    val description: String,
    val tag: String,
    val gigProviderId: String,
    val gigProviderName: String,
    val maxApplier: Int,
    val currentApplier: Int,
    val deadlineDate: Long,
    val salary: Double,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
)
