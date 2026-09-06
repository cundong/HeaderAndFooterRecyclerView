package com.cundong.recyclerview.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by cundong on 2015/11/10.
 * <p/>
 * Sample入口
 */
public class MainActivity extends BaseSampleActivity {

    private static final Class<?>[] ACTIVITY = {ConcatExampleActivity.class, LinearLayoutActivity.class, EndlessLinearLayoutActivity.class, EndlessGridLayoutActivity.class, EndlessStaggeredGridLayoutActivity.class};
    private static final int[] TITLE = {
            R.string.concat_example_title,
            R.string.legacy_header_footer_title,
            R.string.legacy_linear_title,
            R.string.legacy_grid_title,
            R.string.legacy_staggered_title
    };

    private RecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;
    private ArrayList<ListItem> mDataList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDataList = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {

            ListItem item = new ListItem();
            item.title = getString(TITLE[i]);
            item.activity = ACTIVITY[i];
            mDataList.add(item);
        }

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.setData(mDataList);
        mRecyclerView.setAdapter(mDataAdapter);
    }

    private static class ListItem {
        public String title;
        public Class<?> activity;
    }

    private class DataAdapter extends RecyclerView.Adapter {

        private LayoutInflater mLayoutInflater;
        private ArrayList<ListItem> mDataList = new ArrayList<>();

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        public void setData(ArrayList<ListItem> list) {
            this.mDataList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.sample_item_button, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ListItem listItem = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.button.setText(listItem.title);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private Button button;

            public ViewHolder(View itemView) {
                super(itemView);
                button = (Button) itemView.findViewById(R.id.button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = getBindingAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return;
                        }
                        ListItem listItem = mDataList.get(position);
                        startActivity(new Intent(MainActivity.this, listItem.activity));
                    }
                });
            }
        }
    }
}
