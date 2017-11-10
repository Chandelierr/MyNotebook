package com.seewo.mynotebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seewo.mynotebook.R;
import com.seewo.mynotebook.model.Group;

import java.util.List;

/**
 * Created by 王梦洁 on 2017/11/9.
 */

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Group> mGroups;
    private LayoutInflater mInflater;
    private Context mContext;

    private NoteAdapter.OnItemClickListener mClickListener;

    public GroupAdapter(Context context,List<Group> groups) {
        mContext = context;
        mGroups = groups;
        mInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        public TextView groupItemNameTv;

        public myViewHolder(View itemView) {
            super(itemView);
            groupItemNameTv = (TextView) itemView.findViewById(R.id.group_item_name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.group_item, parent, false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((myViewHolder)holder).groupItemNameTv.setText(mGroups.get(position).getName());
        dealClickEvent(holder);
    }

    @Override
    public int getItemCount() {
        return mGroups != null ? mGroups.size() : 0;
    }

    private void dealClickEvent(final RecyclerView.ViewHolder holder) {
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
    }

    public void setOnItemClickListener(NoteAdapter.OnItemClickListener listener) {
        mClickListener = listener;
    }
}
