package com.boguta.cardmanadger;

public final class Constants {
    private Constants() throws InstantiationException {
        throw new InstantiationException();
    }

    public static final String TRELLO_KEY = "eb9a9982249612295e566b71a3925626";
    public static final String TRELLO_URL = "https://api.trello.com";
    public static final String TRELLO_API_VER = "/1";
    public static final String TRELLO_BOARDS_KEY = "/boards";
    public static final String TRELLO_CARDS_KEY = "/cards";
    public static final String TRELLO_ACTIONS_KEY = "/actions";
    public static final String TRELLO_CHECKLISTS_KEY = "/checklists";
    public static final String TRELLO_LABELS_KEY = "/labels";
    public static final String TRELLO_LISTS_KEY = "/lists";

}
