package com.boguta.cardmanadger;

import android.app.Application;

public class CardManagerApplication extends Application {
    private DataManager mDataManager;
    private ConnectionManager mConnectionManager;

    public DataManager getDataManager() {
        return mDataManager;
    }

    public void setDataManager(DataManager manager) {
        mDataManager = manager;
    }
    
    public ConnectionManager getConnectionManager() {
        return mConnectionManager;
    }
    
    public void setConnectionManager(ConnectionManager manager) {
        mConnectionManager = manager;
    }
}
