package com.app.monitormymortgage.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Fragments.CreateNewAccountFragment;
import com.app.monitormymortgage.Fragments.PrivacyPolicyFragment;
import com.app.monitormymortgage.Fragments.TermsAndConditionFragment;
import com.app.monitormymortgage.R;

public class CreateNewAccountActivity extends BaseActivity {
    private static final String TAG = CreateNewAccountActivity.class.getSimpleName();

    ViewDialog viewDialog;
    ToastMessage toastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewDialog = new ViewDialog(this);
        toastMessage = new ToastMessage(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.previous, getApplicationContext().getTheme()));
        } else {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.previous));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonConstants.flag) {
                    fragmentReplace();
                }else {
                    finish();
                    Intent intent=new Intent(CreateNewAccountActivity.this,LoginActivity.class);
                    startActivity(intent);
                    CommonConstants.flag=false;
                }
            }
        });
        CreateNewAccountFragment createNewAccountFragment = new CreateNewAccountFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_create_new_account, createNewAccountFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (!CommonConstants.flag) {
            fragmentReplace();
        }else {
            finish();
            Intent intent=new Intent(CreateNewAccountActivity.this,LoginActivity.class);
            startActivity(intent);
            CommonConstants.flag=false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.icon).setVisible(true);
        menu.findItem(R.id.closeButton).setVisible(false);
        menu.findItem(R.id.icon).setEnabled(false);
        return true;
    }

    public void fragmentReplace() {
        if (getSupportFragmentManager().findFragmentByTag(new TermsAndConditionFragment().getClass().getName()) != null) {
            getSupportFragmentManager().popBackStack(new TermsAndConditionFragment().getClass().getName(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (getSupportFragmentManager().findFragmentByTag(new PrivacyPolicyFragment().getClass().getName()) != null) {
            getSupportFragmentManager().popBackStack(new PrivacyPolicyFragment().getClass().getName(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            super.onBackPressed();
        }
    }
}


