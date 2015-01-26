package com.adobecontest.android.apphub.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adobecontest.android.apphub.R;
import com.adobecontest.android.apphub.util.Constants;
import com.adobecontest.android.apphub.model.AppModel;
import com.adobecontest.android.apphub.util.ImageDownloader;


public class AppDetailActivity extends ActionBarActivity {

    private AppModel appModel;
    private static final String TAG = AppDetailActivity.class.getSimpleName();
    private static final String APPHUB_SHARE_HASHTAG = "#AppHUB";
    private ShareActionProvider mShareActionProvider;
    private String mAppString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);

        Intent intent = getIntent();
        getSupportActionBar().setHomeButtonEnabled(true);
        appModel = (AppModel) intent.getSerializableExtra(Constants.DETAIL_EXTRA);
        if (appModel != null) {

            mAppString = "";
            if (appModel.getmName() != null) {
                mAppString += appModel.getmName();
            }
            if (appModel.getmRating() != null) {
                mAppString += "\n" + "Rating " + appModel.getmRating();
            }
            if (appModel.getmDescription() != null) {
                mAppString += "\n" + appModel.getmDescription();
            }
            if (appModel.getmUrl() != null) {
                mAppString += "\n" +"get it from : "+ appModel.getmUrl();
            }

        }

        setUI();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_detail, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(createShareForecastIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // set detail data
    private void setUI() {
        TextView nameTextView = (TextView) findViewById(R.id.name_textview);
        TextView ratingTextView = (TextView) findViewById(R.id.rating_textview);
        TextView typeTextView = (TextView) findViewById(R.id.type_textview);
        TextView purchaseTextView = (TextView) findViewById(R.id.purchase_textview);
        TextView updateTextView = (TextView) findViewById(R.id.update_textview);
        if (appModel != null) {
            ImageView logoImage = (ImageView) findViewById(R.id.logo_image);
            new ImageDownloader().download(appModel.getmImage(), logoImage);
            nameTextView.setText(appModel.getmName());
            ratingTextView.setText(String.valueOf(appModel.getmRating()));
            typeTextView.setText(appModel.getmType());
            purchaseTextView.setText(appModel.getmInAppPurchase());
            updateTextView.setText(appModel.getmLastUpdated());
            typeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = Toast.makeText(AppDetailActivity.this,
                            appModel.getmDescription(),
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            findViewById(R.id.playstore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + getId(appModel.getmUrl()))));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appModel.getmUrl())));
                    }
                }
            });
        }
    }

    private String getId(String url){
        return url.split("id=")[1];
    }
    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mAppString + APPHUB_SHARE_HASHTAG);
        return shareIntent;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        } else {
            Log.d(TAG, "ShareActionProvider is null");
        }
    }

}
