package com.nextsuntech.vmail;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {SentMailData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SendMailData sendMailData();
}
