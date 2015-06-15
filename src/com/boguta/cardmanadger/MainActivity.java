package com.boguta.cardmanadger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boguta.cardmanadger.ListAdapter.ViewHolder;
import com.boguta.cardmanadger.model.Board;
import com.boguta.cardmanadger.model.Card;

public class MainActivity extends Activity {
    private DataManager mDataManager;
    private ConnectionManager mConnectionManager;
    private ListView mTodoList;
    private ListView mDoingList;
    private ListView mDoneList;
    private ListAdapter mTodoAdapter;
    private ListAdapter mDoingAdapter;
    private ListAdapter mDoneAdapter;
    private TextView mStatus;
    private OnTouchListenerImpl mTouchListener;
    private ViewGroup mListContainer;
    private ImageView mRecycleBin;
    private ViewGroup mTopContainer;
    private boolean mWorkInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnectionManager = new ConnectionManager(this);
        ((CardManagerApplication) getApplication()).setConnectionManager(mConnectionManager);
        mStatus = (TextView) findViewById(R.id.status);
        mTopContainer = (ViewGroup) findViewById(R.id.top_container);
        mRecycleBin = (ImageView) findViewById(R.id.recycle);
        if (!mConnectionManager.syncData(mBoardCallback)) {
            mStatus.setText(Constants.NETWORK_OFF);
            createToast(Constants.NETWORK_OFF);
            setButtonsEnabled(true);
        } else {
            mStatus.setText("Loading data..");
        }
    }

    private void initData(Board board) {
        mDataManager = new DataManager(MainActivity.this, board, mConnectionManager);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());
        Date date = new Date(System.currentTimeMillis());
        mStatus.setText("Data synced at " + format.format(date));
        ((CardManagerApplication) getApplication()).setDataManager(mDataManager);
        initViews(board);
    }

    private void initViews(Board board) {
        mListContainer = (ViewGroup) findViewById(R.id.list_container);
        mTodoList = (ListView) findViewById(R.id.todo_list);
        mDoingList = (ListView) findViewById(R.id.doing_list);
        mDoneList = (ListView) findViewById(R.id.done_list);

        mTouchListener = new OnTouchListenerImpl();

        mTopContainer.setOnDragListener(mTouchListener);
        mTodoAdapter = new ListAdapter(this, board.getListByName(Constants.TODO_LIST),
                mTouchListener);
        mTodoList.setAdapter(mTodoAdapter);
        mDoingAdapter = new ListAdapter(this, board.getListByName(Constants.DOING_LIST),
                mTouchListener);
        mDoingList.setAdapter(mDoingAdapter);
        mDoneAdapter = new ListAdapter(this, board.getListByName(Constants.DONE_LIST),
                mTouchListener);
        mDoneList.setAdapter(mDoneAdapter);

        mListContainer.setOnDragListener(mTouchListener);
        mTodoList.setOnDragListener(mTouchListener);
        mDoingList.setOnDragListener(mTouchListener);
        mDoneList.setOnDragListener(mTouchListener);
        TextView todoHeader = new TextView(this);
        todoHeader.setText(Constants.TODO_LIST);
        TextView doingHeader = new TextView(this);
        doingHeader.setText(Constants.DOING_LIST);
        TextView doneHeader = new TextView(this);
        doneHeader.setText(Constants.DONE_LIST);
        if (mTodoList.getHeaderViewsCount() == 0) {
            mTodoList.addHeaderView(todoHeader);
            mDoingList.addHeaderView(doingHeader);
            mDoneList.addHeaderView(doneHeader);
        }
        mListContainer.setVisibility(View.VISIBLE);
    }

    public class OnTouchListenerImpl implements OnDragListener, OnClickListener,
            OnLongClickListener {
        private View mDraggedView;
        private Card mDraggedCard;
        private int mDraggedPosition;

        @Override
        public void onClick(View v) {
            if (mWorkInProgress) {
                return;
            }
            if (v.getId() == R.id.list_element) {
                Card card = null;
                if (v.getTag() instanceof ViewHolder) {
                    ViewHolder holder = (ViewHolder) v.getTag();
                    card = holder.card;
                    mDraggedPosition = holder.position;
                    if (card != null) {
                        launchEditActivity(card);
                    }
                }
            }
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                if (v.getId() == R.id.recycle || v.getId() == R.id.top_container) {
                    performRemove(v);
                } else if (v.getId() == R.id.list_element && v != mDraggedView) {
                    if (v.getTag() instanceof ViewHolder) {
                        ViewHolder holder = (ViewHolder) v.getTag();
                        Card targetCard = holder.card;
                        int targetPosition = holder.position;
                        setButtonsEnabled(false);
                        mDataManager.moveCard(new OnRequestFinishedCallback<String>() {

                            @Override
                            public void onSuccess(String t) {
                                notifyAdapters();
                                setButtonsEnabled(true);
                            }

                            @Override
                            public void onFail(VolleyError error) {
                                createToast("Card move failed");
                                setButtonsEnabled(true);
                            }
                        }, mDraggedCard, targetCard.getIdList(), targetPosition,
                                (mDraggedPosition > targetPosition));
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                mRecycleBin.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
            }
            return true;
        }

        private void performRemove(View v) {
            setButtonsEnabled(false);
            mDataManager.removeCard(new OnRequestFinishedCallback<String>() {

                @Override
                public void onSuccess(String t) {
                    createToast("\"" + mDraggedCard.getName() + "\" removed!");
                    notifyAdapters();
                    setButtonsEnabled(true);
                }

                @Override
                public void onFail(VolleyError error) {
                    setButtonsEnabled(true);
                    createToast("Remove failed");
                }
            }, mDraggedCard);
        }

        @Override
        public boolean onLongClick(View v) {
            if (mWorkInProgress) {
                return false;
            }
            if (v.getId() == R.id.list_element && v.getTag() instanceof ViewHolder) {
                DragShadowBuilder builder = new DragShadowBuilder(v);
                mRecycleBin.setVisibility(View.VISIBLE);
                mDraggedView = v;
                ViewHolder holder = (ViewHolder) v.getTag();
                mDraggedPosition = holder.position;
                mDraggedCard = holder.card;
                return v.startDrag(null, builder, v, 0);
            }
            return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        notifyAdapters();
    }

    private void notifyAdapters() {
        if (mTodoAdapter != null && mDoingAdapter != null && mDoneAdapter != null) {
            mTodoAdapter.notifyDataSetChanged();
            mDoingAdapter.notifyDataSetChanged();
            mDoneAdapter.notifyDataSetChanged();
        }
    }

    private OnRequestFinishedCallback<Board> mBoardCallback = new OnRequestFinishedCallback<Board>() {

        @Override
        public void onSuccess(Board board) {
            initData(board);
            setButtonsEnabled(true);
        }

        @Override
        public void onFail(VolleyError error) {
            setButtonsEnabled(true);
            mStatus.setText("Sync failed + " + error.getMessage());
        }
    };

    private Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        setButtonsEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            if (mConnectionManager != null) {
                mConnectionManager = new ConnectionManager(this);
            }
            if (!mConnectionManager.syncData(mBoardCallback)) {
                createToast(Constants.NETWORK_OFF);
                return true;
            }
            setButtonsEnabled(false);
            return true;
        }
        if (id == R.id.action_add_item) {
            if (mConnectionManager.isConnected() && mDataManager != null) {
                launchEditActivity(null);
                return true;
            } else if (mConnectionManager.isConnected() && mDataManager == null){
                createToast("Refresh data first");
            } else {
                createToast(Constants.NETWORK_OFF);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchEditActivity(Card card) {
        Intent intent = new Intent(MainActivity.this, CardEditorActivity.class);
        if (card != null) {
            intent.putExtra("card", card);
        }
        startActivityForResult(intent, 0);
    }

    private void setButtonsEnabled(final boolean enabled) {
        if (mMenu != null) {
            mWorkInProgress = !enabled;
            mMenu.findItem(R.id.action_refresh).setEnabled(enabled);
            mMenu.findItem(R.id.action_add_item).setEnabled(enabled);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    setButtonsEnabled(enabled);
                }
            }, 100);
        }
    }

    private void createToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }
}
