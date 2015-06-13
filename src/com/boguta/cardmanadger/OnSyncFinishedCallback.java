package com.boguta.cardmanadger;

import com.android.volley.VolleyError;
import com.boguta.cardmanadger.model.Board;

public interface OnSyncFinishedCallback {
    public void onSyncSuccess(Board board);

    public void onSyncFail(VolleyError error);
}
