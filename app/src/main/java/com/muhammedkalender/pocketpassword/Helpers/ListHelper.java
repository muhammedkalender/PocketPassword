package com.muhammedkalender.pocketpassword.Helpers;

import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;

import java.security.PublicKey;

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

    public int findIndexById(int id) {
        int index = -1;

        for (int i = 0; i < Global.LIST_PASSWORDS_SOLID.size(); i++) {
            if (Global.LIST_PASSWORDS_SOLID.get(i).getId() == id) {
                index = i;

                break;
            }
        }

        return index;
    }

    public int findTempIndexFromId(int id) {
        int index = -1;

        for (int i = 0; i < Global.LIST_PASSWORDS.size(); i++) {
            if(Global.LIST_PASSWORDS.get(i).getId() == id){
                index = i;
                break;
            }
        }

        return index;
    }

    public PasswordModel findBySelectedId() {
        for (int i = 0; i < Global.LIST_PASSWORDS_SOLID.size(); i++) {
            if (Global.LIST_PASSWORDS_SOLID.get(i).getId() == Global.SELECTED_PASSWORD_ID) {
                return Global.LIST_PASSWORDS_SOLID.get(i);
            }
        }

        return null;
    }

    public boolean findAndDeleteBySelectedIdFromTempList(){
        for (int i = 0; i < Global.LIST_PASSWORDS.size(); i++) {
            if (Global.LIST_PASSWORDS.get(i).getId() == Global.SELECTED_PASSWORD_ID) {
                Global.LIST_PASSWORDS.remove(i);

                return true;
            }
        }

        Helpers.logger.info(Global.SELECTED_PASSWORD_ID, "FADBIFT Bulamadı");

        return false;
    }

    public boolean findAndDeleteBySelectedId(){
        for (int i = 0; i < Global.LIST_PASSWORDS_SOLID.size(); i++) {
            if (Global.LIST_PASSWORDS_SOLID.get(i).getId() == Global.SELECTED_PASSWORD_ID) {
                Helpers.logger.info(Global.LIST_PASSWORDS_SOLID.size()+" Önceki");
                Global.LIST_PASSWORDS_SOLID.remove(i);
                Helpers.logger.info(Global.LIST_PASSWORDS_SOLID.size()+" Sonraki");
                return true;
            }
        }

        Helpers.logger.info(Global.SELECTED_PASSWORD_ID, "FADBI Bulamadı");

        return false;
    }
}
