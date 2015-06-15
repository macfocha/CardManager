package com.boguta.cardmanadger;

import android.content.Context;

import com.android.volley.VolleyError;
import com.boguta.cardmanadger.model.Board;
import com.boguta.cardmanadger.model.Card;

public class DataManager {
    protected static final String TAG = DataManager.class.getSimpleName();
    private CardManager mCardManager;
    private ConnectionManager mConnectionManager;
    private Board mBoard;

    public DataManager(Context context, Board board, ConnectionManager manager) {
        mBoard = board;
        mConnectionManager = manager;
        mCardManager = new CardManager(mBoard, manager.getRequestQueue());
    }

    public void addCard(OnRequestFinishedCallback<String> callback, Card card) {
        if (!mConnectionManager.isConnected()) {
            callback.onFail(new VolleyError("No data conncetion"));
            return;
        }
        mCardManager.addCard(callback, card);
    }

    public void removeCard(OnRequestFinishedCallback<String> callback, Card card) {
        if (!mConnectionManager.isConnected()) {
            callback.onFail(new VolleyError("No data conncetion"));
            return;
        }
        mCardManager.removeCard(callback, card);
    }

    public void editCard(OnRequestFinishedCallback<String> callback, Card card) {
        if (!mConnectionManager.isConnected()) {
            callback.onFail(new VolleyError("No data conncetion"));
            return;
        }
        mCardManager.editCard(callback, card);
    }

    public void moveCard(OnRequestFinishedCallback<String> callback, Card card, String destList,
            int destPosition, boolean movingUp) {
        if (!mConnectionManager.isConnected()) {
            callback.onFail(new VolleyError("No data conncetion"));
            return;
        }
        mCardManager.moveCard(callback, card, destList, destPosition, movingUp);
    }

}
