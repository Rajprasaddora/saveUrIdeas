package com.example.saveurideas;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeAgo {
    public String getTimeAgo(long duration){
        Date date=new Date();
        long seconds= TimeUnit.MILLISECONDS.toSeconds(date.getTime()-duration);
        long minutes=TimeUnit.MILLISECONDS.toMinutes(date.getTime()-duration);
        long hours=TimeUnit.MILLISECONDS.toHours(date.getTime()-duration);
        long days=TimeUnit.MILLISECONDS.toDays(date.getTime()-duration);

        if(seconds< 60){
            return "just now";
        }
        else if(minutes==1){
            return "a minute ago";
        }
        else if(minutes>=1 && minutes<60){
            return minutes + " minutes ago";
        }
        else if(hours==1){
            return "an hour ago";
        }
        else if(hours>=1 && hours<24){
            return hours +" hours ago";
        }
        else if(days==1){
            return "a day ago";
        }
        else {
            return days + " days ago";
        }
    }
}
