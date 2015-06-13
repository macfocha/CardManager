package com.boguta.cardmanadger;

public final class Constants {
    private Constants() throws InstantiationException {
        throw new InstantiationException();
    }

    public static final String TOKEN = "7d4f345b7f55a5e483fd7a51ec09ca3524aa645eb07bdfc587c22c3c753f4a8a";
    public static final String TRELLO_KEY = "eb9a9982249612295e566b71a3925626";
    public static final String AUTH_STRING = "?key=" + TRELLO_KEY + "&token=" + TOKEN;

    public static final String MY_BOARD_ID = "557ab970751f9e863abacd7b";

    public static final String TRELLO_MEMBER_ID = "/maciej168";
    public static final String TRELLO_URL = "https://trello.com";
    public static final String TRELLO_API_URL = "https://api.trello.com";
    public static final String TRELLO_API_VER = "/1";
    public static final String TRELLO_BOARDS = TRELLO_API_URL + TRELLO_API_VER + "/boards/";
    public static final String TRELLO_CARDS = TRELLO_API_URL + TRELLO_API_VER + "/cards/";
    public static final String TRELLO_LISTS = TRELLO_API_URL + TRELLO_API_VER + "/lists/";
    public static final String TRELLO_ACTIONS_KEY = "/actions";
    public static final String TRELLO_CHECKLISTS_KEY = "/checklists";
    public static final String TRELLO_MEMBERS_KEY = "/members";
    public static final String TRELLO_LABELS_KEY = "/labels";

    public static final String TRELLO_BOARD_NAME = "My_Board";

    public static final String TRELLO_USERNAME = "macfocha";
    public static final String TRELLO_PASSWORD = "azxs1423";
}
