package com.muhammedkalender.pocketpassword.Helpers;

import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;

public class ListHelper {
    public void findAndUpdate(PasswordModel findPasswordModel) {
        for (int i = 0; i < Global.LIST_PASSWORDS_SOLID.size(); i++) {
            if (Global.LIST_PASSWORDS_SOLID.get(i).getId() == findPasswordModel.getId()) {
                Global.LIST_PASSWORDS_SOLID.set(i, findPasswordModel);

                return;
            }
        }
    }

    //region Find Solid Index

    public int findIndexFromSolid(PasswordModel findPasswordModel) {
        for (int i = 0; i < Global.LIST_PASSWORDS_SOLID.size(); i++) {
            if (Global.LIST_PASSWORDS_SOLID.get(i).getId() == findPasswordModel.getId()) {
                return i;
            }
        }

        return -1;
    }

    public int findIndexFromTempIndex(int tempIndex) {
        int tempId = Global.LIST_PASSWORDS.get(tempIndex).getId();

        for (int i = 0; i < Global.LIST_PASSWORDS_SOLID.size(); i++) {
            if (Global.LIST_PASSWORDS_SOLID.get(i).getId() == tempId) {
                return i;
            }
        }

        return -1;
    }

    //endregion

    public PasswordModel findByGlobal() {
        return Global.LIST_PASSWORDS_SOLID.get(findIndexFromTempIndex(Global.CURRENT_PASSWORD_MODEL_INDEX));
    }

    public void findAndDelete(PasswordModel findPasswordModel) {
        for (int i = 0; i < Global.LIST_PASSWORDS_SOLID.size(); i++) {
            if (Global.LIST_PASSWORDS_SOLID.get(i).getId() == findPasswordModel.getId()) {
                Global.LIST_PASSWORDS_SOLID.remove(i);

                return;
            }
        }
    }
}
