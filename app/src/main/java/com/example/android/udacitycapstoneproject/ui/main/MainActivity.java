package com.example.android.udacitycapstoneproject.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.udacitycapstoneproject.BuildConfig;
import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.ui.detail.DetailActivity;
import com.example.android.udacitycapstoneproject.ui.detail.article_detail.ArticleDetailFragment;
import com.example.android.udacitycapstoneproject.ui.favourites.FavouriteActivity;
import com.example.android.udacitycapstoneproject.ui.main.article_list.ArticleListFragment;
import com.example.android.udacitycapstoneproject.ui.settings.SettingsActivity;
import com.example.android.udacitycapstoneproject.utils.AppConstants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ArticleListFragment.OnArticleListListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.main_activity_toolbar)
    Toolbar toolbar;
    private InterstitialAd interstitialAd;
    private FragmentManager fragmentTransactionManager;
    private boolean isTwoPane;
    private SharedViewModel viewModel;
    private int orientation;
    private AdRequest ar;
    private String currChannel = "";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set layout
        setContentView(R.layout.activity_main);
        // butterknife binding
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            currChannel = savedInstanceState.getString("channel_name");
        }
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        orientation = getResources().getConfiguration().orientation;
        isTwoPane = getResources().getBoolean(R.bool.isTablet);
        fragmentTransactionManager = getSupportFragmentManager();
        // Obtain the Firebase-Analytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initDrawerToggle();
        // set up drawer content
        setUpDrawerContent();
        // init view-model & observe any data
        initViewModel();
        // init mob id
        initAddMob();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("channel_name", currChannel);
    }

    private void initDrawerToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * init ad-mob
     */
    private void initAddMob() {
        MobileAds.initialize(this, BuildConfig.AdAppId);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(BuildConfig.AdInterestialUnitIt);
        ar = new AdRequest
                .Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
    }

    // observing view model
    private void initViewModel() {
        if(viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
            //setUpUIForDifferentScreenSize();
            setNewListFragment();
        }
    }

    private void logEventForAnalytics(String id, String name){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "NAV_BAR");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * check if it's tablet or mobile phone, set layout accordingly
     */
    private void setUpUIForDifferentScreenSize() {
        setNewListFragment();
    }

    /**
     * on click listener for drawer, set checked menu, close drawer & set-drawaer menu item
     */
    private void setUpDrawerContent() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                setDrawerMenuItem(menuItem);
                return true;
            }
        });
    }

    /**
     * replace fragment with selected menu item in navigation bar
     *
     * @param menuItem : selected menu item in bar
     */
    private void setDrawerMenuItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.channel_label_bbc_sports:
                replaceWithNavItemFragment(getString(R.string.channel_source_bbc_sports));
                break;
            case R.id.channel_label_bleacher_report:
                replaceWithNavItemFragment(getString(R.string.channel_source_bleacher_report));
                break;
            case R.id.channel_label_espn:
                replaceWithNavItemFragment(getString(R.string.channel_source_espn));
                break;
            case R.id.channel_label_espn_cric_info:
                replaceWithNavItemFragment(getString(R.string.channel_source_espn_cric_info));
                break;
            case R.id.channel_label_football_italia:
                replaceWithNavItemFragment(getString(R.string.channel_source_football_italia));
                break;
            case R.id.channel_label_four_four_Two:
                replaceWithNavItemFragment(getString(R.string.channel_source_four_four_Two));
                break;
            case R.id.channel_label_fox_sports:
                replaceWithNavItemFragment(getString(R.string.channel_source_fox_sports));
                break;
            case R.id.channel_label_le_quipe:
                replaceWithNavItemFragment(getString(R.string.channel_source_le_quipe));
                break;
            case R.id.channel_label_marca:
                replaceWithNavItemFragment(getString(R.string.channel_source_marca));
                break;
            case R.id.channel_label_nfl_news:
                replaceWithNavItemFragment(getString(R.string.channel_source_nfl_news));
                break;
            case R.id.channel_label_nhl_news:
                replaceWithNavItemFragment(getString(R.string.channel_source_nhl_news));
                break;
            case R.id.channel_label_talk_sport:
                replaceWithNavItemFragment(getString(R.string.channel_source_talk_sport));
                break;
            case R.id.channel_label_the_sport_bible:
                replaceWithNavItemFragment(getString(R.string.channel_source_the_sport_bible));
                break;
            default:
                replaceWithNavItemFragment(viewModel.getDefaultOrFavChannel());
        }
    }

    /**
     * @param channel : selected channel name
     */
    private void replaceWithNavItemFragment(String channel) {
        this.currChannel = channel;
        logEventForAnalytics(channel, channel);
        if(isTwoPane && (orientation != Configuration.ORIENTATION_LANDSCAPE)) {
            viewModel.setChannel(channel);
        } else {
            ArticleListFragment fragment =
                    (ArticleListFragment) getSupportFragmentManager()
                            .findFragmentByTag(getString(R.string.tag_article_list_fragment));
            if (fragment != null) {
                //setUpToolBar(channel);
                fragment.changeMenuItemUpdateNewsList(channel);
            }
        }
    }


    /**
     * set list of news in list-fragment
     */
    private void setNewListFragment() {
        ArticleListFragment listFragment;
        if(isTwoPane && (orientation != Configuration.ORIENTATION_LANDSCAPE)) {
            listFragment = new ArticleListFragment();
        } else {
            Timber.d("curr channel is " + currChannel);
            if(currChannel.equals("")) {
                listFragment = ArticleListFragment.newInstance(viewModel.getDefaultOrFavChannel());
                Timber.d("sending channel via arguments");
            }
            else{
                listFragment = new ArticleListFragment();
                Timber.d("not sending any arguments");
            }

        }
        fragmentTransactionManager.beginTransaction().replace(R.id.news_article_list_container,
                listFragment, getString(R.string.tag_article_list_fragment))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_favourite :
                showAddAndShowFavourites();
                return true;
            case R.id.action_settings : startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return true;
    }

    private void showAddAndShowFavourites() {
        interstitialAd.loadAd(ar);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
            }
        });
    }

    /**
     * @param article : open new activity if mobile device
     */
    @Override
    public void setArticleSelectedInDetailScreen(Article article, boolean flag) {
        if(isTwoPane && (orientation != Configuration.ORIENTATION_LANDSCAPE)) {
            if(flag){
                fragmentTransactionManager.beginTransaction().replace(R.id.news_article_detail_container,
                        ArticleDetailFragment.newInstance(article),
                        getString(R.string.tag_detail_fragment)).commit();
            }
            viewModel.setArticleMutableLiveData(article);
        }
        else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(AppConstants.KEY_BUNDLE_PARCELLABLE, article);
            startActivity(intent);
        }
    }

}
