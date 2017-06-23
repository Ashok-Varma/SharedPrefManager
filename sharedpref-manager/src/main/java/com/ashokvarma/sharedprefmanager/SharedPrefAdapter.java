package com.ashokvarma.sharedprefmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see RecyclerView.Adapter
 * @since 22 Jun 2017
 */
class SharedPrefAdapter extends RecyclerView.Adapter<SharedPrefAdapter.ViewHolder> {

    private ArrayList<SharedPrefItemModel> mSharedPrefItemModelList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private Listener mListener;


    SharedPrefAdapter(Context context, ArrayList<SharedPrefItemModel> sharedPrefItemModels) {
        if (sharedPrefItemModels != null) {
            this.mSharedPrefItemModelList.addAll(sharedPrefItemModels);
        }
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.sharedpref_manager_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    void setSharedPrefItemModelList(ArrayList<SharedPrefItemModel> sharedPrefItemModels) {
        this.mSharedPrefItemModelList.clear();
        if (sharedPrefItemModels != null) {
            this.mSharedPrefItemModelList.addAll(sharedPrefItemModels);
        }
        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        vh.textView.setText(mSharedPrefItemModelList.get(position).getDisplayText());
    }

    @Override
    public int getItemCount() {
        return mSharedPrefItemModelList == null ? 0 : mSharedPrefItemModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onSharedPrefItemClicked(mSharedPrefItemModelList.get(getLayoutPosition()));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    String clipText = mSharedPrefItemModelList.get(getLayoutPosition()).getValue().toString();

                    ClipData clip;
                    clip = ClipData.newPlainText("com.ashokvarma.sharedprefmanager", clipText);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(mContext, clipText + " - Copied to clipBoard", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }

    interface Listener {
        void onSharedPrefItemClicked(SharedPrefItemModel sharedPrefItemModel);
    }
}