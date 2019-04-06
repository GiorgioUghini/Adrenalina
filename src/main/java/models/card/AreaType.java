package models.card;

import com.google.gson.annotations.SerializedName;

public enum AreaType{
    @SerializedName("visible")
    VISIBLE,

    @SerializedName("cardinal")
    CARDINAL,

    @SerializedName("room")
    ROOM,

    @SerializedName("other_rooms")
    OTHER_ROOMS
}
