package com.boguta.cardmanadger;

import android.content.Context;

public class ListManager {

    public static enum Type {
        TODO("To do"),
        DOING("Doing"),
        DONE("Done");

        private final String desc;

        public String getDesc() {
            return desc;
        }

        Type(String desc) {
            this.desc = desc;
        }
    }

    private final Type type;

    public ListManager(Context context, Type type) {
        this.type = type;
    }
}
