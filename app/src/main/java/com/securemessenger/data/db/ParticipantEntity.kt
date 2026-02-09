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
    @PrimaryKey
    @ColumnInfo(name = "nodeId")
    val nodeId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "ed25519PublicKey")
    val ed25519PublicKey: String,

    @ColumnInfo(name = "x25519PublicKey")
    val x25519PublicKey: String
)