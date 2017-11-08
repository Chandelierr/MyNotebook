package com.seewo.mynotebook.adapter;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seewo.mynotebook.R;
import com.seewo.mynotebook.model.Note;

import java.util.List;
import java.util.Random;

/**
 * Created by 王梦洁 on 2017/11/8.
 *
 * @module 记事本展示的RecyclerView的adapter
 */

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "NoteAdapter";
    private List<Note> mNotes;
    private Context mContext;
    private LayoutInflater mInflater;

    private OnItemClickListener mClickListener;

    public static int SCREEN_WIDTH = -1;

    public NoteAdapter(Context context, List<Note> data) {
        mContext = context;
        mNotes = data;
        mInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView content;
        public TextView time;

        public myViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.note_show_title);
            time = (TextView) itemView.findViewById(R.id.note_show_time);
            content = (TextView) itemView.findViewById(R.id.note_show_content);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.note_item, parent, false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mNotes == null) {
            return;
        }
        ((myViewHolder)holder).title.setText(mNotes.get(position).getTitle());
        ((myViewHolder)holder).time.setText(mNotes.get(position).getTime());

        computeViewHeightAndWidth(holder);

        ((myViewHolder)holder).content.setText(mNotes.get(position).getContent());

        dealClickEvent(holder);
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

    private void computeViewHeightAndWidth(RecyclerView.ViewHolder holder) {
        if (SCREEN_WIDTH == -1) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            SCREEN_WIDTH = wm.getDefaultDisplay().getWidth();
        }
        ViewGroup.LayoutParams lp = ((myViewHolder)holder).content.getLayoutParams();
        lp.height = (int)(Math.random() * 300) + 50 * 3;
        lp.width = SCREEN_WIDTH / 3;
        ((myViewHolder)holder).content.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return mNotes != null ? mNotes.size():0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
