package models;

public abstract class Card {
    private String name;

    public Card(){};

    public Card(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}