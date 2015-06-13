package com.boguta.cardmanadger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.boguta.cardmanadger.DataManager.ListType;
import com.boguta.cardmanadger.model.Board;
import com.boguta.cardmanadger.model.Card;
import com.boguta.cardmanadger.model.TrelloList;

public class DataSyncer {
    private static final String TAG = DataSyncer.class.getSimpleName();
    private Context mContext;
    private RequestQueue mRequestQueue;
    private Board mBoard;
    private int mListsDone = 0;
    private OnSyncFinishedCallback mOnSyncFinished;

    public DataSyncer(Context context, OnSyncFinishedCallback callback) {
        mOnSyncFinished = callback;
        mRequestQueue = Volley.newRequestQueue(mContext);
        retrieveData();
    }

    private void retrieveData() {
        GsonRequest<Board> requestBoard = new GsonRequest<Board>(Constants.TRELLO_BOARDS
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
        GsonRequest<TrelloList[]> requestLists = new GsonRequest<TrelloList[]>(
                Constants.TRELLO_BOARDS + Constants.MY_BOARD_ID + "/lists" + Constants.AUTH_STRING,
                TrelloList[].class, null, new Listener<TrelloList[]>() {
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
            setListType(list);
            GsonRequest<Card[]> requestLists = new GsonRequest<Card[]>(Constants.TRELLO_LISTS
                    + list.getId() + "/cards" + Constants.AUTH_STRING, Card[].class, null,
                    new Listener<Card[]>() {
                        @Override
                        public void onResponse(Card[] cards) {
                            mListsDone++;
                            List<Card> cardsList = new ArrayList<Card>();
                            Collections.addAll(cardsList, cards);
                            list.setCards(cardsList);
                            if (mListsDone >= mBoard.getLists().size() && mOnSyncFinished != null) {
                                mListsDone = 0;
                                mOnSyncFinished.onSyncSuccess(mBoard);
                            }
                        }
                    }, mErrorListener);

            mRequestQueue.add(requestLists);
        }
        mRequestQueue.start();

    }

    private void setListType(TrelloList list) {
        switch (list.getName()) {
        case "To do":
            list.setType(ListType.TODO);
            break;
        case "Doing":
            list.setType(ListType.DOING);
            break;
        case "Done":
            list.setType(ListType.DOING);
            break;
        default:
            list.setType(ListType.INVALID);
        }
    }

    private ErrorListener mErrorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            if (mOnSyncFinished != null) {
                mOnSyncFinished.onSyncFail(error);
            }
        }
    };

    public void setOnSyncFinishedCallback(OnSyncFinishedCallback callback) {
        mOnSyncFinished = callback;
    }

}
