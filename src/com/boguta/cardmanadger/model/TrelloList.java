package com.boguta.cardmanadger.model;

import java.util.List;

import com.boguta.cardmanadger.DataManager.ListType;

public class TrelloList {
    private List<Card> cards;

    private String id;
    private String name;
    private boolean closed;
    private int pos;
    private boolean subscribed;
    private ListType type;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ListType getType() {
        return type;
    }

    public void setType(ListType type) {
        this.type = type;
    }

    public void addCard(Card card) {
        if (cards != null) {
            cards.add(card);
        }
    }

    public void removeCard(Card card) {
        if (cards != null) {
            cards.remove(card);
        }
    }

}
