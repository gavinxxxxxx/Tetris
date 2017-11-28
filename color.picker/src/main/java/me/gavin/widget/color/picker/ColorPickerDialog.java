package me.gavin.widget.color.picker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.FrameLayout;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/11/28
 */
public class ColorPickerDialog extends AlertDialog {

    public ColorPickerDialog(Context context) {
        super(context);
    }

    public static class Builder {

        private final AlertDialog.Builder builder;

        public Builder(Context context) {
            FrameLayout parent = new FrameLayout(context);
            int padding = DisplayUtil.dp2px(24);
            parent.setPadding(padding, padding, padding, padding);
            HSLColorPicker picker = new HSLColorPicker(context);
            parent.addView(picker);
            this.builder = new AlertDialog.Builder(context)
                    .setView(parent);
        }

        public Builder(Context context, int themeResId) {
            this.builder = new AlertDialog.Builder(context, themeResId);
        }

        public Builder setTitle(CharSequence title) {
            builder.setTitle(title);
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            builder.setPositiveButton(text, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            builder.setNegativeButton(text, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return this;
        }

        public Builder setN(CharSequence text, final OnClickListener listener) {
            builder.setNeutralButton(text, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return this;
        }

        public AlertDialog show() {
            return builder.show();
        }

    }

    public interface OnColorSeletedListener {
        void onColorSelected(DialogInterface dialog, int color);
    }
}
