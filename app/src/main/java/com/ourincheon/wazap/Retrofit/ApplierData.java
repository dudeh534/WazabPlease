package com.ourincheon.wazap.Retrofit;

/**
 * Created by hsue on 16. 2. 26.
 */
public class ApplierData {
    String postdate;
    int appliers_id;
    String app_users_id;
    int is_check;
    String username;

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public int getAppliers_id() {
        return appliers_id;
    }

    public void setAppliers_id(int appliers_id) {
        this.appliers_id = appliers_id;
    }

    public String getApp_users_id() {
        return app_users_id;
    }

    public void setApp_users_id(String app_users_id) {
        this.app_users_id = app_users_id;
    }

    public int getIs_check() {
        return is_check;
    }

    public void setIs_check(int is_check) {
        this.is_check = is_check;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    String profile_img;
}
