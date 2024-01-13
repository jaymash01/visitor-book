package com.jaymash.visitorbook.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface VisitorDao {

    @Query("SELECT * FROM visitors ORDER BY id DESC LIMIT :limit OFFSET :offset")
    public List<Visitor> getAll(int limit, int offset);

    @Query("SELECT * FROM visitors WHERE name LIKE :q AND visit_date = :date AND time_out IS NULL LIMIT :limit OFFSET :offset")
    public List<Visitor> getCheckedInVisitors(String q, String date, int limit, int offset);

    @Query("SELECT * FROM visitors WHERE visit_date = :date LIMIT :limit OFFSET :offset")
    public List<Visitor> getWhereVisitDate(String date, int limit, int offset);

    @Query("SELECT COUNT(id) FROM visitors WHERE visit_date = :date")
    public int countWhereVisitDate(String date);

    @Query("SELECT * FROM visitors WHERE visit_date >= :startDate LIMIT :limit OFFSET :offset")
    public List<Visitor> getWhereVisitDateFrom(String startDate, int limit, int offset);

    @Query("SELECT COUNT(*) FROM visitors WHERE visit_date >= :startDate")
    public int countWhereVisitDateFrom(String startDate);

    @Query("SELECT * FROM visitors WHERE id == :id")
    public Visitor getById(int id);

    @Insert(onConflict = REPLACE)
    public void insert(Visitor visitor);

    @Update()
    public void update(Visitor visitor);

    @Delete
    public void delete(Visitor visitor);

    @Query("DELETE FROM visitors")
    public void deleteAll();
}
