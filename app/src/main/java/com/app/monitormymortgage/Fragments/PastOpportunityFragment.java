package com.app.monitormymortgage.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.monitormymortgage.Activity.ShowImagesActivity;
import com.app.monitormymortgage.Adapters.PassedOpportunityRecyclerViewAdapter;
import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.DataHolderClasses.PassedOpportunityData;
import com.app.monitormymortgage.DataHolderClasses.ResultPassedOpportunity;
import com.app.monitormymortgage.R;
import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ResultListener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class PastOpportunityFragment extends BaseFragment {

    public final String TAG = PastOpportunityFragment.class.getSimpleName();

    private Context context;

    private RecyclerView passedOpportunityListView;

    private TextView noPassedOpprTV;

    private List<ResultPassedOpportunity> passedOpportunityDataList = null;

    public PastOpportunityFragment() {
        // Required empty public constructor.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.title_past_opportunities));
        context = PastOpportunityFragment.this.getActivity();
        Log.v(TAG, CommonConstants.contactPreference);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_past_opportunity, container, false);
        this.initView(rootView);
        getPassedOpportunities(getGlobalMethods().getUserId());
        showProgress(context);
        return rootView;
    }


    /*Init UI components. */
    public void initView(View view) {
        passedOpportunityListView = (RecyclerView) view.findViewById(R.id.passedOpportunityList);

       /* Use this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView. */
        passedOpportunityListView.setHasFixedSize(true);

        /*  Use a Linear Layout Manager. */
        passedOpportunityListView.setLayoutManager(new LinearLayoutManager(context));
        noPassedOpprTV = (TextView) view.findViewById(R.id.noPassedOpprTV);
    }


    /*
    * Get Past Opportunities List from Server.
    * @param userId
    * */
    public void getPassedOpportunities(final String userId) {
        try {
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("user_id", userId);
            stringObjectHashMap.put("lang", CommonConstants.language);
            stringObjectHashMap.put("req_from", CommonConstants.requestFrom);

            ErisCollectionManager.getInstance().callMethod("getConsumerPastOpportunities", new Object[]{stringObjectHashMap}, new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    Log.v(TAG, result);
                    try {

                        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        PassedOpportunityData passedOpportunityData1 = objectMapper.readValue(result, PassedOpportunityData.class);

                        JSONObject jsonObjectMain = new JSONObject(result);
                        JSONObject jsonObjectError = jsonObjectMain.getJSONObject("error");

                        if (jsonObjectMain.getString("status").equalsIgnoreCase("false")) {
                            noPassedOpprTV.setVisibility(View.VISIBLE);
                            noPassedOpprTV.setText(jsonObjectError.getString("message"));
                        }


                        JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                        if (jsonObjectData.has("result")) {
                            JSONArray jsonArrayResult = jsonObjectData.getJSONArray("result");
                            passedOpportunityDataList = objectMapper.readValue(jsonArrayResult.toString(), new TypeReference<List<ResultPassedOpportunity>>() {
                            });

                            PassedOpportunityRecyclerViewAdapter adapter = new PassedOpportunityRecyclerViewAdapter(passedOpportunityDataList, context, CommonConstants.contactPreference, userId);
                            passedOpportunityListView.setAdapter(adapter);
                        }

                        hideProgress();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        hideProgress();
                    }
                }

                @Override
                public void onError(String error, String reason, String details) {
                    Log.v(TAG, error + " : " + reason);
                    showCustomToast(reason);
                    hideProgress();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        ShowImagesActivity.flagChanged = true;
     }

}
