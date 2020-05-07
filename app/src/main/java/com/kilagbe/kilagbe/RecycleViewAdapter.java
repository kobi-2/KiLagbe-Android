package com.kilagbe.kilagbe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private static final String TAG = "RecycleViewAdapter";
    // variables
    private ArrayList<String> names = new ArrayList<>();
    private Context mContext;
    private OnCatListener mOnCatListener;

    public RecycleViewAdapter(Context mContext, ArrayList<String> names, OnCatListener onCatListener ) {
        this.mContext = mContext;
        this.names = names;
        this.mOnCatListener = onCatListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_layout, parent, false);
        return new ViewHolder(view, mOnCatListener);
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
        OnCatListener onCatListener;

        public ViewHolder(@NonNull View itemView, OnCatListener onCatListener) {
            super(itemView);
            this.onCatListener = onCatListener;

            textView = itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCatListener.onCatClick(names.get(getAdapterPosition()));
        }
    }


    public interface OnCatListener{
        void onCatClick(String name);
    }

}



