package app.com.customviews.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

import com.mindorks.placeholderview.PlaceHolderView;

import app.com.customviews.R;
import app.com.customviews.drawer.DrawerHeader;
import app.com.customviews.drawer.DrawerMenuItem;

public class DrawerActivity extends AppCompatActivity {

    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private PlaceHolderView mGalleryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mDrawerView = findViewById(R.id.drawer_view);
        mDrawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        mGalleryView = findViewById(R.id.gallery_view);

        setUpDrawer();


    }

    private void setUpDrawer() {
        mDrawerView
                .addView(new DrawerHeader())
                .addView(new DrawerMenuItem(DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE,this.getApplicationContext()))
                .addView(new DrawerMenuItem(DrawerMenuItem.DRAWER_MENU_ITEM_REQUESTS, this.getApplicationContext()))
                .addView(new DrawerMenuItem(DrawerMenuItem.DRAWER_MENU_ITEM_GROUPS, this.getApplicationContext()))
                .addView(new DrawerMenuItem(DrawerMenuItem.DRAWER_MENU_ITEM_MESSAGE, this.getApplicationContext()))
                .addView(new DrawerMenuItem(DrawerMenuItem.DRAWER_MENU_ITEM_NOTIFICATIONS, this.getApplicationContext()))
                .addView(new DrawerMenuItem(DrawerMenuItem.DRAWER_MENU_ITEM_SETTINGS, this.getApplicationContext()))
                .addView(new DrawerMenuItem(DrawerMenuItem.DRAWER_MENU_ITEM_TERMS, this.getApplicationContext()))
                .addView(new DrawerMenuItem(DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT, this.getApplicationContext()));

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                mDrawer,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
}
