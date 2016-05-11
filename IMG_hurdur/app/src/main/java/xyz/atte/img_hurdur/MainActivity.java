package xyz.atte.img_hurdur;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class acts as a main view for the application.
 * <br>
 * {@inheritDoc}
 *
 * @author Mikko Tossavainen
 * @author Atte Huhtakangas
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    protected String mToken;
    protected Date mTokenExpires;

    /**
     * Used to setup ui elements in the view and set on click listener.
     * <br>
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mToken = extras.getString("token");
            mTokenExpires = new Date(extras.getInt("expiresIn"));
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Add listener to the back button in toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        });

    }

    /**
     * Used to hide visual backbutton on app toolbar.
     * <br>
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /**
     * Creates floating action button and adds a listener which will start new activity
     * when pressed.
     * {@inheritDoc}
     */
    @Override
    protected void onStart() {
        super.onStart();
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ImageUploadActivity.class);
                intent.putExtra("token", mToken);
                startActivity(intent);
            }
        });

    }

    /**
     * Initializes view pager.
     *
     * @param viewPager used to initialized view pager.
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RootFragment(), "BROWSE PICTURES");
        viewPager.setAdapter(adapter);
    }

    /**
     * Used for opening comments fragment.
     *
     * @param mImageID id which represents a image on a server.
     * @param title    image title.
     */
    public void onItemClick(String mImageID, String title) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        //create fragment
        CommentsFragment commentsFragment = new CommentsFragment();

        //add comments to args bundle
        Bundle args = new Bundle();
        args.putString("imageID", mImageID);
        args.putString("token", mToken);
        args.putString("title", title);
        commentsFragment.setArguments(args);

        ft.replace(R.id.root_frame, commentsFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();


    }

    /**
     * Shows back button on app toolbar.
     */
    public void showBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Used for creating Viewpager on fragment.
     *
     * @Author Mikko Tossavainen
     * @Author Atte Huhtakangas
     * @Version 1.0
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        /**
         * {@inheritDoc}
         *
         * @param position used to get item position.
         * @return returns item @ position.
         */
        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        /**
         * {@inheritDoc}
         *
         * @return returns fragmentlist size.
         */
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        /**
         * Adds fragment.
         * {@inheritDoc}
         */
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        /**
         * Returns page title.
         * {@inheri-Doc}
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
