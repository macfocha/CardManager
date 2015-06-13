package com.boguta.cardmanadger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManager {
    private Context mContext;
    private DataManager mDataRetriever;

    public ConnectionManager(Context context) {
        mContext = context;
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public boolean syncData(OnSyncFinishedCallback callback) {
        if (isConnected()) {
            new DataSyncer(mContext, callback);
            return true;
        }
        return false;
    }

}
