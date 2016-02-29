package com.lapism.searchview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import java.util.ArrayList;
import java.util.List;


public class MenuItemActivity extends BaseActivity {

    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    private int mVersion = SearchCodes.VERSION_MENU_ITEM;
    private int mStyle = SearchCodes.STYLE_MENU_ITEM_CLASSIC;
    private int mTheme = SearchCodes.THEME_LIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mVersion = extras.getInt("version");
            mStyle = extras.getInt("style");
            mTheme = extras.getInt("theme");

            if (mTheme == SearchCodes.THEME_LIGHT) {
                setTheme(R.style.AppThemeLight);
                checkedMenuItem = -1;
            }

            if (mTheme == SearchCodes.THEME_DARK) {
                setTheme(R.style.AppThemeDark);
                checkedMenuItem = -1;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);

        mToolbar = getToolbar();
        setTitle(mTheme == SearchCodes.THEME_LIGHT ? "Light" : "Dark");
        mToolbar.setTitle(mTheme == SearchCodes.THEME_LIGHT ? "Light" : "Dark");
        mToolbar.setSubtitle(mStyle == SearchCodes.STYLE_MENU_ITEM_CLASSIC ? "Classic" : "Color");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mHistoryDatabase = new SearchHistoryTable(this);
        mSuggestionsList = new ArrayList<>();

        // SearchView basic attributes  ------------------------------------------------------------
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setVersion(mVersion);
        mSearchView.setStyle(mStyle);
        mSearchView.setTheme(mTheme);
        // -----------------------------------------------------------------------------------------
        mSearchView.setDivider(false);
        mSearchView.setHint("Search");
        mSearchView.setHint(R.string.search);
        mSearchView.setHintSize(getResources().getDimension(R.dimen.search_text_medium));
        mSearchView.setVoice(true);
        mSearchView.setVoiceText("Voice");
        mSearchView.setAnimationDuration(300);
        mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.hide(false);
                mHistoryDatabase.addItem(new SearchItem(query));
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                // mFab.hide();
            }

            @Override
            public void onSearchViewClosed() {
                // mFab.show();
            }
        });

        List<SearchItem> mResultsList = new ArrayList<>();
        SearchAdapter mSearchAdapter = new SearchAdapter(this, mResultsList, mSuggestionsList, mTheme);
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mSearchView.hide(false);
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                CharSequence text = textView.getText();
                mHistoryDatabase.addItem(new SearchItem(text));
                Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mSearchView.setAdapter(mSearchAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String searchWrd = results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showSearchView() {
        mSuggestionsList.clear();
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems());
        mSuggestionsList.add(new SearchItem("Google"));
        mSuggestionsList.add(new SearchItem("Android"));
        mSearchView.show(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                showSearchView();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                // NavUtils.navigateUpTo();
                // DatabaseUtils ...
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}