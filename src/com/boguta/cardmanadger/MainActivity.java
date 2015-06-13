package com.boguta.cardmanadger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.boguta.cardmanadger.model.Board;
import com.boguta.cardmanadger.model.Card;
import com.boguta.cardmanadger.model.TrelloList;

public class MainActivity extends Activity {
    private TextView mResponseTv;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectionManager manager = new ConnectionManager(this);
        mResponseTv = (TextView) findViewById(R.id.response);
        if (!manager.syncData(mOnSyncFinishedCallback)) {
            mResponseTv.setText("Network unavailable");
        }
    }

    private OnSyncFinishedCallback mOnSyncFinishedCallback = new OnSyncFinishedCallback() {

        @Override
        public void onSyncSuccess(Board board) {
            mDataManager = new DataManager(MainActivity.this, board);
            StringBuilder s = new StringBuilder();
            s.append(board.getName() + "\n\n");
            for (TrelloList list : board.getLists()) {
                s.append("\tList " + list.getName() + "\n");
                for (Card card : list.getCards()) {
                    s.append("\t\tcard " + card.getName() + "\n");
                }
            }
            mResponseTv.setText(s.toString());
        }

        @Override
        public void onSyncFail(VolleyError error) {
            mResponseTv.setText("Sync failed + " + error.getMessage());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
