package com.amir.challengeone1.fragment;


import static com.amir.challengeone1.Constants.SHARED_PREFERENCES_MAIN;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.amir.challengeone1.ListServiceImpl;
import com.amir.challengeone1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.tailoredapps.codingschool.challenge1.ListService;
import com.tailoredapps.codingschool.challenge1.ShoppingList;
import com.tailoredapps.codingschool.challenge1.Utilities;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import timber.log.Timber;
import yuku.ambilwarna.AmbilWarnaDialog;


public class CreateShoppingListFragment extends BaseFragment {
    int mDefaultColor;
    SharedPreferences sharedPreferences;
    ListServiceImpl listService;
    int url;


    AppCompatImageView ivIcon;
    TextInputEditText etTitle;
    AppCompatButton btnChooseColor;
    AppCompatButton btnSave;
    AppCompatButton btnUpdate;
    TextView tvUUID;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI(view);
        randomIcon();
        getAndSetDataFromMainShoppingListFragment();
        setupActionbar();

    }

    private void setupUI(View view) {
        ivIcon = view.findViewById(R.id.iv_icon_new_category);
        etTitle = view.findViewById(R.id.et_title_new_category);
        btnChooseColor = view.findViewById(R.id.btn_choose_color_new_category);
        btnSave = view.findViewById(R.id.btn_save_new_category);
        btnUpdate = view.findViewById(R.id.btn_update_new_category);
        tvUUID = view.findViewById(R.id.tvuuid);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etTitle.getText().toString().isEmpty() && ivIcon.getResources() != null) {

                    listService.add(etTitle.getText().toString(), randomIcon(), mDefaultColor);
                    goBackToMainFragment();

                } else {
                    Toast.makeText(requireContext(), "empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UUID id = getDataFromShoppingListFragment().getId();
                List<ShoppingList> listToUpdate = listService.shoppingLists(ListService.SortOrder.Alphabetical);

                for (ShoppingList shoppingList : listService.shoppingLists(ListService.SortOrder.Alphabetical)) {
                    if (shoppingList.getId().equals(id)) {
                        displayToast(requireContext(), "update");

                        listToUpdate.remove(shoppingList);

                        String newList = Utilities.listOfShoppingListsToString(listToUpdate);
                        listService.editShoppingList(newList);
                        goBackToMainFragment();
                    }
                }
                listService.add(etTitle.getText().toString(), randomIcon(), mDefaultColor);
            }
        });

        btnChooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
    }



    private void getAndSetDataFromMainShoppingListFragment() {
        UUID id = getDataFromShoppingListFragment().getId();
        etTitle.setText(getDataFromShoppingListFragment().getName());
        ivIcon.setImageResource(getDataFromShoppingListFragment().getIcon());
        ivIcon.setColorFilter(getDataFromShoppingListFragment().getColor());
        mDefaultColor = getDataFromShoppingListFragment().getColor();
        url = getDataFromShoppingListFragment().getIcon();
        Timber.e("UUID: %s", getDataFromShoppingListFragment().getId());

        List<ShoppingList> list = listService.shoppingLists(ListService.SortOrder.Alphabetical);

        for (ShoppingList shoppingList : list) {
            if (shoppingList.getId().equals(id)) {
                btnSave.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
            }
        }
    }



    private void openColorPicker() {
        mDefaultColor = ContextCompat.getColor(requireContext(), R.color.purple_200);
        Timber.e("onOk: %s", mDefaultColor);
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(requireContext(), mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                ivIcon.setColorFilter(mDefaultColor);
                btnChooseColor.setBackgroundColor(mDefaultColor);
                Timber.e("onOk: %s", mDefaultColor);
            }
        });
        colorPicker.show();
    }

    private int randomIcon() {
        int[] icons = {R.drawable.ic_tablet_android_24, R.drawable.ic_shopping_bag_24, R.drawable.ic_hardware_24, R.drawable.ic_flatware_24, R.drawable.ic_fitness_center_24, R.drawable.ic_audio_file_24, R.drawable.ic_baseline_menu_book_24, R.drawable.ic_flatware_24};
        Random ran = new Random();

        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = ran.nextInt(icons.length);
                ivIcon.setImageResource(icons[k]);
                ivIcon.setTag(icons[k]);
                url = Integer.parseInt(ivIcon.getTag().toString());
                Timber.e("onOk: %s", url);
            }
        });

        return url;
    }


    private void setupActionbar() {
        setupBackButtonOnToolbar("Create a new List", true);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == android.R.id.home) {
                    goBackToMainFragment();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }


}