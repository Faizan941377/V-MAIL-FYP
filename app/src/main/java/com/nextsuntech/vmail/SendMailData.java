package com.nextsuntech.vmail;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SendMailData {

    @Insert
    void insertrecode(SentMailData sentMailData);

    @Query("select * from SentMailData")
    List<SentMailData> getAllMails();
}
