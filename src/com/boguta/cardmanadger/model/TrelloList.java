package com.boguta.cardmanadger.model;

import java.util.List;

public class TrelloList {
    private List<Card> cards;

    private String id;
    private String name;

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

    public void addCard(Card card) {
        if (cards != null && card != null) {
            cards.add(card);
        }
    }

    public void removeCard(Card card) {
        if (cards != null) {
            cards.remove(card);
        }
    }

    public Card getCardByName(String name) {
        if (cards != null && name != null) {
            for (Card card : cards) {
                if (name.equals(card.getName())) {
                    return card;
                }
            }
        }
        return null;
    }

    public Card getCardById(String id) {
        if (cards != null && id != null) {
            for (Card card : cards) {
                if (id.equals(card.getId())) {
                    return card;
                }
            }
        }
        return null;
    }

}
