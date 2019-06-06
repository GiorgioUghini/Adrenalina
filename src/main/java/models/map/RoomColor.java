package models.map;

import com.google.gson.annotations.SerializedName;
import models.card.Taggable;

import java.io.Serializable;

public enum RoomColor implements Serializable, Taggable {
    @SerializedName("blue")
    BLUE,
    @SerializedName("red")
    RED,
    @SerializedName("purple")
    PURPLE,
    @SerializedName("white")
    WHITE,
    @SerializedName("yellow")
    YELLOW,
    @SerializedName("green")
    GREEN
}
