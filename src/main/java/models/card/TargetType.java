package models.card;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum TargetType implements Serializable {
    @SerializedName("player")
    PLAYER,

    @SerializedName("room")
    ROOM,

    @SerializedName("square")
    SQUARE
}