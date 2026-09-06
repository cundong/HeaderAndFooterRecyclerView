package com.cundong.recyclerview.sample;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/** Applies system-bar and cutout insets to the sample content. */
public abstract class BaseSampleActivity extends AppCompatActivity {
    private boolean capturedPadding;
    private int left;
    private int top;
    private int right;
    private int bottom;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        View content = findViewById(android.R.id.content);
        if (!capturedPadding) {
            left = content.getPaddingLeft();
            top = content.getPaddingTop();
            right = content.getPaddingRight();
            bottom = content.getPaddingBottom();
            capturedPadding = true;
        }
        ViewCompat.setOnApplyWindowInsetsListener(content, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            view.setPadding(left + insets.left, top + insets.top,
                    right + insets.right, bottom + insets.bottom);
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(content);
    }
}
