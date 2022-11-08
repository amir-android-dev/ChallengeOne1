package com.amir.challengeone1.fragment;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.amir.challengeone1.MainActivity;

import java.util.UUID;

public class BaseFragment extends Fragment {

    public void displayToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public void setupBackButtonOnToolbar(String title, Boolean setDiaplayhome) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(setDiaplayhome);
            actionBar.setTitle(title);
        }
    }

    //Shoppinglist fragment
    public void navigateAndSendDataToCreateShoppingList(UUID id, String name, int icon, int color) {
        NavDirections action = MainShoppingListFragmentDirections.actionMainShoppingListFragmentToCreateShoppingListFragment(id, name, icon, color);
        Navigation.findNavController(requireView()).navigate(action);
    }


    //create
    public CreateShoppingListFragmentArgs getDataFromShoppingListFragment() {
        return CreateShoppingListFragmentArgs.fromBundle(getArguments());
    }

    //article
    public ArticleFragmentArgs getDataFromShoppingListInFragmentArticle(){
       return  ArticleFragmentArgs.fromBundle(getArguments());

    }

    public void goBackToMainFragment(){
        Navigation.findNavController(requireView()).popBackStack();
    }


}
