package com.amir.challengeone1.fragment;

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
import androidx.navigation.Navigation;
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

import com.amir.challengeone1.ListServiceImpl;
import com.amir.challengeone1.R;
import com.amir.challengeone1.adapter.ArticleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tailoredapps.codingschool.challenge1.ListService;
import com.tailoredapps.codingschool.challenge1.ShoppingList;
import com.tailoredapps.codingschool.challenge1.ShoppingListEntry;
import com.tailoredapps.codingschool.challenge1.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ArticleFragment extends BaseFragment {
    FloatingActionButton fb;
    RecyclerView rvChecked;
    RecyclerView rvNotChecked;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ShoppingListEntry shoppingListEntry;
    ListServiceImpl listService;


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
            listService = new ListServiceImpl(sharedPreferences);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI(view);
        getSendDataFromMainShoppingFragment();
        setupAdapterForCheckedArticle();
        setupAdapterForUnCheckedArticle();
        setupActionbar();
      //  sendUncheckedSize();


    }

    private void setupUI(View view) {
        fb = view.findViewById(R.id.fb_article_fragment);
        rvChecked = view.findViewById(R.id.rv_bought);
        rvNotChecked = view.findViewById(R.id.rv_not_bought_article_fragment);
        dialog = new Dialog(requireContext());


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


//    private void setupAdapterForUnCheckedArticle() {
//        adapterUnchecked = new ArticleAdapter(requireContext(), unChecked(), new ArticleAdapter.CallBack() {
//
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
//    }

    private void setupAdapterForUnCheckedArticle() {
        List<ShoppingList> list = listService.shoppingLists(ListService.SortOrder.Alphabetical);
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
    }


    private List<ShoppingListEntry> unChecked() {
        entriesUnChecked = new ArrayList<>();

        List<ShoppingList> list = listService.shoppingLists(ListService.SortOrder.Alphabetical);

        for (ShoppingList shoppingList : list) {
            if (shoppingList.getId().equals(getDataFromShoppingListInFragmentArticle().getId()))
                entriesUnChecked.addAll(shoppingList.getUncheckedEntries());
        }

        return entriesUnChecked;
    }

    private List<ShoppingListEntry> checked() {
        entriesChecked = new ArrayList<>();
        List<ShoppingList> list = listService.shoppingLists(ListService.SortOrder.Alphabetical);

        for (ShoppingList shoppingList : list) {
            if (shoppingList.getId().equals(getDataFromShoppingListInFragmentArticle().getId()))
                entriesChecked.addAll(shoppingList.getCheckedEntries());
        }
        return entriesChecked;
    }


    private void getSendDataFromMainShoppingFragment() {
        id = getDataFromShoppingListInFragmentArticle().getId();
        title = getDataFromShoppingListInFragmentArticle().getTitle();
        icon = getDataFromShoppingListInFragmentArticle().getIcon();
        color = getDataFromShoppingListInFragmentArticle().getColor();
    }

//    private void sendUncheckedSize() {
//        Bundle bundle = new Bundle();
//        bundle.putInt("SIZE", 5);
//        Navigation.findNavController(requireView()).navigate(R.id.action_articleFragment_to_createShoppingListFragment, bundle);
//
//    }


    void dialog() {
        TextInputEditText et;
        Button btnCancel;
        Button btnSave;

        dialog.setContentView(R.layout.article_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        et = dialog.findViewById(R.id.et_enter_article_dialog);
        btnCancel = dialog.findViewById(R.id.btn_cancel_enter_article_dialog);
        btnSave = dialog.findViewById(R.id.btn_save_enter_article_dialog);

        btnCancel.setOnClickListener(new View.OnClickListener() {
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


                    shoppingListEntry = new ShoppingListEntry(UUID.randomUUID(), name, false);
                    entriesUnChecked.add(shoppingListEntry);
                    addEntry();
                    //    addList();
                    displayToast(requireContext(), name);

                    dialog.dismiss();
                    adapterUnchecked.notifyDataSetChanged();
                }
            }
        });
        dialog.create();
        dialog.show();
    }


    private void addEntry() {

        editor = sharedPreferences.edit();
        List<ShoppingList> listToUpdate = listService.shoppingLists(ListService.SortOrder.Alphabetical);

        for (ShoppingList shoppingList : listToUpdate) {
            if (shoppingList.getId().equals(id)) {
                shoppingList.setUncheckedEntries(entriesUnChecked);

                String shopToString = Utilities.listOfShoppingListsToString(listToUpdate);
                editor.putString(SHARED_PREFERENCES_LIST, shopToString);
            }
        }

        editor.commit();
    }


    private void setupActionbar() {
        setupBackButtonOnToolbar(getDataFromShoppingListInFragmentArticle().getTitle(), true);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.article_menu, menu);
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case android.R.id.home:
                        goBackToMainFragment();
                        return true;
                    case R.id.action_go_to_create_shopping:
                        navigateFromArticleFragmentToCreateShoppingList(getDataFromShoppingListInFragmentArticle().getId(), getDataFromShoppingListInFragmentArticle().getTitle(), getDataFromShoppingListInFragmentArticle().getIcon(), getDataFromShoppingListInFragmentArticle().getColor());
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