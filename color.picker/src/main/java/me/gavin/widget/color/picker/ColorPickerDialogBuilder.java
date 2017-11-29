package me.gavin.widget.color.picker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * ColorPickerDialogBuilder
 *
 * @author gavin.xiong 2017/11/29
 */
public class ColorPickerDialogBuilder {

    private final Context mContext;
    private final AlertDialog.Builder builder;

    private HSLColorPicker picker;

    private ColorPickerDialogBuilder(Context context, int themeResId) {
        this.mContext = context;
        FrameLayout parent = new FrameLayout(context);
        int padding = DisplayUtil.dp2px(24);
        parent.setPadding(padding, padding, padding, padding);
        picker = new HSLColorPicker(context);
        parent.addView(picker);
        this.builder = new AlertDialog.Builder(context, themeResId)
                .setView(parent);
    }

    public static ColorPickerDialogBuilder with(Context context) {
        return with(context, 0);
    }

    public static ColorPickerDialogBuilder with(Context context, int themeResId) {
        return new ColorPickerDialogBuilder(context, themeResId);
    }

    public ColorPickerDialogBuilder setTitle(String title) {
        builder.setTitle(title);
        return this;
    }

    public ColorPickerDialogBuilder setColor(int color) {
        // TODO: 2017/11/29
        return this;
    }

    public ColorPickerDialogBuilder setInputButton(CharSequence text, final OnColorSelectedListener listener) {
        builder.setNeutralButton(text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FrameLayout parent = new FrameLayout(mContext);
                int padding = DisplayUtil.dp2px(24);
                parent.setPadding(padding, padding, padding, padding);
                EditText editText = new EditText(mContext);
                editText.setHint("#26A69A  |  #80000000");
                editText.setFilters(new InputFilter[]{new ColorInputFilter()});
                parent.addView(editText);
                new AlertDialog.Builder(mContext)
                        .setTitle("选择颜色")
                        .setView(parent)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onColorSelected(dialog, 0xFFFF0000);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        return this;
    }

    public ColorPickerDialogBuilder setPositiveButton(CharSequence text, final OnColorSelectedListener listener) {
        builder.setPositiveButton(text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onColorSelected(dialog, 0xFFFF0000);
            }
        });
        return this;
    }

    public ColorPickerDialogBuilder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
        builder.setNegativeButton(text, listener);
        return this;
    }

    public ColorPickerDialogBuilder setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
        builder.setNeutralButton(text, listener);
        return this;
    }

    public AlertDialog show() {
        return builder.show();
    }

}
