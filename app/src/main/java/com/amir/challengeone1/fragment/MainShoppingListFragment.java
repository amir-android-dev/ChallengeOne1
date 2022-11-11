package com.amir.challengeone1.fragment;


import static com.amir.challengeone1.Constants.SHARED_PREFERENCES_MAIN;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amir.challengeone1.ListServiceImpl;

import com.amir.challengeone1.R;
import com.amir.challengeone1.adapter.ShoppingListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tailoredapps.codingschool.challenge1.ListService;
import com.tailoredapps.codingschool.challenge1.ShoppingList;
import com.tailoredapps.codingschool.challenge1.Utilities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;

public class MainShoppingListFragment extends BaseFragment {
    SharedPreferences sharedPreferences;
    ShoppingListAdapter shoppingListAdapter;
    ListServiceImpl listService;


    FloatingActionButton fbMainList;
    RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES_MAIN, Context.MODE_PRIVATE);
            listService = new ListServiceImpl(sharedPreferences);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI(view);
        setupShoppingListAdapter();
        setupActionBar();
    }

    private void setupUI(View view) {
        fbMainList = view.findViewById(R.id.fb_main_list);
        rv = view.findViewById(R.id.rv_main_list);
        listService = new ListServiceImpl(sharedPreferences);

        fbMainList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateAndSendDataToCreateShoppingList(UUID.randomUUID(), "", R.drawable.ic_shopping_bag_24, R.color.purple_200);
            }
        });

    }

    private void setupShoppingListAdapter() {

        shoppingListAdapter = new ShoppingListAdapter(loadShoppingList(), requireContext(), new ShoppingListAdapter.Callback() {
            @Override
            public void shoppingListClicked(ShoppingList shoppingList) {
                setupEditDeleteDialog(shoppingList.getId(), shoppingList.getName(), shoppingList.getIcon(), shoppingList.getColor());
                shoppingListAdapter.notifyDataSetChanged();
            }
        });

        rv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rv.setAdapter(shoppingListAdapter);
    }

    private List<ShoppingList> loadShoppingList() {
        return listService.shoppingLists(ListService.SortOrder.Alphabetical);
    }

    private void setupEditDeleteDialog(UUID id, String name, int icon, int color) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(name);
        builder.setItems(R.array.delete_and_edit, new DialogInterface.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(requireContext(), "Edit", Toast.LENGTH_LONG).show();
                        navigateAndSendDataToCreateShoppingList(id, name, icon, color);
                        break;
                    case 1:
                        List<ShoppingList> listToDelete = listService.shoppingLists(ListService.SortOrder.Alphabetical);

                        for (ShoppingList shoppingList : listService.shoppingLists(ListService.SortOrder.Alphabetical)) {
                            if (shoppingList.getId().equals(id))
                                listToDelete.remove(shoppingList);
                            String newList = Utilities.listOfShoppingListsToString(listToDelete);
                            listService.editShoppingList(newList);
                           shoppingListAdapter.getUpdateShoppingList(listToDelete);

                        }
                        break;
                }
            }
        });
        builder.create();
        builder.show();
    }



    private void setupActionBar() {
        setupBackButtonOnToolbar(getResources().getString(R.string.shopping_list), false);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.shopping_list_menu, menu);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_sort_by_alphabet:
                        displayToast(requireContext(), "alpha");
                        sortAlphabetical();
                        // sortAlphabet();
                        return true;
                    case R.id.action_sort_by_count:
                        displayToast(requireContext(), "count");
                        sortCount();
                        return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }


    void sortAlphabetical() {
       List<ShoppingList> shoppingLists = listService.shoppingLists(ListService.SortOrder.Alphabetical);
        Collections.sort(shoppingLists, new Comparator<ShoppingList>() {
            @Override
            public int compare(ShoppingList o1, ShoppingList o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (ShoppingList shoppingList : loadShoppingList()) {
            Timber.e("NAME%s", shoppingList.getName());
        }

        shoppingListAdapter.getUpdateShoppingList(shoppingLists);
    }

    void sortCount() {
        List<ShoppingList> shoppingLists = listService.shoppingLists(ListService.SortOrder.Alphabetical);
        Collections.sort(shoppingLists, new Comparator<ShoppingList>() {
            @Override
            public int compare(ShoppingList o1, ShoppingList o2) {
                return String.valueOf(o1.getUncheckedEntries().size()).compareTo(String.valueOf(o2.getUncheckedEntries().size()));
            }
        });
        shoppingListAdapter.notifyDataSetChanged();
        for (ShoppingList shoppingList : loadShoppingList()) {
            Timber.e("NAME%s", shoppingList.getName());
        }

        shoppingListAdapter.getUpdateShoppingList(shoppingLists);
    }

        //Comparator to sort alphabetical
    public static Comparator<ShoppingList> ShoppingListNameAZComparator = new Comparator<ShoppingList>() {
        @Override
        public int compare(ShoppingList shoppingList1, ShoppingList shoppingList2) {
            return shoppingList1.getName().compareTo(shoppingList2.getName());
        }
    };



}