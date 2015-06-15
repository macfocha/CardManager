package com.boguta.cardmanadger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.boguta.cardmanadger.model.Board;
import com.boguta.cardmanadger.model.Card;
import com.boguta.cardmanadger.model.TrelloList;

public class DataSyncer {
    private static final String TAG = DataSyncer.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private Board mBoard;
    private int mListsDone = 0;
    private OnRequestFinishedCallback<Board> mOnSyncFinished;

    public DataSyncer(OnRequestFinishedCallback<Board> callback, RequestQueue queue) {
        mOnSyncFinished = callback;
        mRequestQueue = queue;
        retrieveData();
    }

    private void retrieveData() {
        GsonRequest<Board> requestBoard = new GsonRequest<Board>(Constants.BOARDS
                + Constants.MY_BOARD_ID + Constants.AUTH_STRING, Board.class, null,
                new Listener<Board>() {
                    @Override
                    public void onResponse(Board response) {
                        mBoard = response;
                        getLists();
                    }
                }, mErrorListener);

        mRequestQueue.add(requestBoard);
        mRequestQueue.start();
    }

    private void getLists() {
        GsonRequest<TrelloList[]> requestLists = new GsonRequest<TrelloList[]>(Constants.BOARDS
                + Constants.MY_BOARD_ID + "/lists" + Constants.AUTH_STRING, TrelloList[].class,
                null, new Listener<TrelloList[]>() {
                    @Override
                    public void onResponse(TrelloList[] lists) {
                        List<TrelloList> list = new ArrayList<TrelloList>();
                        Collections.addAll(list, lists);
                        mBoard.setLists(list);
                        requestCards();
                    }
                }, mErrorListener);

        mRequestQueue.add(requestLists);
        mRequestQueue.start();
    }

    private void requestCards() {
        if (mBoard.getLists() == null || mBoard.getLists().size() != 3) {
            Log.e(TAG, "Lists empty");
            return;
        }
        for (final TrelloList list : mBoard.getLists()) {
            GsonRequest<Card[]> requestLists = new GsonRequest<Card[]>(Constants.LISTS
                    + list.getId() + "/cards" + Constants.AUTH_STRING, Card[].class, null,
                    new Listener<Card[]>() {
                        @Override
                        public void onResponse(Card[] cards) {
                            mListsDone++;
                            List<Card> cardsList = new ArrayList<Card>();
                            Collections.addAll(cardsList, cards);
                            Collections.sort(cardsList);
                            list.setCards(cardsList);
                            if (mListsDone >= mBoard.getLists().size() && mOnSyncFinished != null) {
                                mListsDone = 0;
                                mOnSyncFinished.onSuccess(mBoard);
                            }
                        }
                    }, mErrorListener);

            mRequestQueue.add(requestLists);
        }
        mRequestQueue.start();

    }

    private ErrorListener mErrorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            if (mOnSyncFinished != null) {
                mOnSyncFinished.onFail(error);
            }
        }
    };

    public void setOnSyncFinishedCallback(OnRequestFinishedCallback<Board> callback) {
        mOnSyncFinished = callback;
    }

}
