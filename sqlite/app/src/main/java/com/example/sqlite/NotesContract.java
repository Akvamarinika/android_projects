package com.example.sqlite;

import android.provider.BaseColumns;

import java.util.Date;

/*class - contract keep info about DB: имя табл, имена столбцов... */
public class NotesContract {
    /*кол-во подкласов задется, смотря сколько табл. нужно*/
    public static final class NotesEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DAY_OF_WEEK = "day_of_week";
        public static final String COLUMN_PRIORITY = "priority";

        public static final String TYPE_TEXT = "TEXT";
        public static final String TYPE_INTEGER = "INTEGER";
        public static final String TYPE_DATE = "DATE";

        public static final String CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " " + TYPE_DATE + ", " +
                COLUMN_TITLE + " " + TYPE_TEXT + ", " +
                COLUMN_DESCRIPTION + " " + TYPE_TEXT + ", " +
                COLUMN_DAY_OF_WEEK + " " + TYPE_TEXT + ", " +
                COLUMN_PRIORITY + " " + TYPE_INTEGER + ");";

        public static final String DROP_COMMAND = "DROP TABLE IF EXIST " + TABLE_NAME;
    }
}
