package models.map;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum RoomColor implements Serializable {
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
