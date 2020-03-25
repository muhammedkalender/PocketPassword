package com.muhammedkalender.pocketpassword.Components;

import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.button.MaterialButton;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.R;

import java.util.Random;

public class ColorPickerComponent {
    //region Variables

    private ColorPickerItemComponent[] arrColorPickerItemComponent;
    private int selected;
    private int indexSelected = -1;
    private ViewGroup viewGroup;
    private LinearLayout parent;
    private MaterialButton button;

    private View.OnClickListener clickSelect;

    //endregion

    //region Constructors

    public ColorPickerComponent(ViewGroup viewGroup, int selected, MaterialButton button) {
        this.selected = selected;
        this.viewGroup = viewGroup;
        this.button = button;

        if(this.selected < 0){
            this.selected *= -1;
        }

        initialize();
    }

    //endregion

    //region Initialize

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

    //endregion

    //region Loader

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

    public void refresh(int selected){
        this.selected = selected;
        this.indexSelected = -1;

        this.initialize();
        this.fillLayout(parent);
    }

    //endregion

    //region Getters & Setters

    //region Getters

    public int getIndexSelected() {
        return indexSelected;
    }

    public int getColor(){
        return ColorConstants.colorItem[indexSelected].getColor();
    }

    public int getTintColor(){
        return ColorConstants.colorItem[indexSelected].getTint();
    }

    //endregion

    //endregion

    //region Secondary Functions

    public int getRandomColor(){
        try{
            Random random = new Random();
            return ColorConstants.colorItem[random.nextInt(ColorConstants.colorItem.length)].getColor();
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.COLOR_PICKED_RANDOM_COLOR, e);

            return -1;
        }
    }

    //endregion
}
