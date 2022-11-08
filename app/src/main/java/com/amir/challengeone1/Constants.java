package com.amir.challengeone1;

import com.tailoredapps.codingschool.challenge1.ShoppingList;
import com.tailoredapps.codingschool.challenge1.ShoppingListEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Constants {

    public final static String SHARED_PREFERENCES_MAIN = "shared_preferences_main";
    public final static String SHARED_PREFERENCES_EDIT_LOAD_SHOPPING = "shared_preferences_edit_shopping";
    public final static String SHARED_PREFERENCES_LIST = "SHARED_PREFERENCES_LIST";
    public final static String SHARED_PREFERENCES_ENTRY = "SHARED_PREFERENCES_ENTRY";

    public final static String SHARED_PREFERENCES_ARTICLE_CHECKED_LIST = "SHARED_PREFERENCES_ARTICLE_CHECKED_LIST";
    public final static String SHARED_PREFERENCES_ARTICLE_NOT_CHECKED_LIST = "SHARED_PREFERENCES_ARTICLE_NOT_CHECKED_LIST";
    public final static String SHARED_PREFERENCES_CHECKED= "SHARED_PREFERENCES_CHECKED";


    public static final String ICON_DIALOG_TAG = "icon-dialog";




    public static final String SAVE_INSTANCE_TITLE = "SAVE_INSTANCE_TITLE";
    public static final String SAVE_INSTANCE_COLOR = "SAVE_INSTANCE_COLOR";
    public static final String SAVE_INSTANCE_ICON = "SAVE_INSTANCE_ICON";

//
//    private List<ShoppingListEntry> unChecked1() {
//        entriesUnChecked = new ArrayList<>();
//        entriesUnChecked.add(new ShoppingListEntry(UUID.randomUUID(), "milk", true));
//        return entriesUnChecked;
//    }
//    private List<ShoppingListEntry> checked() {
//        for(ShoppingList shoppingList:loadShoppingList()){
//            entriesChecked = shoppingList.getCheckedEntries();
//        }
//        return entriesChecked;
//    }
//
//    private List<ShoppingListEntry> unChecked() {
//        for (ShoppingList shoppingList:loadShoppingList()){
//
//            entriesUnChecked = shoppingList.getUncheckedEntries();
//        }
//
//        return entriesUnChecked;
//    }
}
