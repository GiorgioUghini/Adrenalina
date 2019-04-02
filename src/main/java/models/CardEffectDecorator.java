package models;

public abstract class CardEffectDecorator extends Card {
    protected Card card;

    @Override
    public abstract String getName();
}
