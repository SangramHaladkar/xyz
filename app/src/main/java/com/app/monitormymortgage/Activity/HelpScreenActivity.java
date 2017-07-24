package com.app.monitormymortgage.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.RadioGroup;

import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.Fragments.HelpScreenFifthFragment;
import com.app.monitormymortgage.Fragments.HelpScreenFirstFragment;
import com.app.monitormymortgage.Fragments.HelpScreenFourthFragment;
import com.app.monitormymortgage.Fragments.HelpScreenSecondFragment;
import com.app.monitormymortgage.Fragments.HelpScreenThirdFragment;
import com.app.monitormymortgage.R;

public class HelpScreenActivity extends BaseActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    /**
     * fields
     */
    private static final String TAG = HelpScreenActivity.class.getSimpleName();
    private static final int NUMBER_OF_PAGES = 5;
    private RadioGroup radioGroup;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_help_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(this);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);

    }
    /*************************************************************
     * Listeners for ViewPager
     *************************************************************/
    /**
     * When the current page is scrolled
     *
     * @param position
     * @param v
     * @param i
     */
    @Override
    public void onPageScrolled(int position, float v, int i) {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * When a new page becomes selected
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                radioGroup.check(R.id.radioButton1);
                this.setTitle(R.string.help_screen_one);
                break;
            case 1:
                radioGroup.check(R.id.radioButton2);
                this.setTitle(R.string.why_m3);
                break;
            case 2:
                radioGroup.check(R.id.radioButton3);
                this.setTitle(R.string.about_your_mortgage);
                break;
            case 3:
                radioGroup.check(R.id.radioButton4);
                this.setTitle(R.string.saving_direct_to_you);
                break;
            case 4:
                radioGroup.check(R.id.radioButton5);
                this.setTitle(R.string.join_now);
                break;
            default:
                radioGroup.check(R.id.radioButton1);
        }
    }

    /**
     * When the pager is automatically setting to the current page
     *
     * @param position
     */
    @Override
    public void onPageScrollStateChanged(int position) {

    }

    /**
     * On checked listener to Radio Buttons.
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton1:
                pager.setCurrentItem(0);
                break;
            case R.id.radioButton2:
                pager.setCurrentItem(1);
                break;
            case R.id.radioButton3:
                pager.setCurrentItem(2);
                break;
            case R.id.radioButton4:
                pager.setCurrentItem(3);
                break;
            case R.id.radioButton5:
                pager.setCurrentItem(4);
                break;
        }
    }

    /**
     * Custom PagerAdapter class
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {
        /**
         * Constructor
         *
         * @param fm
         */
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment based on the position.
         *
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HelpScreenFirstFragment.newInstance("FirstFragment, Instance 1");
                case 1:
                    return HelpScreenSecondFragment.newInstance("SecondFragment, Instance 1");
                case 2:
                    return HelpScreenThirdFragment.newInstance("ThirdFragment, Instance 1");
                case 3:
                    return HelpScreenFourthFragment.newInstance("FourthFragment, Instance 1");
                case 4:
                    return HelpScreenFifthFragment.newInstance("FifthFragment, Instance 1");
                default:
                    return HelpScreenFirstFragment.newInstance("FirstFragment, Default");
            }
        }

        /**
         * Return the number of pages.
         *
         * @return
         */
        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }
    }


}
