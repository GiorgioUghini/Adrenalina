package models.card;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum Color implements Serializable {
    @SerializedName("red")
    RED,

    @SerializedName("yellow")
    YELLOW,

    @SerializedName("blue")
    BLUE
}

