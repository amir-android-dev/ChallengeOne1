package com.amir.challengeone1.fragment;


import static com.amir.challengeone1.Constants.SHARED_PREFERENCES_MAIN;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.amir.challengeone1.ListServiceImpl;
import com.amir.challengeone1.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tailoredapps.codingschool.challenge1.ListService;
import com.tailoredapps.codingschool.challenge1.ShoppingList;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import timber.log.Timber;
import yuku.ambilwarna.AmbilWarnaDialog;


public class CreateShoppingListFragment extends BaseFragment {
    int mDefaultColor;
    SharedPreferences sharedPreferences;
    ListServiceImpl listService;
    int urlOfIcon;


    AppCompatImageView ivIcon;
    TextInputEditText etTitle;
    MaterialButton btnChooseColor;
    MaterialButton btnSave;
    MaterialButton btnUpdate;


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


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etTitle.getText().toString().isEmpty() && ivIcon.getResources() != null) {

                    listService.add(etTitle.getText().toString(), randomIcon(), mDefaultColor);
                    goBackToMainFragment();

                } else {
                    displayToast(requireContext(), getString(R.string.fill_up_name));
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etTitle.getText().toString().isEmpty()) {
                    listService.changeName(getDataFromShoppingListFragment().getId(), etTitle.getText().toString());
                    listService.changeIcon(getDataFromShoppingListFragment().getId(), randomIcon());
                    listService.changeColor(getDataFromShoppingListFragment().getId(), mDefaultColor);

                    goBackToMainFragment();
                } else {
                    displayToast(requireContext(), getString(R.string.fill_up_name));
                }

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
        urlOfIcon = getDataFromShoppingListFragment().getIcon();
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
        mDefaultColor = ContextCompat.getColor(requireContext(), R.color.brown_200);
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
                urlOfIcon = Integer.parseInt(ivIcon.getTag().toString());
                Timber.e("onOk: %s", urlOfIcon);
            }
        });

        return urlOfIcon;
    }


    private void setupActionbar() {

        if (getDataFromShoppingListFragment().getName().isEmpty()) {
            setupBackButtonOnToolbar(getString(R.string.create_a_new_list), true);
        } else {
            setupBackButtonOnToolbar(    getResources().getString(R.string.edit)+ "  " + getDataFromShoppingListFragment().getName(), true);
        }


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