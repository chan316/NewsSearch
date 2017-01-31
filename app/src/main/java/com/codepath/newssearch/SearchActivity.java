package com.codepath.newssearch;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements SearchFilterFragment.SearchFilterDialogListener {

    private static final String LOG_TAG = SearchActivity.class.getName();

    private final String URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private String query;

    private List<Article> articles;
    private RecyclerView rwArticleGrid;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rwArticleGrid = (RecyclerView) findViewById(R.id.rwArticleGrid);

        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this, articles);

        GridLayoutManager grid = new GridLayoutManager(this, 3);
        rwArticleGrid.setLayoutManager(grid);
        rwArticleGrid.setAdapter(adapter);

    }

    private void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }


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
                refreshArticle(query);
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

        /*
        MenuItem filterItem = menu.findItem(R.id.miFilterButton);
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SearchFilterFragment dialog = new SearchFilterFragment();
                dialog.show(getFragmentManager(), "dialog");
                return true;
            }
        });
        */
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miFilterButton:
                SearchFilterFragment dialog = new SearchFilterFragment();
                dialog.show(getSupportFragmentManager(), "dialog");

                return true;
        }
        Toast.makeText(this, "OPTIONS SELECTED " + item.getItemId(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionViewItem = menu.findItem(R.id.miActionButton);
        // Retrieve the action-view from menu
        View v = MenuItemCompat.getActionView(actionViewItem);
        // Find the button within action-view
        Button b = (Button) v.findViewById(R.id.btnCustomAction);
        // Handle button click here
        return super.onPrepareOptionsMenu(menu);
    }
    */

    private void refreshArticle(final String searchText) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("api-key", "88c5c0da90b64cca8bb6503b8c70567c");
        params.put("page", 0);
        params.put("q", searchText);

        client.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    articles.clear();
                    JSONArray docsArray = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(docsArray));
                    adapter.notifyDataSetChanged();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(SearchActivity.this, "Articles for: " + articles.size(), Toast.LENGTH_SHORT);
                super.onSuccess(statusCode, headers, response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SearchActivity.this, "Failure for: " + getQuery(), Toast.LENGTH_SHORT);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public void onSearchFilterClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    /*
    @Override
    public void onSearchFilterClick(DialogFragment dialog) {
        dialog.dismiss();
    }
    */
}
