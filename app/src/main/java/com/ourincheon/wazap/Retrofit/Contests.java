package com.ourincheon.wazap.Retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsue on 16. 2. 23.
 */
public class Contests
{
    boolean result;
    String msg;
    List<ContestData> data = new ArrayList<ContestData>();

    public boolean isResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }


}

class ContestData{
    int contests_id;
    String title;
    String cont_writer;
    String hosts;
    String categories;
    // List<CateData>
    String period;
    String cover;
    String positions;
    int members;
    int appliers;
    int clips;
    int views;
}
