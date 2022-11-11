package com.amir.challengeone1;

import com.tailoredapps.codingschool.challenge1.ShoppingList;
import com.tailoredapps.codingschool.challenge1.ShoppingListEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Constants {

    public final static String SHARED_PREFERENCES_MAIN = "shared_preferences_main";
    public final static String SHARED_PREFERENCES_LIST = "SHARED_PREFERENCES_LIST";


    public static final String SAVE_INSTANCE_TITLE = "SAVE_INSTANCE_TITLE";


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
