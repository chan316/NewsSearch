package com.codepath.newssearch;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.newssearch.adapter.ArticleAdapter;
import com.codepath.newssearch.model.Article;
import com.codepath.newssearch.model.SearchFilter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements SearchFilterDialogFragment.SearchFilterDialogListener {

    private static final String LOG_TAG = SearchActivity.class.getName();

    private final String URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private String query;

    private List<Article> articles;
    private RecyclerView rwArticleGrid;
    private RecyclerView.Adapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    private SearchFilter searchFilter;

    // TODO implement offline
    final boolean HAS_NETWORK = true;
    final int NUM_COLUMNS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        searchFilter = new SearchFilter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rwArticleGrid = (RecyclerView) findViewById(R.id.rwArticleGrid);

        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this, articles);

        GridLayoutManager grid = new GridLayoutManager(this, NUM_COLUMNS);
        rwArticleGrid.setLayoutManager(grid);
        rwArticleGrid.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(grid) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                refreshArticle(page);
            }
        };
        rwArticleGrid.addOnScrollListener(scrollListener);
    }

    private void setQuery(String query) {
        this.searchFilter.setQuery(query);
    }

    public String getQuery() {
        return this.searchFilter.getQuery();
    }

    public SearchFilter getSearchFilter() { return this.searchFilter; }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchItem.expandActionView();
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                setQuery(query);

                if (HAS_NETWORK) {
                    articles.clear();
                    refreshArticle(0);
                    adapter.notifyDataSetChanged();
                    scrollListener.resetState();
                } else {
                    refreshArticleOffline();
                }
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miFilterButton:
                showSearchFilterDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearchFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable("searchFilter", Parcels.wrap(searchFilter));

        SearchFilterDialogFragment searchFilterDialogFragment = SearchFilterDialogFragment.newInstance(bundle);
        searchFilterDialogFragment.show(getSupportFragmentManager(), "dialog");

    }

    private void refreshArticle(final int page) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("api-key", "88c5c0da90b64cca8bb6503b8c70567c");
        params.put("page", page);

        if (searchFilter.getQuery() != null && !searchFilter.getQuery().isEmpty()) {
            params.put("q", searchFilter.getQuery());
        }

        if (searchFilter.getDate() != null && !searchFilter.getDate().isEmpty()) {
            params.put("begin_date", searchFilter.getDate());
        }

        if (searchFilter.getSort() != null && !searchFilter.getSort().isEmpty() && searchFilter.getSort() != "none") {
            params.put("sort", searchFilter.getSort());
        }

        if (searchFilter.getCategory() != null && !searchFilter.getCategory().isEmpty() && searchFilter.getCategory() != "none") {
            params.put("fq", searchFilter.getCategory());
        }

        client.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray docsArray = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(docsArray));
                    adapter.notifyItemRangeInserted(page * 10, 10);
                    Toast.makeText(SearchActivity.this, headers.toString(), Toast.LENGTH_SHORT);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(SearchActivity.this, "Articles for: " + articles.size(), Toast.LENGTH_SHORT);
                super.onSuccess(statusCode, headers, response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(SearchActivity.this, "Articles failed: " + articles.size(), Toast.LENGTH_SHORT);

            }

        });
    }

    private void refreshArticleOffline() {

        for (int i = 0; i < 20; i++) {
            articles.add(Article.makeArticle("Lorem Ipsem lorem ipsem lorem ipsem " + i, "http://www.foo.com/", null));
        }
    }

    @Override
    public void onSearchFilterClick(Parcelable p, DialogFragment dialog) {
        searchFilter = Parcels.unwrap(p);
        dialog.dismiss();
    }
}
