package com.amir.challengeone1.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.amir.challengeone1.R;

import com.google.android.material.textview.MaterialTextView;

import com.tailoredapps.codingschool.challenge1.ShoppingListEntry;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<MArticleViewHolder> {

    public interface CallBack {
        void articleIsChecked(ShoppingListEntry shoppingListEntry);
    }

    CallBack callBack;
    Context context;
    List<ShoppingListEntry> entryList;

    public ArticleAdapter(Context context, List<ShoppingListEntry> entryList, CallBack callBack) {
        this.context = context;
        this.entryList = entryList;
        this.callBack = callBack;
    }


    @NonNull
    @Override
    public MArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.article_item, parent, false);
        return new MArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MArticleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ShoppingListEntry entry = entryList.get(position);

        holder.tvArticle.setText(entry.getName());
        holder.rbCheck.setChecked(entry.isChecked());
        holder.rbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !entry.isChecked()) {
                    //set the isCheck function of object into true
                    entry.setChecked(true);
                    //when is checked send a new object of entry
                    callBack.articleIsChecked(entry);

               } else {
                    entry.setChecked(false);
                    callBack.articleIsChecked(entry);
                }
            }
        });
    }

    public void getUpdateEntry(List<ShoppingListEntry> entries) {
        entryList = entries;

    }


    @Override
    public int getItemCount() {
        return entryList.size();
    }
}

class MArticleViewHolder extends RecyclerView.ViewHolder {
    MaterialTextView tvArticle;
    AppCompatCheckBox rbCheck;

    public MArticleViewHolder(@NonNull View itemView) {
        super(itemView);

        tvArticle = itemView.findViewById(R.id.tv_article_item);
        rbCheck = itemView.findViewById(R.id.rb_article_item);
    }
}
