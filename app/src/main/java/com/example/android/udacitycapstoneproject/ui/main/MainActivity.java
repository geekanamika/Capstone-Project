package com.example.android.udacitycapstoneproject.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.example.android.udacitycapstoneproject.ui.main.article_list.ArticleListFragment;

import butterknife.BindView;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ArticleListFragment.OnArticleListListener{

    @BindView(R.id.main_activity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    FragmentManager manager;
    private boolean isTwoPane;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MainActivityComponent activityComponent = DaggerMainActivityComponent.builder()
//                .mainActivityModule(new MainActivityModule(this))
//                .applicationComponent(((MyApp) getApplication()).getComponent())
//                .build();
//        activityComponent.injectMainActivity(this);

        setUpToolBar();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpUIForDifferentScreenSize();
        // set up drawer content
        setUpDrawerContent();
    }

    private void setUpDrawerContent() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                setDrawerMenuItem(menuItem);
                return true;
            }
        });
    }

    private void setDrawerMenuItem(MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
           // case R.id.channel_bbc_sports: repository.setCurrentFavChannel(getString(R.string.channel_bbc_sports));
            case R.id.channel_bleacher_report: ;
            case R.id.channel_espn: ;
            case R.id.channel_espn_cric_info: ;
            case R.id.channel_football_italia: ;
            case R.id.channel_four_four_Two: ;
            case R.id.channel_fox_sports: ;
            case R.id.channel_le_quipe: ;
            case R.id.channel_marca: ;
            case R.id.channel_nfl_news: ;
            case R.id.channel_nhl_news: ;
            case R.id.channel_the_sport_bible: ;

        }
    }

    private void setUpUIForDifferentScreenSize() {
        if (isTwoPane) {
            Timber.d("tablet screen");
            setNewListFragment();
            //        manager.beginTransaction().add(R.id.news_article_detail_container,
//                    DetailStepFragment.newInstance(recipeResponse.getSteps().get(0))).commit();

        } else {
            setNewListFragment();
        }
    }

    private void setNewListFragment() {
        manager.beginTransaction().add(R.id.news_article_list_container,
                new ArticleListFragment(), getString(R.string.tag_article_list_fragment))
                .commit();
    }

    private void replaceWithNavItemFragment(String channel) {
        manager.beginTransaction().replace(R.id.news_article_list_container,
                new ArticleListFragment(), getString(R.string.tag_article_list_fragment))
                .commit();
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
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
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setArticleSelectedInDetailScreen(Article article) {
        Timber.d(article.getTitle());
    }
}
