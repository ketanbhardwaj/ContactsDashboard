package com.contactsdashboard_ketan.viewholder;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.contactsdashboard_ketan.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ketan on 3/19/17.
 */

public class PhonebookViewHolder extends RecyclerView.ViewHolder {

    public CardView cardview;
    public TextView nameTxt, mobileTxt;
    public CircleImageView icon;
    Activity activity;

    public PhonebookViewHolder(View itemView, Activity activity) {
        super(itemView);
        this.activity = activity;
        icon = (CircleImageView)itemView.findViewById(R.id.ph_pic);
        nameTxt = (TextView)itemView.findViewById(R.id.ph_name_txt);
        mobileTxt = (TextView) itemView.findViewById(R.id.ph_mobile_txt);
        cardview = (CardView) itemView.findViewById(R.id.ph_cardview);
    }

    public CardView getCardview() {
        return cardview;
    }

    public void setCardview(CardView cardview) {
        this.cardview = cardview;
    }

    public TextView getNameTxt() {
        return nameTxt;
    }

    public void setNameTxt(TextView nameTxt) {
        this.nameTxt = nameTxt;
    }

    public TextView getMobileTxt() {
        return mobileTxt;
    }

    public void setMobileTxt(TextView mobileTxt) {
        this.mobileTxt = mobileTxt;
    }

    public CircleImageView getIcon() {
        return icon;
    }

    public void setIcon(CircleImageView icon) {
        this.icon = icon;
    }
}
