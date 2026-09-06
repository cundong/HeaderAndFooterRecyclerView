package com.cundong.recyclerview.sample;

import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowToast;
import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 35)
@LooperMode(LooperMode.Mode.PAUSED)
public class ConcatExampleActivityTest {
    private ActivityController<ConcatExampleActivity> controller;
    private ConcatExampleActivity activity;
    private RecyclerView recycler;

    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(ConcatExampleActivity.class).setup();
        activity = controller.get();
        recycler = activity.findViewById(R.id.list);
        layoutList();
    }

    @After
    public void tearDown() {
        controller.pause().stop().destroy();
    }

    @Test
    public void headerAndFooterSpanGridWhileDataUsesOneColumn() {
        assertTrue(recycler.getAdapter() instanceof ConcatAdapter);
        assertEquals(32, recycler.getAdapter().getItemCount());
        GridLayoutManager layout = (GridLayoutManager) recycler.getLayoutManager();
        assertEquals(2, layout.getSpanSizeLookup().getSpanSize(0));
        assertEquals(1, layout.getSpanSizeLookup().getSpanSize(1));
        assertEquals(1, layout.getSpanSizeLookup().getSpanSize(30));
        assertEquals(2, layout.getSpanSizeLookup().getSpanSize(31));
        TextView header = recycler.findViewHolderForAdapterPosition(0).itemView
                .findViewById(android.R.id.text1);
        assertEquals(activity.getString(R.string.concat_header), header.getText().toString());
        recycler.scrollToPosition(31);
        layoutList();
        TextView footer = recycler.findViewHolderForAdapterPosition(31).itemView
                .findViewById(android.R.id.text1);
        assertEquals(activity.getString(R.string.concat_footer), footer.getText().toString());
    }

    @Test
    public void dataClickReportsLocalPositionDespiteHeader() {
        RecyclerView.ViewHolder firstData = recycler.findViewHolderForAdapterPosition(1);
        assertNotNull(firstData);
        assertEquals(0, firstData.getBindingAdapterPosition());
        firstData.itemView.performClick();
        assertEquals(activity.getString(R.string.concat_clicked,
                activity.getString(R.string.concat_item, 1), 0), ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void activityRecreationRestoresCompositionAndClicks() {
        controller.recreate();
        activity = controller.get();
        recycler = activity.findViewById(R.id.list);
        layoutList();
        assertEquals(32, recycler.getAdapter().getItemCount());
        dataClickReportsLocalPositionDespiteHeader();
    }

    @Test
    public void repeatedInsetsAndContentReplacementDoNotAccumulatePadding() {
        View content = activity.findViewById(android.R.id.content);
        // The activity may already have received real platform insets during setup.
        // Measure base padding after removing those before injecting test insets.
        ViewCompat.dispatchApplyWindowInsets(content, new WindowInsetsCompat.Builder().build());
        int left = content.getPaddingLeft();
        int top = content.getPaddingTop();
        int right = content.getPaddingRight();
        int bottom = content.getPaddingBottom();
        WindowInsetsCompat insets = new WindowInsetsCompat.Builder()
                .setInsets(WindowInsetsCompat.Type.systemBars(), Insets.of(4, 24, 4, 48))
                .build();
        ViewCompat.dispatchApplyWindowInsets(content, insets);
        ViewCompat.dispatchApplyWindowInsets(content, insets);
        assertEquals(left + 4, content.getPaddingLeft());
        assertEquals(top + 24, content.getPaddingTop());
        assertEquals(right + 4, content.getPaddingRight());
        assertEquals(bottom + 48, content.getPaddingBottom());
        activity.setContentView(R.layout.sample_activity);
        ViewCompat.dispatchApplyWindowInsets(content, insets);
        assertEquals(top + 24, content.getPaddingTop());
        assertEquals(bottom + 48, content.getPaddingBottom());
    }

    private void layoutList() {
        shadowOf(Looper.getMainLooper()).idle();
        recycler.measure(View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY));
        recycler.layout(0, 0, 1080, 1920);
        shadowOf(Looper.getMainLooper()).idle();
    }
}
