package me.eagzzycsl.intertent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import me.eagzzycsl.intertent.frag.ChatFragment;
import me.eagzzycsl.intertent.frag.ConnectFragment;
import me.eagzzycsl.intertent.frag.InputFragment;
import me.eagzzycsl.intertent.service.MainService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    private TabLayout main_tabLayout;
    private ViewPager main_viewPager;
    private FragmentPagerAdapter main_fragmentPagerAdapter;
    private Toolbar main_toolbar;
    private FloatingActionButton main_fab;
    private NavigationView main_navigationView;
    private DrawerLayout main_drawer;
    private ActionBarDrawerToggle main_toggle;
    private Fragment[] main_fragmentList = new Fragment[]{
            new ConnectFragment(),
            new InputFragment(),
            new ChatFragment()
    };
    private String[] main_tabLayout_title = new String[]{
            "连接",
            "远程输入",
            "互相通信"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.myFind();
        this.myCreate();
        this.mySet();
        startService(new Intent(this, MainService.class));


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void myFind() {
        this.main_tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        this.main_viewPager = (ViewPager) findViewById(R.id.main_viewPager);
        this.main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        this.main_fab = (FloatingActionButton) findViewById(R.id.main_fab);
        this.main_navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        this.main_drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
    }

    private void myCreate() {
        main_fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return main_fragmentList[position];
            }

            @Override
            public int getCount() {
                return main_fragmentList == null ? 0 : main_fragmentList.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return main_tabLayout_title[position];
            }
        };

        main_toggle = new ActionBarDrawerToggle(
                this, main_drawer, main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    private void mySet() {
        setSupportActionBar(main_toolbar);
        main_fab.setOnClickListener(this);
        main_toggle.syncState();
        main_drawer.addDrawerListener(main_toggle);
        main_navigationView.setNavigationItemSelectedListener(this);
        main_viewPager.setAdapter(main_fragmentPagerAdapter);
        main_tabLayout.setupWithViewPager(main_viewPager, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_fab: {
                Snackbar.make(null, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }
}
