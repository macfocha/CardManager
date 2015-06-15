package com.boguta.cardmanadger;

import com.android.volley.VolleyError;

public interface OnRequestFinishedCallback<T> {
    public void onSuccess(T t);

    public void onFail(VolleyError error);
}
