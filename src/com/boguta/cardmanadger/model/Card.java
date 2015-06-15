package com.boguta.cardmanadger.model;

import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable {
    private static final long serialVersionUID = -3707450450453250207L;

    private String id;
    private String idList;
    private String name;
    private String pos;
    private String due;
    private String urlSource;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getUrlSource() {
        return urlSource;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Card another) {
        double thisPos = Double.valueOf(pos);
        double anotherPos = Double.valueOf(another.getPos());
        return (int) (thisPos - anotherPos);
    }

}
