package com.amir.challengeone1.fragment;

import static com.amir.challengeone1.Constants.SHARED_PREFERENCES_ARTICLE_CHECKED_LIST;
import static com.amir.challengeone1.Constants.SHARED_PREFERENCES_CHECKED;
import static com.amir.challengeone1.Constants.SHARED_PREFERENCES_LIST;
import static com.amir.challengeone1.Constants.SHARED_PREFERENCES_MAIN;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;

import com.amir.challengeone1.R;
import com.amir.challengeone1.adapter.ArticleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tailoredapps.codingschool.challenge1.ShoppingList;
import com.tailoredapps.codingschool.challenge1.ShoppingListEntry;
import com.tailoredapps.codingschool.challenge1.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class ArticleFragment extends BaseFragment {
    FloatingActionButton fb;
    RecyclerView rvChecked;
    RecyclerView rvNotChecked;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ShoppingListEntry shoppingListEntry;


    List<ShoppingListEntry> entriesUnChecked;
    List<ShoppingListEntry> entriesChecked;


    UUID id;
    String title;
    int icon;
    int color;

    String name;

    ArticleAdapter adapterChecked;
    ArticleAdapter adapterUnchecked;
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES_MAIN, Context.MODE_PRIVATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI(view);
        setupAdapterForCheckedArticle();
        setupAdapterForUnCheckedArticle();
        setupActionbar();


    }

    private void setupUI(View view) {
        fb = view.findViewById(R.id.fb_article_fragment);
        rvChecked = view.findViewById(R.id.rv_bought);
        rvNotChecked = view.findViewById(R.id.rv_not_bought_article_fragment);
        dialog = new Dialog(requireContext());


        for (ShoppingList shoppingList : loadShoppingList()) {
            Log.e("checke", shoppingList.getUncheckedEntries() + "");
        }

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
    }


    private void setupAdapterForCheckedArticle() {
        adapterChecked = new ArticleAdapter(requireContext(), checked(), new ArticleAdapter.CallBack() {
            @Override
            public void articleIsChecked(ShoppingListEntry shoppingListEntry) {
                for (int i = 0; i < entriesChecked.size(); i++) {
                    if (!shoppingListEntry.isChecked()) {
                        shoppingListEntry.setChecked(false);
                        entriesUnChecked.add(shoppingListEntry);
                        entriesChecked.remove(i);


//                        adapterChecked.notifyItemRangeChanged(i, entriesChecked.size());
//                        adapterUnchecked.notifyItemRangeChanged(i, entriesUnChecked.size());
//                        adapterChecked.notifyItemRemoved(i);
                        runnableChecked(i);

                        break;
                    }

                }
            }
        });
        rvChecked.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rvChecked.setAdapter(adapterChecked);

    }


    private void setupAdapterForUnCheckedArticle() {
        if (loadShoppingListEntry() == null) {
            adapterUnchecked = new ArticleAdapter(requireContext(), unChecked(), new ArticleAdapter.CallBack() {
                @Override
                public void articleIsChecked(ShoppingListEntry shoppingListEntry) {
                    for (int i = 0; i < entriesUnChecked.size(); i++) {
                        if (shoppingListEntry.isChecked()) {
                            entriesUnChecked.remove(i);
                            shoppingListEntry.setChecked(true);
                            entriesChecked.add(shoppingListEntry);
                            runnableUnchecked(i);
                            break;
                        }

                    }
                }
            });
            rvNotChecked.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            rvNotChecked.setAdapter(adapterUnchecked);
        } else {
            adapterUnchecked = new ArticleAdapter(requireContext(), loadShoppingListEntry(), new ArticleAdapter.CallBack() {
                @Override
                public void articleIsChecked(ShoppingListEntry shoppingListEntry) {
                    for (int i = 0; i < entriesUnChecked.size(); i++) {
                        if (shoppingListEntry.isChecked()) {
                            entriesUnChecked.remove(i);
                            shoppingListEntry.setChecked(true);
                            entriesChecked.add(shoppingListEntry);
                            runnableUnchecked(i);
                            break;
                        }

                    }
                }
            });
            rvNotChecked.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            rvNotChecked.setAdapter(adapterUnchecked);
        }
    }
//        adapterUnchecked = new ArticleAdapter(requireContext(), loadShoppingListEntry(), new ArticleAdapter.CallBack() {
//            @Override
//            public void articleIsChecked(ShoppingListEntry shoppingListEntry) {
//                for (int i = 0; i < entriesUnChecked.size(); i++) {
//                    if (shoppingListEntry.isChecked()) {
//                        entriesUnChecked.remove(i);
//                        shoppingListEntry.setChecked(true);
//                        entriesChecked.add(shoppingListEntry);
//                        runnableUnchecked(i);
//                        break;
//                    }
//
//                }
//            }
//        });
//        rvNotChecked.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
//        rvNotChecked.setAdapter(adapterUnchecked);


    //    private List<ShoppingListEntry> checked() {
//        entriesChecked = new ArrayList<>();
////        entriesChecked.add(new ShoppingListEntry(UUID.randomUUID(), "milk", true));
//        return entriesChecked;
//    }
//
    private List<ShoppingListEntry> unChecked() {
        entriesUnChecked = new ArrayList<>();
        entriesUnChecked.add(new ShoppingListEntry(UUID.randomUUID(), "banana", false));
        //entriesUnChecked.add(saveEntry());
        return entriesUnChecked;
    }

    private List<ShoppingListEntry> checked() {
        entriesChecked = new ArrayList<>();
        entriesChecked.add(new ShoppingListEntry(UUID.randomUUID(), "milk", true));
        return entriesChecked;
    }

//    private List<ShoppingListEntry> unChecked() {
//        entriesUnChecked = new ArrayList<>();
//       entriesUnChecked.add(new ShoppingListEntry(UUID.randomUUID(), "banana", false));
//
////        for (ShoppingList shoppingList:loadShoppingList()) {
////            if (shoppingList != null) {
////
////                entriesUnChecked = shoppingList.getUncheckedEntries();
////
////            }
////        }
//        return entriesUnChecked;
//    }

    private void getSendDataFromMainShoppingFragment() {
        id = getDataFromShoppingListInFragmentArticle().getId();
        title = getDataFromShoppingListInFragmentArticle().getTitle();
        icon = getDataFromShoppingListInFragmentArticle().getIcon();
        color = getDataFromShoppingListInFragmentArticle().getColor();
    }


    void dialog() {
        TextInputEditText et;
        Button btnCancle;
        Button btnSave;

        dialog.setContentView(R.layout.article_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.animation
        et = dialog.findViewById(R.id.et_enter_article_dialog);
        btnCancle = dialog.findViewById(R.id.btn_cancel_enter_article_dialog);
        btnSave = dialog.findViewById(R.id.btn_save_enter_article_dialog);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                name = et.getText().toString();
                if (name.isEmpty()) {
                    displayToast(requireContext(), "To save a new article please fill up the text field!");
                } else {
                    getSendDataFromMainShoppingFragment();

                    shoppingListEntry = new ShoppingListEntry(UUID.randomUUID(), name, false);

                    try {
                        saveEntry();
                        entriesUnChecked.add(saveEntry());
                        saveShoppingList();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    displayToast(requireContext(), name);
//                    adapterUnchecked.notifyDataSetChanged();
//                    adapterChecked.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        dialog.create();
        dialog.show();
    }

    private ShoppingListEntry saveEntry() {
        shoppingListEntry = new ShoppingListEntry(UUID.randomUUID(), name, false);
        return shoppingListEntry;
    }

    private void saveShoppingList() {
        getDataFromShoppingListInFragmentArticle();
        List<ShoppingList> list = loadShoppingList();

        list.add(new ShoppingList(id, title, icon, color, entriesChecked, entriesUnChecked));
        String listInString = Utilities.listOfShoppingListsToString(list);
        editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_CHECKED, listInString);
        editor.commit();
    }


    private List<ShoppingList> loadShoppingList() {
        String shoppingListToList = sharedPreferences.getString(SHARED_PREFERENCES_CHECKED, "");
        // List<ShoppingList> list
        return Utilities.listOfShoppingListsFromString(shoppingListToList);
    }

    private List<ShoppingListEntry> loadShoppingListEntry() {
        getDataFromShoppingListInFragmentArticle();
        //String shoppingListToList = sharedPreferences.getString(SHARED_PREFERENCES_CHECKED, "");
        // List<ShoppingList> list = Utilities.listOfShoppingListsFromString(shoppingListToList);
        List<ShoppingList> list = loadShoppingList();

        for (ShoppingList shoppingList : list) {
            Log.e("id", shoppingList.getId().toString() + "");
            Log.e("id", shoppingList.getUncheckedEntries().toString() + "");

            return shoppingList.getUncheckedEntries();

        }

        return null;
    }


    private void setupActionbar() {
        setupBackButtonOnToolbar(getDataFromShoppingListInFragmentArticle().getTitle(), true);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case android.R.id.home:
                        goBackToMainFragment();
                        return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }


    public void runnableUnchecked(int i) {
        rvChecked.post(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                adapterUnchecked.notifyItemRemoved(i);
//                adapterUnchecked.notifyItemRangeChanged(0, entriesUnChecked.size());
//                adapterUnchecked.notifyItemChanged(i);
                //adapterChecked.notifyItemRangeChanged(i, entriesChecked.size());
                adapterChecked.notifyDataSetChanged();
                // adapterUnchecked.notifyDataSetChanged();   some of checkbox are going to not be fill

            }
        });
    }

    public void runnableChecked(int i) {
        rvChecked.post(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                adapterChecked.notifyItemRemoved(i);
                //    adapterChecked.notifyItemRangeChanged(i, entriesChecked.size());
                //   adapterChecked.notifyItemChanged(i);
                adapterChecked.notifyDataSetChanged();
                //adapterUnchecked.notifyItemRangeChanged(i, entriesUnChecked.size());
                // adapterUnchecked.notifyDataSetChanged();     it makes duplicate after removing
            }
        });
    }
}

class WrapContentLinearLayoutManager extends LinearLayoutManager {
    public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    //... constructor
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("TAG", "meet a IOOBE in RecyclerView");
        }
    }


}