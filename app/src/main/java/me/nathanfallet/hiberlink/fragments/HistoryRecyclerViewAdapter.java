package me.nathanfallet.hiberlink.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.javatuples.Pair;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import me.nathanfallet.hiberlink.R;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private HistoryContainer container;

    public HistoryRecyclerViewAdapter(HistoryContainer container) {
        this.container = container;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = container.getLinks().get(position);
        holder.textLabel.setText(holder.item.getValue0());
        holder.detailTextLabel.setText(holder.item.getValue1());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Copy link
                ClipboardManager clipboard = (ClipboardManager) holder.view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    ClipData clip = ClipData.newPlainText("link", holder.item.getValue0());
                    clipboard.setPrimaryClip(clip);

                    // Show confirmation
                    new AlertDialog.Builder(holder.view.getContext()).setTitle(R.string.copied_title).setPositiveButton(R.string.copied_close, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Close
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return container.getLinks().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final TextView textLabel;
        public final TextView detailTextLabel;
        public Pair<String, String> item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textLabel = view.findViewById(R.id.textLabel);
            this.detailTextLabel = view.findViewById(R.id.detailTextLabel);
        }

    }

    public interface HistoryContainer {

        List<Pair<String, String>> getLinks();

    }

}
