package com.boguta.cardmanadger;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.boguta.cardmanadger.model.Board;
import com.boguta.cardmanadger.model.Card;
import com.google.gson.Gson;

public class CardManager {
    private Board mBoard;
    private RequestQueue mRequestQueue;
    private Gson mGson;

    public CardManager(Board board, RequestQueue queue) {
        mBoard = board;
        mRequestQueue = queue;
        mGson = new Gson();
    }

    public List<Card> getAllCardsOnList(String idList) {
        return mBoard.getListById(idList).getCards();
    }

    public Card getCard(String listId, String cardId) {
        if (cardId == null) {
            return null;
        }

        return mBoard.getListById(listId).getCardById(cardId);
    }

    public void addCard(OnRequestFinishedCallback<String> callback, final Card card) {
        if (mRequestQueue == null || card == null) {
            callback.onFail(new VolleyError("RequestQueue == null " + (mRequestQueue == null)
                    + " or card == null " + (card == null)));
            return;
        }
        card.setIdList(mBoard.getListByName(Constants.TODO_LIST).getId());
        RequestListenerImpl<String> listener = new RequestListenerImpl<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Card card = gson.fromJson((String) response, Card.class);
                mBoard.getListById(card.getIdList()).addCard(card);
                super.onResponse(response);
            }
        };
        listener.setOnRequestFinishedCallback(callback);
        StringRequest request = new StringRequest(Method.POST, Constants.CARDS
                + Constants.AUTH_STRING, listener, listener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("due", card.getDue());
                map.put("idList", card.getIdList());
                map.put("name", card.getName());
                return map;
            }
        };
        mRequestQueue.add(request);
        mRequestQueue.start();
    }

    public void editCard(OnRequestFinishedCallback<String> callback, final Card card) {
        final List<Card> list = mBoard.getListById(card.getIdList()).getCards();
        final int position = list.indexOf(getCard(card.getIdList(), card.getId()));

        if (mRequestQueue == null || card == null) {
            callback.onFail(new VolleyError("RequestQueue == null " + (mRequestQueue == null)
                    + " or card == null " + (card == null)));
            return;
        }
        RequestListenerImpl<String> listener = new RequestListenerImpl<String>() {
            @Override
            public void onResponse(String response) {
                list.remove(position);
                list.add(mGson.fromJson(response, Card.class));
                Collections.sort(list);
                super.onResponse(response);
            }
        };
        listener.setOnRequestFinishedCallback(callback);
        StringRequest request = new StringRequest(Method.PUT, Constants.CARDS + card.getId()
                + Constants.AUTH_STRING, listener, listener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return cardToMap(card);
            }
        };
        mRequestQueue.add(request);
        mRequestQueue.start();
    }

    private Map<String, String> cardToMap(Card card) {
        Map<String, String> map = new HashMap<String, String>();
        String pos = String.valueOf(card.getPos());
        map.put(Constants.CARD_KEY_POS, pos);
        map.put(Constants.CARD_KEY_NAME, card.getName());
        map.put(Constants.CARD_KEY_IDLIST, card.getIdList());
        String due = card.getDue();
        if (due != null) {
            map.put(Constants.CARD_KEY_DUE, card.getDue());
        }
        return map;
    }

    public void moveCard(OnRequestFinishedCallback<String> callback, final Card card,
            final String destList, final int destPos, final boolean movingUp) {
        if (mRequestQueue == null || card == null) {
            callback.onFail(new VolleyError("RequestQueue == null " + (mRequestQueue == null)
                    + " or card == null " + (card == null)));
            return;
        }
        RequestListenerImpl<String> listener = new RequestListenerImpl<String>() {
            @Override
            public void onResponse(String response) {
                List<Card> fromList = mBoard.getListById(card.getIdList()).getCards();
                List<Card> toList = mBoard.getListById(destList).getCards();
                Card newCard = mGson.fromJson(response, Card.class);
                fromList.remove(card);
                toList.add(newCard);
                Collections.sort(toList);
                super.onResponse(response);
            }
        };
        listener.setOnRequestFinishedCallback(callback);
        StringRequest request = new StringRequest(Method.PUT, Constants.CARDS + card.getId()
                + Constants.AUTH_STRING, listener, listener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String position = getNewCardPosition(card, destPos, movingUp, destList);
                if (!destList.equals(card.getIdList())) {
                    map.put("idList", destList);
                }
                map.put("pos", position);
                return map;
            }
        };

        mRequestQueue.add(request);
        mRequestQueue.start();
    }

    private String getNewCardPosition(Card card, int pos, boolean movingUp, String destList) {
        if (pos <= 0) {
            return Constants.TOP_POS;
        }
        List<Card> list = mBoard.getListById(destList).getCards();
        if (pos >= list.size() - 1) {
            return Constants.BOTTOM_POS;
        }
        Card target = list.get(pos);

        double targetPos = Double.valueOf(target.getPos());
        double newPos = targetPos;
        if (movingUp) {
            newPos -= Constants.CARD_MOVE_SAFE_THRESHOLD;
        } else {
            newPos += Constants.CARD_MOVE_SAFE_THRESHOLD;
        }

        DecimalFormat format = new DecimalFormat("#");
        return format.format(newPos);
    }

    public void removeCard(OnRequestFinishedCallback<String> callback, final Card card) {
        if (mRequestQueue == null || card == null) {
            callback.onFail(new VolleyError("RequestQueue == null " + (mRequestQueue == null)
                    + " or card == null " + (card == null)));
            return;
        }
        RequestListenerImpl<String> listener = new RequestListenerImpl<String>() {
            @Override
            public void onResponse(String response) {
                mBoard.getListById(card.getIdList()).removeCard(card);
                super.onResponse(response);
            }
        };
        listener.setOnRequestFinishedCallback(callback);
        StringRequest request = new StringRequest(Method.DELETE, Constants.CARDS + card.getId()
                + Constants.AUTH_STRING, listener, listener);
        mRequestQueue.add(request);
        mRequestQueue.start();
    }

    private class RequestListenerImpl<T> implements Listener<T>, ErrorListener {
        private OnRequestFinishedCallback<T> callback;

        public void setOnRequestFinishedCallback(OnRequestFinishedCallback<T> callback) {
            this.callback = callback;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (callback != null) {
                callback.onFail(error);
            }
        }

        @Override
        public void onResponse(T response) {
            if (callback != null) {
                callback.onSuccess(response);
            }
        }
    }
}
