package models.card;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum AreaType implements Serializable {
    @SerializedName("visible")
    VISIBLE,

    @SerializedName("cardinal")
    CARDINAL,

    @SerializedName("room")
    ROOM,

    @SerializedName("other_rooms")
    OTHER_ROOMS
}
