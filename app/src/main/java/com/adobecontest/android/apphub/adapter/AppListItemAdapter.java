package com.adobecontest.android.apphub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adobecontest.android.apphub.R;
import com.adobecontest.android.apphub.model.AppModel;

import java.util.List;

/**
 * Created by brajesh on 26/1/15.
 */
public class AppListItemAdapter extends BaseAdapter {
    private List<AppModel> mAppDataList;
    private Context mContext;

    public AppListItemAdapter(Context context, List<AppModel> appDataList) {
        this.mAppDataList = appDataList;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        if (mAppDataList != null) {
            return mAppDataList.size();
        }
        return 0;
    }

    @Override
    public AppModel getItem(int position) {
        if (mAppDataList != null) {
            return mAppDataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refreshList(List<AppModel> appDataList) {
        mAppDataList = appDataList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_app, parent, false);
        AppModel data = getItem(position);
        TextView listItemAppTextView = (TextView) convertView
                .findViewById(R.id.list_item_app_textview);
        TextView listItemPurchaseTextView = (TextView) convertView
                .findViewById(R.id.list_item_purchase_textview);
        listItemPurchaseTextView.setText(mContext
                .getString(R.string.inapp_purchase, data.getmInAppPurchase()));
        listItemAppTextView.setText(data.getmName());
        return convertView;
    }
}
