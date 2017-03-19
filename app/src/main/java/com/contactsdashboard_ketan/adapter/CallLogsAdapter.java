package com.contactsdashboard_ketan.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.contactsdashboard_ketan.R;
import com.contactsdashboard_ketan.helper.BLog;
import com.contactsdashboard_ketan.helper.PhonebookUtil;
import com.contactsdashboard_ketan.model.CallLogsModel;
import com.contactsdashboard_ketan.model.PhonebookModel;
import com.contactsdashboard_ketan.viewholder.CallLogsViewHolder;

import java.util.List;

/**
 * Created by Ketan on 3/19/17.
 */

public class CallLogsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = "CallLogsAdapter";
    private List<CallLogsModel> itemList;
    Activity activity;

    public CallLogsAdapter(Activity activity, List<CallLogsModel> itemList) {
        this.itemList = itemList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v2 = inflater.inflate(R.layout.call_log_item_layout, parent, false);
        viewHolder = new CallLogsViewHolder(v2, activity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CallLogsViewHolder vh2 = (CallLogsViewHolder) holder;
        CallLogsModel phonebookModel = itemList.get(position);
        if(phonebookModel.getPic() != null){
            Uri pic = Uri.parse(phonebookModel.getPic());
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
        vh2.getEmailTxt().setText(phonebookModel.getEmail());
        vh2.getEmailTxt().setVisibility(View.GONE);
        vh2.getLastContactTimeTxt().setText("Last Contact Time: "+phonebookModel.getLastContactTime());
        if(phonebookModel.getTotalTalkTime() != null && !phonebookModel.getTotalTalkTime().isEmpty()){
            vh2.getDurationTxt().setText("Total Talk time: "+ PhonebookUtil.getDurationString(Integer.parseInt(phonebookModel.getTotalTalkTime())));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
