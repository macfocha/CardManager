package com.boguta.cardmanadger;

import android.content.Context;

import com.boguta.cardmanadger.model.Board;
import com.boguta.cardmanadger.model.Card;

public class DataManager {
    protected static final String TAG = DataManager.class.getSimpleName();
    private Context mContext;
    private CardManager mCardManager;
    private Board mBoard;

    public enum ListType {
        TODO,
        DOING,
        DONE,
        INVALID
    }

    public DataManager(Context context, Board board) {
        mContext = context;
        mBoard = board;
        mCardManager = new CardManager(mBoard);
    }

    public Card getCard(ListType type, String cardId) {
        return mCardManager.getCard(type, cardId);
    }

}
