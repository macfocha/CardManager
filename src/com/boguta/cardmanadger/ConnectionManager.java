package com.boguta.cardmanadger;

import android.content.Context;

public class ConnectionManager {
    private Context mContext;

    public ConnectionManager(Context context) {
        mContext = context;
    }
    
    
    
    public boolean isNetworkAvailable() {
        return true;
    }
    
}
