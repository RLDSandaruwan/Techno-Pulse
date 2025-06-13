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
}
