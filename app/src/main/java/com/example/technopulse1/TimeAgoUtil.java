package com.example.technopulse1;

import android.text.format.DateUtils;

public class TimeAgoUtil {
    public static String getTimeAgo(long timeMillis) {
        return DateUtils.getRelativeTimeSpanString(
                timeMillis,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
        ).toString();
    }

    public static String formatFullDate(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a");
        return sdf.format(new java.util.Date(timestamp));
    }

}

