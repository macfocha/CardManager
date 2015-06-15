package com.boguta.cardmanadger;

public final class Constants {
    private Constants() throws InstantiationException {
        throw new InstantiationException();
    }

    public static final String TOKEN = "d2fcbfd3a026bb0f42adf7ddd759f257e95ce1a212f80a5b18662106c7767807";
    public static final String API_KEY = "eb9a9982249612295e566b71a3925626";
    public static final String AUTH_STRING = "?key=" + API_KEY + "&token=" + TOKEN;
    public static final String MY_BOARD_ID = "557ab970751f9e863abacd7b";
    public static final String TRELLO_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String READABLE_DATE_PATTERN = "yyyy-MM-dd HH:mm";

    public static final String MEMBER_ID = "/maciej168";
    public static final String URL = "https://trello.com";
    public static final String API_URL = "https://api.trello.com";
    public static final String API_VER = "/1";
    public static final String BOARDS = API_URL + API_VER + "/boards/";
    public static final String CARDS = API_URL + API_VER + "/cards/";
    public static final String LISTS = API_URL + API_VER + "/lists/";
    public static final String KEY_TOKEN = "token";
    public static final String BOARD_NAME = "My_Board";

    public static final String PREFS_FILE = "trello_prefs";
    public static final String TOP_POS = "top";
    public static final String BOTTOM_POS = "bottom";

    // card fields
    public static final String CARD_KEY_POS = "pos";
    public static final String CARD_KEY_ID = "id";
    public static final String CARD_KEY_NAME = "name";
    public static final String CARD_KEY_IDLIST = "idList";
    public static final String CARD_KEY_DUE = "due";
    public static final String CARD_KEY_URL_SOURCE = "urlSource";

    public static final int CARD_MOVE_SAFE_THRESHOLD = 1;

    public static final String TODO_LIST = "To do";
    public static final String DOING_LIST = "Doing";
    public static final String DONE_LIST = "Done";

    public static final String NETWORK_OFF = "Network unavailable";

}
