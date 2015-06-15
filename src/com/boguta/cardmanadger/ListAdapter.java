package com.boguta.cardmanadger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boguta.cardmanadger.MainActivity.OnTouchListenerImpl;
import com.boguta.cardmanadger.model.Card;
import com.boguta.cardmanadger.model.TrelloList;

public class ListAdapter extends BaseAdapter {
    private TrelloList mTrelloList;
    private LayoutInflater mInflater;
    private OnTouchListenerImpl mOnTouchListener;

    public ListAdapter(Context context, TrelloList list, OnTouchListenerImpl listener) {
        mTrelloList = list;
        mInflater = LayoutInflater.from(context);
        mOnTouchListener = listener;
    }

    @Override
    public int getCount() {
        return mTrelloList.getCards().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_element, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.card_name);
            holder.due = (TextView) convertView.findViewById(R.id.card_due);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Card card = mTrelloList.getCards().get(position);
        if (card != null) {
            holder.name.setText(card.getName());
            holder.card = card;
            String due = card.getDue();
            if (due != null && !due.isEmpty()) {
                holder.due.setText(Utils.getReadableDate(due));
            } else {
                holder.due.setText("");
            }
            holder.position = position;
            convertView.setOnClickListener(mOnTouchListener);
            convertView.setOnLongClickListener(mOnTouchListener);
            convertView.setOnDragListener(mOnTouchListener);
        }
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        
        super.notifyDataSetChanged();
    }
    
    public static class ViewHolder {
        TextView name;
        TextView due;
        Card card;
        int position;
    }

}
