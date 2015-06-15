package com.boguta.cardmanadger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boguta.cardmanadger.model.Card;

public class CardEditorActivity extends Activity {
    public static final int RESULT_OK = 0;
    public static final int RESULT_FAIL = 1;
    public static final int RESULT_CANCEL = 2;

    private Button mDueButton;
    private TextView mDueText;
    private EditText mEditName;
    private Button mSaveButton;
    private Button mCancelButton;
    private Card mCard;
    private DataManager mDataManager;
    private String mDue;
    private boolean mIsEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra("card")) {
            mCard = (Card) intent.getExtras().getSerializable("card");
            mIsEditMode = true;
            setTitle("Edit card: " + mCard.getName());
        } else {
            setTitle("Add new card");
        }
        mDataManager = ((CardManagerApplication) getApplication()).getDataManager();
        setContentView(R.layout.edit_activity);
        mDueButton = (Button) findViewById(R.id.due_button);
        mDueText = (TextView) findViewById(R.id.due_text);
        mEditName = (EditText) findViewById(R.id.name_edit);
        mSaveButton = (Button) findViewById(R.id.edit_save);
        mCancelButton = (Button) findViewById(R.id.edit_cancel);
        mDueButton.setOnClickListener(mOnClickListener);
        mSaveButton.setOnClickListener(mOnClickListener);
        mCancelButton.setOnClickListener(mOnClickListener);
        if (mIsEditMode) {
            mEditName.setText(mCard.getName());
            if (mCard.getDue() != null && !mCard.getDue().isEmpty()) {
                mDueText.setText(Utils.getReadableDate(mCard.getDue()));
            }
        } else {
            mCard = new Card();
        }

    }

    private OnClickListener mOnClickListener = new View.OnClickListener() {
        private Dialog mDialog;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.due_button:
                createDateTimePickerDialog();
                break;
            case R.id.dialog_save_button:
                saveNewDue(v);
                break;
            case R.id.dialog_delete_button:
                deleteDue(v);
                break;
            case R.id.edit_save:
                saveCard();
                break;
            case R.id.edit_cancel:
                if (getParent() != null) {
                    getParent().setResult(RESULT_CANCEL);
                }
                finish();
                break;
            }
        }

        private void saveCard() {
            String name = mEditName.getText().toString();
            if (name == null || "".equals(name)) {
                createToast("Name cant be empty");
                return;
            }
            if (mDue == null) {
                mDue = "";
            }
            mCard.setDue(mDue);
            mCard.setName(name.trim());
            if (mIsEditMode) {
                editCard();
            } else {
                addCard();
            }
        }

        private void addCard() {
            mDataManager.addCard(new OnRequestFinishedCallback<String>() {

                @Override
                public void onSuccess(String t) {
                    if (getParent() != null) {
                        Intent intent = new Intent();
                        intent.putExtra("card", mCard);
                        getParent().setResult(RESULT_OK, intent);
                    }
                    finish();
                }

                @Override
                public void onFail(VolleyError error) {
                    createToast("Save failed");
                }
            }, mCard);
        }

        private void editCard() {
            mDataManager.editCard(new OnRequestFinishedCallback<String>() {

                @Override
                public void onSuccess(String t) {
                    if (getParent() != null) {
                        Intent intent = new Intent();
                        intent.putExtra("card", mCard);
                        getParent().setResult(RESULT_OK, intent);
                    }
                    finish();
                }

                @Override
                public void onFail(VolleyError error) {
                    createToast("Save failed");
                }
            }, mCard);
        }

        private void saveNewDue(View v) {
            DatePicker datePicker = (DatePicker) mDialog.findViewById(R.id.date_picker);
            TimePicker timePicker = (TimePicker) mDialog.findViewById(R.id.time_picker);
            Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(),
                    datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker
                            .getCurrentMinute());
            SimpleDateFormat format = new SimpleDateFormat(Constants.TRELLO_DATE_PATTERN, Locale
                    .getDefault());
            mDue = format.format(calendar.getTime()) + "Z";
            mDialog.dismiss();
            mDialog = null;
        }

        private void deleteDue(View v) {
            mDialog.dismiss();
            mDialog = null;
            mDue = null;
        }

        private void createDateTimePickerDialog() {
            mDialog = new Dialog(CardEditorActivity.this);
            mDialog.setContentView(R.layout.date_time_picker);
            DatePicker datePicker = (DatePicker) mDialog.findViewById(R.id.date_picker);
            TimePicker timePicker = (TimePicker) mDialog.findViewById(R.id.time_picker);
            String curDue = mCard.getDue();
            Calendar calendar = Utils.getCalendar(curDue);
            int hours = 0;
            if (DateFormat.is24HourFormat(CardEditorActivity.this)) {
                timePicker.setIs24HourView(true);
                hours = calendar.get(Calendar.HOUR_OF_DAY);
            } else {
                hours = calendar.get(Calendar.HOUR);
                timePicker.setIs24HourView(false);
            }
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            timePicker.setCurrentHour(hours);
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
            Button saveButton = (Button) mDialog.findViewById(R.id.dialog_save_button);
            Button deleteButton = (Button) mDialog.findViewById(R.id.dialog_delete_button);
            saveButton.setOnClickListener(mOnClickListener);
            deleteButton.setOnClickListener(mOnClickListener);
            mDialog.show();
        }

    };

    @Override
    protected void onDestroy() {
        if (getParent() != null) {
            getParent().setResult(RESULT_CANCEL);
        }
        super.onDestroy();
    }

    private void createToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }
}
