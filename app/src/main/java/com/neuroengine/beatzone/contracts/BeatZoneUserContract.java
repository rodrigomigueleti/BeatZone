package com.neuroengine.beatzone.contracts;

import android.provider.BaseColumns;
import android.util.Log;

import java.util.Date;

/**
 * Created by usuariodebian on 08/03/17.
 */

public final class BeatZoneUserContract {
    private BeatZoneUserContract() {}

    public static class BZUser implements BaseColumns {
        public static final String TABLE_NAME = "BZUser";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_USER_EMAIL = "user_email";
        public static final String COLUMN_USER_PHOTO_URL = "user_photo_url";
        public static final String COLUMN_USER_BIRTH_DATE = "user_birth_date";
        public static final String COLUMN_USER_AGE = "user_age";
        public static final String COLUMN_USER_HEIGHT = "user_height";
        public static final String COLUMN_USER_WEIGHT = "user_weight";
    }

    public static class BZPractice implements BaseColumns {
        public static final String TABLE_NAME = "BZPractice";
        public static final String COLUMN_TYPE_PRACTICE = "type_practice";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_LOG_THC = "log_thc"; //log for time, heart and coordinate
        public static final String COLUMN_USER_ID = "user_id";

    }

    public static class BZTypePractice implements BaseColumns {
        public static final String TABLE_NAME = "BZTypePractice";
        public static final String COLUMN_TYPE_PRACTICE = "type_practice";
    }
}
