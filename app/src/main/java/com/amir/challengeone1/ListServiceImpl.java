package com.amir.challengeone1;


import static com.amir.challengeone1.Constants.SHARED_PREFERENCES_LIST;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import com.tailoredapps.codingschool.challenge1.ListService;
import com.tailoredapps.codingschool.challenge1.ShoppingList;
import com.tailoredapps.codingschool.challenge1.ShoppingListEntry;
import com.tailoredapps.codingschool.challenge1.Utilities;
import java.util.List;
import java.util.UUID;


import timber.log.Timber;

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

    public void removeShoppingList(String newUpdatedList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SHARED_PREFERENCES_LIST);
        editor.putString(SHARED_PREFERENCES_LIST, newUpdatedList);
        editor.commit();
    }


    @Nullable
    @Override
    public ShoppingList shoppingList(UUID listId) {
        return null;
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

        Timber.e("onOk: " + "" + name + "" + icon + "" + color);
    }


    @Override
    public void remove(UUID listId) {
        editor = sharedPreferences.edit();
        List<ShoppingList> lists = shoppingLists(SortOrder.Alphabetical);

        for (ShoppingList shoppingList : lists) {
            if (shoppingList.getId().equals(listId)) {

                ShoppingList shoppingList1 = new ShoppingList(shoppingList.getId(), shoppingList.getName(), shoppingList.getIcon(), shoppingList.getColor(), shoppingList.getCheckedEntries(), shoppingList.getUncheckedEntries());
                Timber.tag("RAMOVE").e("HUURRAA");
                lists.remove(shoppingList1);
            }
        }
    }


    @Override
    public void addEntry(UUID listId, String name) {

    }

    public void mAddEntry(UUID listId, List<ShoppingListEntry> entriesUnChecked) {
        editor = sharedPreferences.edit();
        List<ShoppingList> listToUpdate = shoppingLists(ListService.SortOrder.Alphabetical);

        for (ShoppingList shoppingList : listToUpdate) {
            if (shoppingList.getId().equals(listId)) {
                shoppingList.setUncheckedEntries(entriesUnChecked);
                String shopToString = Utilities.listOfShoppingListsToString(listToUpdate);
                editor.putString(SHARED_PREFERENCES_LIST, shopToString);
            }
        }
        editor.commit();
    }

    public void mUncheckedEntry(UUID id,int row,List<ShoppingListEntry>entriesUnChecked,List<ShoppingListEntry>entriesChecked) {
        List<ShoppingList> list = shoppingLists(ListService.SortOrder.Alphabetical);
        editor = sharedPreferences.edit();
        for(ShoppingList shoppingList:list) {
            if (shoppingList.getId().equals(id)) {
                if (shoppingList.getId().equals(id)) {
                    entriesUnChecked.remove(row);
                    shoppingList.setUncheckedEntries(entriesUnChecked);
                    shoppingList.setCheckedEntries(entriesChecked);
                    String shopInString = Utilities.listOfShoppingListsToString(list);
                    editor.putString(SHARED_PREFERENCES_LIST, shopInString);
                }
                editor.commit();
            }
        }
    }

    public void mCheckedEntry(UUID id, int row,List<ShoppingListEntry>entriesUnChecked,List<ShoppingListEntry> entriesChecked) {
        List<ShoppingList> list = shoppingLists(ListService.SortOrder.Alphabetical);
        editor = sharedPreferences.edit();
        for(ShoppingList shoppingList:list) {
            if (shoppingList.getId().equals(id)) {
                if (shoppingList.getId().equals(id)) {
                    entriesChecked.remove(row);
                    shoppingList.setCheckedEntries(entriesChecked);
                    shoppingList.setUncheckedEntries(entriesUnChecked);
                    String shopInString = Utilities.listOfShoppingListsToString(list);
                    editor.putString(SHARED_PREFERENCES_LIST, shopInString);
                }
                editor.commit();
            }
        }
    }


    @Override
    public void checkEntry(UUID listId, int row) {
    }

    @Override
    public void uncheckEntry(UUID listId, int row) {}

    @Override
    public void uncheckAllEntries(UUID listId) {
    }

    private void updatedShoppingLists(List<ShoppingList> shoppingLists) {
        String shoppingListsString = Utilities.listOfShoppingListsToString(shoppingLists);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_LIST, shoppingListsString);
        editor.commit();
    }

    @Override
    public void changeName(UUID listId, String name) {
        List<ShoppingList> shops = shoppingLists(SortOrder.Alphabetical);


        for (ShoppingList shoppingList : shops) {
            if (listId.equals(shoppingList.getId())) {
                shoppingList.setName(name);
            }
        }
        updatedShoppingLists(shops);

    }

    @Override
    public void changeIcon(UUID listId, int icon) {
        List<ShoppingList> shops = shoppingLists(SortOrder.Alphabetical);
        for (ShoppingList shoppingList : shops) {
            if (listId.equals(shoppingList.getId())) {
                shoppingList.setIcon(icon);
            }
        }
        updatedShoppingLists(shops);
    }

    @Override
    public void changeColor(UUID listId, int color) {
        List<ShoppingList> shops = shoppingLists(SortOrder.Alphabetical);
        for (ShoppingList shoppingList : shops) {
            if (listId.equals(shoppingList.getId())) {
                shoppingList.setColor(color);
            }
        }
        updatedShoppingLists(shops);

    }


}
