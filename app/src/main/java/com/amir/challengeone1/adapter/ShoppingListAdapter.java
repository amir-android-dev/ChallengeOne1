package com.amir.challengeone1.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.amir.challengeone1.R;
import com.amir.challengeone1.fragment.MainShoppingListFragmentDirections;
import com.tailoredapps.codingschool.challenge1.ShoppingList;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<MyShoppingViewHolder> {
    public interface Callback {
        void shoppingListClicked(ShoppingList shoppingList);
    }

    Callback callback;
    List<ShoppingList> shoppingLists;
    private final Context context;


    public ShoppingListAdapter(List<ShoppingList> shoppingLists, Context context, Callback callback) {
        this.shoppingLists = shoppingLists;
        this.context = context;
        this.callback = callback;
    }


    @NonNull
    @Override

    public MyShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.shopping_list_item, parent, false);
        return new MyShoppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyShoppingViewHolder holder, int position) {
        ShoppingList shoppingList = shoppingLists.get(position);
        try {
            holder.tvCategoryName.setText(shoppingList.getName());
            holder.ivIcon.setImageResource(shoppingList.getIcon());
            holder.ivIcon.setColorFilter(shoppingList.getColor());

            holder.tvCountOfUndone.setText(String.valueOf(shoppingList.getUncheckedEntries().size()));


        } catch (NullPointerException | Resources.NotFoundException e) {
            e.printStackTrace();
        }


        holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callback.shoppingListClicked(shoppingList);

                return true;
            }
        });

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = MainShoppingListFragmentDirections.actionMainShoppingListFragmentToArticleFragment(shoppingList.getId(), shoppingList.getName(), shoppingList.getIcon(), shoppingList.getColor());
                Navigation.findNavController(v).navigate(action);
            }
        });

        if ((position % 2) == 0) {
            holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.blue_100));
            holder.tvCategoryName.setTextColor(context.getResources().getColor(R.color.blue_900));
            holder.tvCountOfUndone.setTextColor(context.getResources().getColor(R.color.blue_900));
        }else{
            holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.blue_900));
            holder.tvCategoryName.setTextColor(context.getResources().getColor(R.color.blue_100));
            holder.tvCountOfUndone.setTextColor(context.getResources().getColor(R.color.blue_100));
        }

    }


    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }




    @SuppressLint("NotifyDataSetChanged")
    public void getUpdateShoppingList(List<ShoppingList> shoppingCards) {
        shoppingLists = shoppingCards;
        notifyDataSetChanged();
    }
}

class MyShoppingViewHolder extends RecyclerView.ViewHolder {
    AppCompatImageView ivIcon;
    TextView tvCategoryName;
    TextView tvCountOfUndone;
    CardView cv;
    ConstraintLayout constraintLayout;

    public MyShoppingViewHolder(@NonNull View itemView) {
        super(itemView);
        cv = itemView.findViewById(R.id.cv_main_list_item);
        ivIcon = itemView.findViewById(R.id.iv_main_list_item);
        tvCategoryName = itemView.findViewById(R.id.tv_title_main_list_item);
        tvCountOfUndone = itemView.findViewById(R.id.tv_count_main_list_item);
        constraintLayout = itemView.findViewById(R.id.constraint_main_list_item);


    }


}
