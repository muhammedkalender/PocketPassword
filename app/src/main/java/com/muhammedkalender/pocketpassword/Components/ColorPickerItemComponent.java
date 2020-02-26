package com.muhammedkalender.pocketpassword.Components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.R;

public class ColorPickerItemComponent {
    private ViewGroup viewParent;
    private int color;
    private boolean selected;
    private View view;
    private View.OnClickListener clickSelect;
    private int index;

    public ColorPickerItemComponent(
            ViewGroup viewParent,
            int color,
            boolean selected,
            View.OnClickListener clickSelect,
            int index)
    {
        this.viewParent = viewParent;
        this.color = color;
        this.selected = selected;
        this.clickSelect = clickSelect;
        this.index = index;

        initialize();
    }

    public ColorPickerItemComponent initialize() {
        LayoutInflater layoutInflater = (LayoutInflater) Global.CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.view = layoutInflater.inflate(R.layout.item_list_color, viewParent, false);

        if (this.selected) {
            this.view.findViewById(R.id.viewColor).setBackgroundColor(color);
            this.view.findViewById(R.id.viewFrame).setBackground(Helpers.resource.getDrawable(R.drawable.button_color_with_border));
        } else {
            view.findViewById(R.id.viewColor).setBackgroundColor(color);
            this.view.findViewById(R.id.viewFrame).setBackgroundColor(Color.TRANSPARENT);
        }

        this.view.setClickable(true);
        this.view.setOnClickListener(clickSelect);

        this.view.setTag(index);

        return this;
    }

    public View getView() {
        return this.view;
    }

    public void setSelected() {
        Helpers.logger.info("SELECTED" + index);

        this.view.findViewById(R.id.viewFrame).setBackground(Helpers.resource.getDrawable(R.drawable.button_color_with_border));
    }

    public void setUnSelected() {
        Helpers.logger.info("UNSELECT" + index);

        this.view.findViewById(R.id.viewFrame).setBackgroundColor(Color.TRANSPARENT);
    }

}
