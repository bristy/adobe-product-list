package com.adobecontest.android.apphub.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.adobecontest.android.apphub.R;
import com.adobecontest.android.apphub.activity.AppDetailActivity;
import com.adobecontest.android.apphub.adapter.AppListItemAdapter;
import com.adobecontest.android.apphub.helper.NetworkHelper;
import com.adobecontest.android.apphub.model.AppModel;
import com.adobecontest.android.apphub.network.RestClient;
import com.adobecontest.android.apphub.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppListFragment extends Fragment {
    private static final boolean DEBUG = true;
    private RestClient mRestClient;
    private static final String BASE_URL = "http://adobe.0x10.info/api/products?";

    private List<AppModel> mAppDataList;
    private AppListItemAdapter mAppListAdapter;
    private ListView mAppListView;
    private TextView mProductCountTextView;
    private boolean mSortByInApp = false;
    private boolean mSortByRating = false;
    private TextView mInAppTextView;
    private TextView mRatingTextView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AppListFragment.
     */
    public static AppListFragment newInstance() {
        AppListFragment fragment = new AppListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AppListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_app_list, container, false);

        mProductCountTextView = (TextView) rootView.findViewById(R.id.product_cout_textview);
        mInAppTextView = (TextView) rootView.findViewById(R.id.inapp_tv);
        mRatingTextView = (TextView) rootView.findViewById(R.id.rating_tv);
        mInAppTextView.setOnClickListener(mOnSortClick);
        mRatingTextView.setOnClickListener(mOnSortClick);
        int count = mAppDataList != null ? mAppDataList.size() : 0;

        mProductCountTextView.setText(getString(R.string.product_count, count));
        mAppListView = (ListView) rootView.findViewById(R.id.list_view_app);
        mAppListAdapter = new AppListItemAdapter(getActivity(), new ArrayList<AppModel>());
        mAppListView.setAdapter(mAppListAdapter);
        mAppListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AppDetailActivity.class);
                intent.putExtra(Constants.DETAIL_EXTRA, mAppListAdapter.getItem(position));
                startActivity(intent);

            }
        });
        return rootView;
    }

    private View.OnClickListener mOnSortClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.rating_tv) {
                // other is checked
                if (mSortByInApp) {
                    mSortByInApp = false;
                    toogleTextView(mInAppTextView, mSortByInApp);
                }
                mSortByRating = !mSortByRating;
                toogleTextView(mRatingTextView, mSortByRating);
                sortProducts();
            } else if (id == R.id.inapp_tv) {
                if (mSortByRating) {
                    mSortByRating = false;
                    toogleTextView(mRatingTextView, mSortByRating);
                }
                mSortByInApp = !mSortByInApp;
                toogleTextView(mInAppTextView, mSortByInApp);
                sortProducts();
            }
        }
    };

    private void sortProducts() {
        Comparator<AppModel> comparator = null;
        if (mSortByInApp) {
            comparator = new Comparator<AppModel>() {
                public int compare(AppModel c1, AppModel c2) {
                    return c1.getmInAppPurchase().compareTo(c2.getmInAppPurchase()); // use your logic
                }


            };
        } else if (mSortByRating) {
            comparator = new Comparator<AppModel>() {
                public int compare(AppModel c1, AppModel c2) {
                    return c1.getmRating().compareTo(c2.getmRating()); // use your logic
                }


            };
        } else {
            mAppListAdapter.refreshList(mAppDataList);
        }
        if(mAppDataList != null && comparator != null){
            List<AppModel> data = new ArrayList<AppModel>();
            data.addAll(mAppDataList);
            Collections.sort(data, comparator);
            mAppListAdapter.refreshList(data);
        }
    }


    private void toogleTextView(TextView textView, boolean checked) {
        if (checked) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_accept, 0, 0, 0);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class AppInfoFetchTask extends AsyncTask<Void, Void, List<AppModel>> {

        private final String TAG = AppInfoFetchTask.class.getSimpleName();
        final String TYPE = "json";
        final String TYPE_PARAM = "type";


        @Override
        protected void onPostExecute(List<AppModel> dataList) {
            if (dataList == null) {
                // error
                return;
            }
            mAppDataList = dataList;
            mAppListAdapter.refreshList(mAppDataList);
        }

        @Override
        protected List<AppModel> doInBackground(Void... params) {
            Map<String, String> urlParam = new HashMap<String, String>();
            urlParam.put(TYPE_PARAM, TYPE);

            String url = NetworkHelper.makeUrl(BASE_URL, urlParam);
            if (DEBUG) {
                Log.v(TAG, url);
            }
            mRestClient = new RestClient(url, "GET");
            String appJsonData = mRestClient.getJSONData();
            List<AppModel> dataList = null;
            if (appJsonData != null) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    dataList = (List<AppModel>) mapper.readValue(appJsonData,
                            mapper.getTypeFactory().
                                    constructCollectionType(List.class, AppModel.class));
                    if (DEBUG) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                                + appJsonData);
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                                + mAppDataList);
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                }
            }


            return normalize(dataList);
        }

        private List<AppModel> normalize(List<AppModel> data) {
            if (data == null) {
                return null;
            }
            List<AppModel> normData = new ArrayList<AppModel>();
            for (AppModel model : data) {
                if (model.getmName() != null && model.getmName().length() != 0) {
                    normData.add(model);
                } else {
                    Log.e(TAG, "some bad input in api");
                }
            }
            return normData;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        updateAppInfo();
    }

    private void updateAppInfo() {
        new AppInfoFetchTask().execute();
    }
}
