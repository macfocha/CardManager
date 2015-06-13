package com.boguta.cardmanadger.model;

import java.util.List;

import com.boguta.cardmanadger.DataManager.ListType;

public class Board {
    List<TrelloList> lists;
    private String id;
    private String name;
    public void setLists(List<TrelloList> response) {
        lists = response;
    }

    public List<TrelloList> getLists() {
        return lists;
    }

    public TrelloList getList(ListType type) {
        for (TrelloList list : lists) {
            if (list.getType() == type) {
                return list;
            }
        }
        return null;
    }
    
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
