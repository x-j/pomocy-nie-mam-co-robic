package com.example.xj.pomocyniemamcorobic;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xj on 04/06/2016.
 */
public class MyCalendar {

    static ArrayList<Excuse>  content = new ArrayList<>();
    static String nickname;

}

class Excuse {
    int day;
    String description;
    Date begin;
    Date end;
    String location;
}