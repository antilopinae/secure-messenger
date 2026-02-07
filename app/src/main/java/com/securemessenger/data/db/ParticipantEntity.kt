package com.securemessenger.data.db

import androidx.room.*

@Entity(
    tableName = "participants",
    indices = [
        Index("ed25519PublicKey"),
        Index("x25519PublicKey")
    ]
)
data class ParticipantEntity(
    @PrimaryKey val nodeId: String,
    val name: String,
    val ed25519PublicKey: String,
    val x25519PublicKey: String
)
