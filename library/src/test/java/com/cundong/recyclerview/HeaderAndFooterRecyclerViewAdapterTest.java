package com.cundong.recyclerview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class HeaderAndFooterRecyclerViewAdapterTest {
    private DataAdapter inner;
    private HeaderAndFooterRecyclerViewAdapter wrapper;
    private View header;
    private View footer;
    private FrameLayout parent;

    @Before
    public void setUp() {
        inner = new DataAdapter();
        wrapper = new HeaderAndFooterRecyclerViewAdapter(inner);
        header = new View(RuntimeEnvironment.getApplication());
        footer = new View(RuntimeEnvironment.getApplication());
        parent = new FrameLayout(RuntimeEnvironment.getApplication());
        wrapper.addHeaderView(header);
        wrapper.addFooterView(footer);
    }

    @Test
    public void wrapsHeaderDataAndFooterInOrder() {
        assertEquals(5, wrapper.getItemCount());
        assertSame(header, wrapper.onCreateViewHolder(parent, wrapper.getItemViewType(0)).itemView);
        assertSame(footer, wrapper.onCreateViewHolder(parent, wrapper.getItemViewType(4)).itemView);
        RecyclerView.ViewHolder holder = wrapper.onCreateViewHolder(parent, wrapper.getItemViewType(2));
        wrapper.onBindViewHolder(holder, 2);
        assertEquals(1, inner.boundPosition);
        assertEquals(7, inner.createdType);
    }

    @Test
    public void removingDecorationsRestoresDataCount() {
        wrapper.removeHeaderView(header);
        wrapper.removeFooterView(footer);
        assertEquals(3, wrapper.getItemCount());
        assertNull(wrapper.getHeaderView());
        assertNull(wrapper.getFooterView());
    }

    @Test
    public void gridDecorationsSpanAllColumns() {
        HeaderSpanSizeLookup lookup = new HeaderSpanSizeLookup(wrapper, 3);
        assertEquals(3, lookup.getSpanSize(0));
        assertEquals(1, lookup.getSpanSize(1));
        assertEquals(3, lookup.getSpanSize(4));
    }

    @Test
    public void forwardsInsertRemoveChangeAndMoveWithHeaderOffset() {
        List<String> events = new ArrayList<>();
        wrapper.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onItemRangeInserted(int start, int count) {
                events.add("insert:" + start + ":" + count);
            }
            @Override public void onItemRangeRemoved(int start, int count) {
                events.add("remove:" + start + ":" + count);
            }
            @Override public void onItemRangeChanged(int start, int count) {
                events.add("change:" + start + ":" + count);
            }
            @Override public void onItemRangeMoved(int from, int to, int count) {
                events.add("move:" + from + ":" + to + ":" + count);
            }
        });
        inner.count++;
        inner.notifyItemInserted(1);
        inner.count--;
        inner.notifyItemRemoved(1);
        inner.notifyItemChanged(0);
        inner.notifyItemMoved(0, 2);
        assertEquals(java.util.Arrays.asList("insert:2:1", "remove:2:1", "change:1:1", "move:1:3:1"), events);
    }

    @Test
    public void preservesPayloadForNotificationsAndBinding() {
        Object payload = new Object();
        List<Object> observed = new ArrayList<>();
        wrapper.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onItemRangeChanged(int start, int count, Object value) {
                assertEquals(2, start);
                assertEquals(1, count);
                observed.add(value);
            }
        });
        inner.notifyItemChanged(1, payload);
        assertEquals(Collections.singletonList(payload), observed);
        RecyclerView.ViewHolder holder = wrapper.onCreateViewHolder(parent, wrapper.getItemViewType(2));
        wrapper.onBindViewHolder(holder, 2, Collections.singletonList(payload));
        assertEquals(1, inner.boundPosition);
        assertEquals(Collections.singletonList(payload), inner.payloads);
    }

    @Test
    public void replacingAdapterUnregistersOldObserver() {
        DataAdapter replacement = new DataAdapter();
        wrapper.setAdapter(replacement);
        List<String> events = new ArrayList<>();
        wrapper.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onChanged() { events.add("changed"); }
        });
        inner.notifyDataSetChanged();
        assertTrue(events.isEmpty());
        replacement.notifyDataSetChanged();
        assertEquals(Collections.singletonList("changed"), events);
    }

    @Test
    public void invalidHolderPositionsKeepNoPositionSentinel() {
        RecyclerView recycler = new RecyclerView(RuntimeEnvironment.getApplication());
        recycler.setAdapter(wrapper);
        RecyclerView.ViewHolder holder = new HeaderAndFooterRecyclerViewAdapter.ViewHolder(
                new View(RuntimeEnvironment.getApplication()));
        assertEquals(RecyclerView.NO_POSITION, RecyclerViewUtils.getAdapterPosition(recycler, holder));
        assertEquals(RecyclerView.NO_POSITION, RecyclerViewUtils.getLayoutPosition(recycler, holder));
    }

    private static class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        int count = 3;
        int boundPosition = -1;
        int createdType = -1;
        List<Object> payloads;
        @Override public int getItemCount() { return count; }
        @Override public int getItemViewType(int position) { return 7; }
        @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
            createdType = type;
            return new HeaderAndFooterRecyclerViewAdapter.ViewHolder(new View(parent.getContext()));
        }
        @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            boundPosition = position;
        }
        @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> values) {
            boundPosition = position;
            payloads = values;
        }
    }
}
