package com.kalendersoftware.pocketpassword.Abstracts;

import android.view.View;

public abstract class PageAbstract {
    protected View viewRoot = null;
    protected boolean initialized = false;

    public boolean isInitialized() {
        return this.initialized;
    }
}
