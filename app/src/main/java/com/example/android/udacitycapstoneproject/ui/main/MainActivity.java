package com.example.android.udacitycapstoneproject.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.ui.detail.DetailActivity;
import com.example.android.udacitycapstoneproject.ui.detail.article_detail.ArticleDetailFragment;
import com.example.android.udacitycapstoneproject.ui.favourites.FavouriteActivity;
import com.example.android.udacitycapstoneproject.ui.main.article_list.ArticleListFragment;
import com.example.android.udacitycapstoneproject.ui.settings.SettingsActivity;
import com.example.android.udacitycapstoneproject.utils.AppConstants;
import com.example.android.udacitycapstoneproject.worker.SyncNewsWorker;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.android.udacitycapstoneproject.utils.AppConstants.TAG_PERIODIC_WORK_REQUEST;

public class MainActivity extends AppCompatActivity implements ArticleListFragment.OnArticleListListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.main_activity_toolbar)
    Toolbar toolbar;
    private FragmentManager manager;
    private boolean isTwoPane;
////    private PeriodicWorkRequest periodicWorkRequest;
//    private SharedPreferences prefs;
//    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private SharedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set layout
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        isTwoPane = getResources().getBoolean(R.bool.isTablet);
        manager = getSupportFragmentManager();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // init view-model & observe any data
        observeViewModel();
        // set fav channel as home
        viewModel.setChannel(viewModel.getFavouriteChannel());
        setUpUIForDifferentScreenSize();
        // set up drawer content
        setUpDrawerContent();

 //       registerNewsFavChangeListener();
    }

//    /**
//     * listener to change in fav channel & sync data using work-manager
//     * cancel existing work manager instance
//     */
//    private void registerNewsFavChangeListener() {
//        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//                if(key.equals(getString(R.string.key_fav_channel))) {
//                    Timber.d("fav channel changed");
//                    if (periodicWorkRequest != null) {
//                        UUID compressionWorkId = periodicWorkRequest.getId();
//                        WorkManager.getInstance().cancelWorkById(compressionWorkId);
//                        startWorkManager();
//                    }
//                }
//            }
//        };
//        prefs.registerOnSharedPreferenceChangeListener(listener);
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        prefs.unregisterOnSharedPreferenceChangeListener(listener);
//    }

    // observing view model
    private void observeViewModel() {
        viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
    }

    /**
     * check if it's tablet or mobile phone, set layout accordingly
     */
    private void setUpUIForDifferentScreenSize() {
        if (isTwoPane) {
            Timber.d("tablet screen");
            setNewListFragment();
            viewModel.startFetchingData(viewModel.getDefaultOrFavChannel());
            viewModel.getNewsNetworkLiveData().observe(this, new Observer<List<Article>>() {
                @Override
                public void onChanged(@Nullable List<Article> articles) {
                    manager.beginTransaction().replace(R.id.news_article_detail_container,
                            ArticleDetailFragment.newInstance(articles.get(0)),
                            getString(R.string.tag_detail_fragment)).commit();
                }
            });
        } else {
            setNewListFragment();
        }
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
                Timber.e("unknown menu item");
        }
    }

    /**
     * @param channel : selected channel name
     */
    private void replaceWithNavItemFragment(String channel) {
        if(isTwoPane) {
            viewModel.setChannel(channel);
        } else {
            ArticleListFragment fragment =
                    (ArticleListFragment) getSupportFragmentManager()
                            .findFragmentByTag(getString(R.string.tag_article_list_fragment));
            if (fragment != null) {
                fragment.changeMenuItemUpdateNewsList(channel);
            }
        }
    }

    /**
     * set list of news in list-fragment
     */
    private void setNewListFragment() {
        ArticleListFragment listFragment = ArticleListFragment.newInstance(viewModel.getFavouriteChannel());

        manager.beginTransaction().add(R.id.news_article_list_container,
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
            case R.id.action_favourite : startActivity(new Intent(this, FavouriteActivity.class));
                return true;
            case R.id.action_settings : startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_sync: //startWorkManager();
                return true;
        }
        return true;
    }

//    /**
//     * starts work manager using periodic work request
//     */
//    private void startWorkManager() {
//        final WorkManager workManager = WorkManager.getInstance();
//
//        periodicWorkRequest =
//                new PeriodicWorkRequest.Builder(SyncNewsWorker.class,
//                        2, TimeUnit.MINUTES)
//                        .addTag(TAG_PERIODIC_WORK_REQUEST)
//                        .build();
//
//        // Queue the work
//        Timber.d("work manager enqueue");
//        workManager.enqueue(periodicWorkRequest);
//    }

    /**
     * @param article : open new activity if mobile device
     */
    @Override
    public void setArticleSelectedInDetailScreen(Article article) {
        if(isTwoPane) {
            viewModel.setArticleMutableLiveData(article);
        }
        else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(AppConstants.KEY_BUNDLE_PARCELLABLE, article);
            startActivity(intent);
        }
    }

}
