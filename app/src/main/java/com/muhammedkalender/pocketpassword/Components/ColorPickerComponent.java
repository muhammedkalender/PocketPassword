package com.muhammedkalender.pocketpassword.Components;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.button.MaterialButton;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Objects.ColorObject;
import com.muhammedkalender.pocketpassword.R;

public class ColorPickerComponent {

    private ColorPickerItemComponent[] arrColorPickerItemComponent;
    private int selected;
    private int indexSelected = -1;
    private ViewGroup viewGroup;
    private LinearLayout parent;
    private MaterialButton button;

    private View.OnClickListener clickSelect;

    public ColorPickerComponent(ViewGroup viewGroup, int selected, MaterialButton button) {
        this.selected = selected;
        this.viewGroup = viewGroup;
        this.button = button;

        if(this.selected < 0){
            this.selected *= -1;
        }

        initialize();
    }

    private void initialize() {
        if (indexSelected == -1) {
            indexSelected = 0;

            selected = Math.abs(selected);

            for(int i = 0; i < ColorConstants.colorItem.length; i++){
                if(selected == Math.abs(ColorConstants.colorItem[i].getColor())){
                    indexSelected = i;
                    break;
                }
            }

            button.setBackgroundColor(ColorConstants.colorItem[indexSelected].getColor());
            button.setTextColor(ColorConstants.colorItem[indexSelected].getTint());
        }else{
            button.setBackgroundColor(Helpers.resource.getColor(R.color.lightBlue));
            button.setTextColor(Helpers.resource.getColor(R.color.tintLightBlue));
        }

        this.clickSelect = v -> {
            int index = (int) v.getTag();

            arrColorPickerItemComponent[indexSelected].setUnSelected();
            arrColorPickerItemComponent[index].setSelected();

            button.setBackgroundColor(ColorConstants.colorItem[index].getColor());
            button.setTextColor(ColorConstants.colorItem[index].getTint());

            indexSelected = index;
        };
    }

    public void fillLayout(LinearLayout parent) {
        arrColorPickerItemComponent = new ColorPickerItemComponent[ColorConstants.colorItem.length];

        parent.removeAllViews();

        parent.post(() -> parent.setScrollX(0));

        for (int i = 0; i < arrColorPickerItemComponent.length; i++) {
            ColorPickerItemComponent colorPickerItemComponent = new ColorPickerItemComponent(viewGroup, ColorConstants.colorItem[i].getColor(), ColorConstants.colorItem[i].getColor() == selected, clickSelect, i);

            arrColorPickerItemComponent[i] = colorPickerItemComponent;

            parent.addView(arrColorPickerItemComponent[i].getView());

            if (selected == Math.abs(ColorConstants.colorItem[i].getColor())) {
                arrColorPickerItemComponent[i].setSelected();

                this.indexSelected = i;
            }
        }

        if (indexSelected != -1) {
            RelativeLayout.LayoutParams paramBase = (RelativeLayout.LayoutParams) ((RelativeLayout) arrColorPickerItemComponent[0].getView()).getChildAt(0).getLayoutParams();

            int sizeItem = paramBase.width + paramBase.leftMargin + paramBase.rightMargin;

            ((HorizontalScrollView) parent.getParent()).post(() -> ((HorizontalScrollView) parent.getParent()).scrollTo((sizeItem * indexSelected) - (sizeItem / 3), 100));
        }


        this.parent = parent;
    }

    public int getIndexSelected() {
        return indexSelected;
    }

    public int getColor(){
        return ColorConstants.colorItem[indexSelected].getColor();
    }

    public int getTintColor(){
        return ColorConstants.colorItem[indexSelected].getTint();
    }

    public void refresh(int selected){
        this.selected = selected;
        this.indexSelected = -1;

        this.initialize();
        this.fillLayout(parent);
    }
}
