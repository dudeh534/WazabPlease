package com.ourincheon.wazap.Retrofit;

import java.io.Serializable;

/**
 * Created by sue on 2016-02-24.
 */

public class ContestData  implements Serializable {
    int applies_id;
    int contests_id;
    int recruitment;
    String title;
    String cont_writer;
    String hosts;
    String username;
    String categories;
    // List<CateData>
    String period;
    String cover;
    String positions;
    int members;
    int appliers;
    int clips;
    int views;

    public int getApplies_id() {
        return applies_id;
    }

    public void setApplies_id(int applies_id) {
        this.applies_id = applies_id;
    }

    public int getRecruitment() {
        return recruitment;
    }

    public void setRecruitment(int recruitment) {
        this.recruitment = recruitment;
    }

    public int getContests_id() {
        return contests_id;
    }

    public void setContests_id(int contests_id) {
        this.contests_id = contests_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCont_writer() {
        return cont_writer;
    }

    public void setCont_writer(String cont_writer) {
        this.cont_writer = cont_writer;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getAppliers() {
        return appliers;
    }

    public void setAppliers(int appliers) {
        this.appliers = appliers;
    }

    public int getClips() {
        return clips;
    }

    public void setClips(int clips) {
        this.clips = clips;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getTitle()
    { return title; }


}

