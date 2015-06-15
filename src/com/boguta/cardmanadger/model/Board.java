package com.boguta.cardmanadger.model;

import java.util.List;

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

    public TrelloList getListByName(String name) {
        for (TrelloList list : lists) {
            if (list.getName().equals(name)) {
                return list;
            }
        }
        return null;
    }

    public TrelloList getListById(String id) {
        for (TrelloList list : lists) {
            if (list.getId().equals(id)) {
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
