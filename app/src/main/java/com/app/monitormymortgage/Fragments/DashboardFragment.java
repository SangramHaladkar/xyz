package com.app.monitormymortgage.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.monitormymortgage.Adapters.MortgageRecyclerViewAdapter;
import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.DataHolderClasses.MortgageArrayHolder;
import com.app.monitormymortgage.R;
import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ResultListener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class DashboardFragment extends BaseFragment {
    private final String TAG = DashboardFragment.class.getSimpleName();

    private RecyclerView mortgageRecyclerView;
    private List<MortgageArrayHolder> mortgageArrayHolders=null;
    private Context mContext;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=DashboardFragment.this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initView(rootView);
        showProgress(mContext);
        this.getMortgageList(getGlobalMethods().getUserId());
        return rootView;
    }

    private void initView(View view) {
        mortgageRecyclerView = (RecyclerView) view.findViewById(R.id.mortgageRecyclerView);
        mortgageRecyclerView.setHasFixedSize(true);
        mortgageRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }



    private void getMortgageList(String userId)
    {
        HashMap<String, Object> mortgageMap=new HashMap<>();
        mortgageMap.put("user_id",userId);
        mortgageMap.put("lang", CommonConstants.language);
        mortgageMap.put("req_from",CommonConstants.requestFrom);

        ErisCollectionManager.getInstance().callMethod("getUserMortgages", new Object[]{mortgageMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v(TAG,result);

                try {
                    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    if (jsonObjectData.has("result")) {
                        JSONArray jsonArrayResult = jsonObjectData.getJSONArray("result");
                        mortgageArrayHolders=objectMapper.readValue(jsonArrayResult.toString(), new TypeReference<List<MortgageArrayHolder>>() {});
                        Log.v(TAG,mortgageArrayHolders.toString());
                        MortgageRecyclerViewAdapter adapter=new MortgageRecyclerViewAdapter(mContext,mortgageArrayHolders);
                        mortgageRecyclerView.setAdapter(adapter);
                    }

                    hideProgress();

                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, String reason, String details) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
