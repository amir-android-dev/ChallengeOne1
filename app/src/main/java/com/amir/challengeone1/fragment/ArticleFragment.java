package com.amir.challengeone1.fragment;


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


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.amir.challengeone1.ListServiceImpl;
import com.amir.challengeone1.R;
import com.amir.challengeone1.adapter.ArticleAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tailoredapps.codingschool.challenge1.ListService;
import com.tailoredapps.codingschool.challenge1.ShoppingList;
import com.tailoredapps.codingschool.challenge1.ShoppingListEntry;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;


public class ArticleFragment extends BaseFragment {
    FloatingActionButton fb;
    RecyclerView rvChecked;
    RecyclerView rvNotChecked;

    SharedPreferences sharedPreferences;
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



    }

    private void setupUI(View view) {
        fb = view.findViewById(R.id.fb_article_fragment);
        rvChecked = view.findViewById(R.id.rv_bought);
        rvNotChecked = view.findViewById(R.id.rv_not_bought_article_fragment);
        dialog = new Dialog(requireContext());


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogToAddAnewEntry();
            }
        });
    }


    private void setupAdapterForCheckedArticle() {
        adapterChecked = new ArticleAdapter(requireContext(), checked(), new ArticleAdapter.CallBack() {
            @Override
            public void articleIsChecked(ShoppingListEntry shoppingListEntry) {

                unChecked().add(shoppingListEntry);
                checked().remove(shoppingListEntry);
                shoppingListEntry.setChecked(false);

                for (int i = 0; i < entriesChecked.size(); i++) {
                    if (!shoppingListEntry.isChecked() && shoppingListEntry.getId().equals(entriesChecked.get(i).getId())) {

                        listService.mCheckedEntry(getDataFromShoppingListInFragmentArticle().getId(), i, entriesUnChecked, entriesChecked);
                        adapterChecked.getUpdateEntry(entriesChecked);
                        adapterUnchecked.getUpdateEntry(entriesUnChecked);
                        runnableUnchecked();


                      //  break;
                    }

                }
            }
        });
        rvChecked.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rvChecked.setAdapter(adapterChecked);

    }

    private void setupAdapterForUnCheckedArticle() {

        adapterUnchecked = new ArticleAdapter(requireContext(), unChecked(), new ArticleAdapter.CallBack() {

            @Override
            public void articleIsChecked(ShoppingListEntry shoppingListEntry) {

                shoppingListEntry.setChecked(true);
                checked().add(shoppingListEntry);
                unChecked().remove(shoppingListEntry);

                for (int i = 0; i < entriesUnChecked.size(); i++) {
                    if (shoppingListEntry.isChecked() && shoppingListEntry.getId().equals(entriesUnChecked.get(i).getId())) {

                        listService.mUncheckedEntry(getDataFromShoppingListInFragmentArticle().getId(), i, entriesUnChecked, entriesChecked);
                        adapterUnchecked.getUpdateEntry(entriesUnChecked);
                        adapterChecked.getUpdateEntry(entriesChecked);
                        runnableUnchecked();
                      //  break;
                    }
                }
            }
        });
        rvNotChecked.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rvNotChecked.setAdapter(adapterUnchecked);
    }
    public void runnableUnchecked() {
        rvChecked.post(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                adapterChecked.notifyDataSetChanged();
                adapterUnchecked.notifyDataSetChanged();
            }
        });
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


    void dialogToAddAnewEntry() {
        TextInputEditText et;
        MaterialButton btnCancel;
        MaterialButton btnSave;

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
                    listService.mAddEntry(getDataFromShoppingListInFragmentArticle().getId(), entriesUnChecked);


                    displayToast(requireContext(), name+"  "+getString(R.string.is_saved));

                    dialog.dismiss();
                    adapterUnchecked.notifyDataSetChanged();
                }
            }
        });
        dialog.create();
        dialog.show();
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
            Timber.tag("TAG").e("meet a IOOBE in RecyclerView");
        }
    }


}