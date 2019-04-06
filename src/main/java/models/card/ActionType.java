package models.card;

import com.google.gson.annotations.SerializedName;

public enum ActionType{
    @SerializedName("select")
    SELECT,

    @SerializedName("damage")
    DAMAGE,

    @SerializedName("mark")
    MARK,

    @SerializedName("move")
    MOVE
}
