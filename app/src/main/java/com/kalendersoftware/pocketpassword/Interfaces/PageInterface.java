package com.kalendersoftware.pocketpassword.Interfaces;

import android.view.View;

public interface PageInterface {
    public void initialize(View viewRoot);
    public View getView();
    public void refresh();
}
