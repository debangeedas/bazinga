package com.example.covidtest.data;

import android.provider.BaseColumns;

public final class TestContract {

    private TestContract() {
    }

    public static final class CovidEntry implements BaseColumns {
        public final static String TABLE_NAME = "covidData";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_RESULT = "result";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_DATE_TIME = "dateTime";
    }

    public static final class EcgEntry implements BaseColumns {
        public final static String TABLE_NAME = "ecgData";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE_TIME = "dateTime";
        public final static String COLUMN_IMAGE = "image";
    }

    public static final class DiabetesEntry implements BaseColumns {
        public final static String TABLE_NAME = "diabetesData";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GLUCOSE_LEVEL = "glucoseLevel";
        public final static String COLUMN_DATE_TIME = "dateTime";
    }
}
