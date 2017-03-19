package com.contactsdashboard_ketan.adapter;

import android.app.Activity;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.contactsdashboard_ketan.R;
import com.contactsdashboard_ketan.helper.BLog;
import com.contactsdashboard_ketan.helper.PhonebookUtil;
import com.contactsdashboard_ketan.model.PhonebookModel;
import com.contactsdashboard_ketan.viewholder.PhonebookViewHolder;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Ketan on 3/19/17.
 */

public class PhonebookAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = "TableListAdapter";
    private List<PhonebookModel> itemList;
    Activity activity;
    int cnt = 0;

    public PhonebookAdapter(Activity activity, List<PhonebookModel> itemList) {
        this.itemList = itemList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v2 = inflater.inflate(R.layout.phonebook_item_layout, parent, false);
        viewHolder = new PhonebookViewHolder(v2, activity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PhonebookViewHolder vh2 = (PhonebookViewHolder) holder;
        PhonebookModel phonebookModel = itemList.get(position);
        if(phonebookModel.getPicUri() != null){
            Uri pic = Uri.parse(phonebookModel.getPicUri());
            BLog.e(LOG_TAG, "uri - "+pic);
            if (pic != null) {
                vh2.getIcon().setImageURI(pic);
            } else {
                vh2.getIcon().setImageResource(R.mipmap.pro_pic_ic);
            }
        }else{
            vh2.getIcon().setImageResource(R.mipmap.pro_pic_ic);
        }
        vh2.getMobileTxt().setText(phonebookModel.getMobile());
        vh2.getNameTxt().setText(phonebookModel.getName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
