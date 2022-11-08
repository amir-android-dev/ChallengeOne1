package com.amir.challengeone1;


import static com.amir.challengeone1.Constants.SHARED_PREFERENCES_LIST;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tailoredapps.codingschool.challenge1.ListService;
import com.tailoredapps.codingschool.challenge1.ShoppingList;
import com.tailoredapps.codingschool.challenge1.ShoppingListEntry;
import com.tailoredapps.codingschool.challenge1.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ListServiceImpl implements ListService {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ListServiceImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public List<ShoppingList> shoppingLists(SortOrder sortOrder) {
        String shoppingListInString = sharedPreferences.getString(SHARED_PREFERENCES_LIST, "");
        return Utilities.listOfShoppingListsFromString(shoppingListInString);
    }

    public void editShoppingList(String newUpdatedList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SHARED_PREFERENCES_LIST);
        editor.putString(SHARED_PREFERENCES_LIST,newUpdatedList);
        editor.commit();

    }

    @Nullable
    @Override
    public ShoppingList shoppingList(UUID listId) {
        return null;
    }

    public List<ShoppingList> loadShoppingList() {
        String shoppingListInString = sharedPreferences.getString(SHARED_PREFERENCES_LIST, "");
        return Utilities.listOfShoppingListsFromString(shoppingListInString);
    }

    @Override
    public void add(String name, int icon, int color) {
        List<ShoppingList> list = shoppingLists(SortOrder.Alphabetical);

        ShoppingList shoppingList = new ShoppingList(UUID.randomUUID(), name, icon, color);
        list.add(shoppingList);

        editor = sharedPreferences.edit();
        String listToString = Utilities.listOfShoppingListsToString(list);
        editor.putString(SHARED_PREFERENCES_LIST, listToString);
        editor.commit();

        Log.e("inside color", "onOk: " + "" + name + "" + icon + "" + color);
    }

    public void addShoppingListWithSecondConstrcutor(UUID id, List<ShoppingListEntry> unchecked, List<ShoppingListEntry> checked) {
        List<ShoppingList> list = shoppingLists(ListService.SortOrder.Alphabetical);
        for (ShoppingList shoppingList : list) {
            if (shoppingList.getId() == id) {
                list.remove(shoppingList);
                list.add(new ShoppingList(shoppingList.getId(), shoppingList.getName(), shoppingList.getIcon(), shoppingList.getColor(), unchecked, checked));
            }
        }
        String listInString = Utilities.listOfShoppingListsToString(list);

        editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_LIST, listInString);
        editor.commit();

    }

    @Override
    public void remove(UUID listId) {
        editor = sharedPreferences.edit();
        List<ShoppingList> lists = shoppingLists(SortOrder.Alphabetical);

        for (ShoppingList shoppingList : lists) {
            if (shoppingList.getId().equals(listId)) {
                ShoppingList shoppingList1 = new ShoppingList(shoppingList.getId(), shoppingList.getName(), shoppingList.getIcon(), shoppingList.getColor());
                Log.e("RAMOVE", "HUURRAA");
                lists.remove(shoppingList1);

            }
        }
    }

    //Saves the shopping list.
//This function does nothing if `listId` is invalid or `name` is empty.
//Params:
//listId – The id of the list that should have the entry added.
//name – The name of the entry.
    @Override
    public void addEntry(UUID listId, String name) {
        List<ShoppingList> lists = shoppingLists(SortOrder.Alphabetical);

        for (ShoppingList shoppingList : lists) {
            if (!listId.equals(shoppingList.getId()) && name.isEmpty()) {
                Log.e("ID_OR_NAME", "a proble, with id or name");
            } else {
                List<ShoppingListEntry> unchecked = new ArrayList<>();
                List<ShoppingListEntry> checked = new ArrayList<>();
                lists.add(new ShoppingList(shoppingList.getId(), shoppingList.getName(), shoppingList.getIcon(), shoppingList.getColor(), unchecked, checked));
            }
        }


    }

    @Override
    public void checkEntry(UUID listId, int row) {

    }

    @Override
    public void uncheckEntry(UUID listId, int row) {

    }

    @Override
    public void uncheckAllEntries(UUID listId) {

    }

    @Override
    public void changeName(UUID listId, String name) {

    }

    @Override
    public void changeIcon(UUID listId, int icon) {

    }

    @Override
    public void changeColor(UUID listId, int color) {

    }

//    public void addShoppingListWithSecondConstrcutor(UUID id, String name, int icon, int color, List<ShoppingListEntry> unchecked, List<ShoppingListEntry> checked) {
//        List<ShoppingList> list = shoppingLists(ListService.SortOrder.Alphabetical);
//        for (ShoppingList shoppingList : list) {
//            if (shoppingList.getId() == id) {
//                list.remove(shoppingList);
//                list.add(new ShoppingList(shoppingList.getId(), shoppingList.getName(), shoppingList.getIcon(),shoppingList.getColor(), unchecked, checked));
//            }
//        }
//        list.add(new ShoppingList(id, name, icon, color, unchecked, checked));
//        String listInString = Utilities.listOfShoppingListsToString(list);
//
//        editor = sharedPreferences.edit();
//        editor.putString(SHARED_PREFERENCES_LIST, listInString);
//        editor.commit();
//
//    }
}
