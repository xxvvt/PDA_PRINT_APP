package com.example.model;

import android.content.DialogInterface;
import android.view.View;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * @description log_dic_material_information_true
 * @author BEJSON
 * @date 2023-12-25
 */

public class LogDicMaterialInformationTrue  {

    /**
     * id
     */
    @SerializedName("id")
    private String id;

    /**
     * matnr
     */
    @SerializedName("matnr")
    private String matnr;

    /**
     * charg
     */
    @SerializedName("charg")
    private String charg;

    /**
     * maktx
     */
    @SerializedName("maktx")
    private String maktx;

    /**
     * maker
     */
    @SerializedName("maker")
    private String maker;

    /**
     * vfdat
     */
    @SerializedName("vfdat")
    private String vfdat;

    /**
     * fydat
     */
    @SerializedName("fydat")
    private String fydat;

    /**
     * storage
     */
    @SerializedName("storage")
    private String storage;

    /**
     * lfadt
     */
    @SerializedName("lfadt")
    private String lfadt;

    /**
     * mnum
     */
    @SerializedName("mnum")
    private String mnum;

    /**
     * num
     */
    @SerializedName("num")
    private String num;

    /**
     * matyp
     */
    @SerializedName("matyp")
    private String matyp;

    /**
     * input_time
     */
    @SerializedName("inputTime")
    private Date inputTime;

    /**
     * input_user
     */
    @SerializedName("inputUser")
    private String inputUser;

    /**
     * update_time
     */
    @SerializedName("updateTime")
    private Date updateTime;

    /**
     * update_user
     */
    @SerializedName("updateUser")
    private String updateUser;

    private String printTime;

    private String status;

    /**
     * l
     */
    DialogInterface.OnClickListener canback;

    public LogDicMaterialInformationTrue() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getCharg() {
        return charg;
    }

    public void setCharg(String charg) {
        this.charg = charg;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getVfdat() {
        return vfdat;
    }

    public void setVfdat(String vfdat) {
        this.vfdat = vfdat;
    }

    public String getFydat() {
        return fydat;
    }

    public void setFydat(String fydat) {
        this.fydat = fydat;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getLfadt() {
        return lfadt;
    }

    public void setLfadt(String lfadt) {
        this.lfadt = lfadt;
    }

    public String getMnum() {
        return mnum;
    }

    public void setMnum(String mnum) {
        this.mnum = mnum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMatyp() {
        return matyp;
    }

    public void setMatyp(String matyp) {
        this.matyp = matyp;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getInputUser() {
        return inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public DialogInterface.OnClickListener getCanback() {
        return canback;
    }

    public void setCanback(DialogInterface.OnClickListener canback) {
        this.canback = canback;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}