package com.kilagbe.kilagbe.tools;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kilagbe.kilagbe.R;

import java.util.ArrayList;

public class BookRecycleViewAdapter extends RecyclerView.Adapter<BookRecycleViewAdapter.ViewHolder> {

    private static final String TAG = "BookRecycleViewAdapter";


//  TODO: get the objects of Book class with their images
    private ArrayList<String> names = new ArrayList<>();


    private Context mContext;
    private OnBookListener mOnBookListener;

    public BookRecycleViewAdapter(Context mContext, ArrayList<String> names, OnBookListener onBookListener ) {
        this.mContext = mContext;
        this.names = names;
        this.mOnBookListener = onBookListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_book, parent, false);
        return new ViewHolder(view, mOnBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.textView.setText(names.get(position));

        /*
//      onClickListener
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on textview from recycle view: " + names.get(position));
                Toast.makeText(mContext, names.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return names.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        OnBookListener onBookListener;

        public ViewHolder(@NonNull View itemView, OnBookListener onBookListener) {
            super(itemView);
            this.onBookListener = onBookListener;

            textView = itemView.findViewById(R.id.book_name_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBookListener.onBookClick(names.get(getAdapterPosition()));
        }
    }


    public interface OnBookListener {
        void onBookClick(String name);
    }

}



