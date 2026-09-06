package com.cundong.recyclerview.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Header/data/footer composition using AndroidX only; no library wrapper APIs. */
public class ConcatExampleActivity extends BaseSampleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);
        setTitle(R.string.concat_example_title);

        RowsAdapter header = new RowsAdapter(false);
        RowsAdapter data = new RowsAdapter(true);
        RowsAdapter footer = new RowsAdapter(false);
        ConcatAdapter combined = new ConcatAdapter(header, data, footer);
        GridLayoutManager layout = new GridLayoutManager(this, 2);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return combined.getWrappedAdapterAndPosition(position).first == data
                        ? 1 : layout.getSpanCount();
            }
        });
        RecyclerView recycler = findViewById(R.id.list);
        recycler.setLayoutManager(layout);
        recycler.setAdapter(combined);

        header.submitList(Collections.singletonList(getString(R.string.concat_header)));
        List<String> rows = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            rows.add(getString(R.string.concat_item, i));
        }
        data.submitList(rows);
        footer.submitList(Collections.singletonList(getString(R.string.concat_footer)));
    }

    private static class RowsAdapter extends ListAdapter<String, RowHolder> {
        private final boolean clickable;

        RowsAdapter(boolean clickable) {
            super(new DiffUtil.ItemCallback<String>() {
                @Override public boolean areItemsTheSame(String oldItem, String newItem) {
                    // This sample's labels are unique and immutable; real data should use IDs.
                    return oldItem.equals(newItem);
                }
                @Override public boolean areContentsTheSame(String oldItem, String newItem) {
                    return oldItem.equals(newItem);
                }
            });
            this.clickable = clickable;
        }

        @Override
        public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            RowHolder holder = new RowHolder(view);
            if (clickable) {
                view.setOnClickListener(clicked -> {
                    int position = holder.getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Toast.makeText(clicked.getContext(), clicked.getContext().getString(
                                R.string.concat_clicked, getItem(position), position), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RowHolder holder, int position) {
            holder.text.setText(getItem(position));
        }
    }

    private static class RowHolder extends RecyclerView.ViewHolder {
        final TextView text;
        RowHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(android.R.id.text1);
        }
    }
}
