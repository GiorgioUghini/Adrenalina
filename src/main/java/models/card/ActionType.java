package models.card;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum ActionType implements Serializable {
    @SerializedName("select")
    SELECT,

    @SerializedName("damage")
    DAMAGE,

    @SerializedName("mark")
    MARK,

    @SerializedName("move")
    MOVE
}
