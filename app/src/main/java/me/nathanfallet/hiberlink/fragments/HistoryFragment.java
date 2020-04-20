package me.nathanfallet.hiberlink.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.nathanfallet.hiberlink.R;
import me.nathanfallet.hiberlink.utils.Database;

public class HistoryFragment extends Fragment implements HistoryRecyclerViewAdapter.HistoryContainer {

    private RecyclerView recyclerView;
    private List<Pair<String, String>> links = new ArrayList<>();

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        setHasOptionsMenu(true);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new HistoryRecyclerViewAdapter(this));
        }

        // Load content
        loadContent();

        return view;
    }

    public void loadContent() {
        // Load links from database
        links = Database.getInstance(recyclerView.getContext()).getLinks();

        // Refresh recycler view
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public List<Pair<String, String>> getLinks() {
        return links;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_clear:
                // Clear history
                Database.getInstance(recyclerView.getContext()).clearHistory();

                // Refresh recycler view
                links.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
