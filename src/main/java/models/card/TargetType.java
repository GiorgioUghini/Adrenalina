package models.card;

import com.google.gson.annotations.SerializedName;

public enum TargetType{
    @SerializedName("player")
    PLAYER,

    @SerializedName("room")
    ROOM,

    @SerializedName("square")
    SQUARE
}