package com.boguta.cardmanadger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.boguta.cardmanadger.model.Board;

public class ConnectionManager {
    private Context mContext;
    private DataManager mDataManager;
    private RequestQueue mRequestQueue;

    public ConnectionManager(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public boolean syncData(OnRequestFinishedCallback<Board> callback) {
        if (isConnected()) {
            new DataSyncer(callback, mRequestQueue);
            return true;
        }
        return false;
    }

    public void onDestroy() {
        if (mRequestQueue != null) {
            mRequestQueue.stop();
            mRequestQueue = null;
        }
        if (mDataManager != null) {
            mDataManager = null;
        }
    }

}
