package com.boguta.cardmanadger;

import java.util.List;

import com.boguta.cardmanadger.DataManager.ListType;
import com.boguta.cardmanadger.model.Board;
import com.boguta.cardmanadger.model.Card;
import com.boguta.cardmanadger.model.TrelloList;

public class CardManager {
    private Board mBoard;

    public CardManager(Board board) {
        mBoard = board;
    }

    public List<Card> getAllCardsOnList(ListType type) {
        return mBoard.getList(type).getCards();
    }

    public Card getCard(ListType type, String cardId) {
        if (cardId == null) {
            return null;
        }
        List<Card> cards = getAllCardsOnList(type);
        for (Card card : cards) {
            if (cardId.equals(card.getId())) {
                return card;
            }
        }
        return null;
    }

    public void addCard(ListType type, Card card) {
        TrelloList list = mBoard.getList(type);
        list.addCard(card);
    }

    public void removeCard(ListType type, Card card) {
        mBoard.getList(type).removeCard(card);
    }

    public void removeCard(ListType type, String id) {
        removeCard(type, getCard(type, id));
    }
}
