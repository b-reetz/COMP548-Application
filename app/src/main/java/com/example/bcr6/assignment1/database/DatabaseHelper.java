package com.example.bcr6.assignment1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bcr6.assignment1.models.Friend;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Brendan
 *
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for our application
    private static final String DATABASE_NAME = "friends.db";
    // anytime there's a change to the database objects, update this to update everything
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the Horse table
    private Dao<Friend, Integer> friendDao = null;
    private RuntimeExceptionDao<Friend, Integer> friendRuntimeDao = null;

    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Friend.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our Database classes. It will create them or just give the cached
     * values.
     */
    public Dao<Friend, Integer> getFriendDao() throws SQLException {
        if (friendDao == null)
            friendDao = getDao(Friend.class);
        return friendDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for database object classes. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Friend, Integer> getFriendDataDao() {
        if (friendRuntimeDao == null)
            friendRuntimeDao = getRuntimeExceptionDao(Friend.class);
        return friendRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        friendDao = null;
        friendRuntimeDao = null;
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
/*        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Horse.class, true);
            TableUtils.dropTable(connectionSource, Birth.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);


        } catch (SQLException e) {
            //Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        } */
/*        RuntimeExceptionDao<Horse, Integer> horseDAO = getHorseDataDao();

        try {
            //Upgrading to version 3 of the database
            if (oldVersion < 3) {
                horseDao.executeRaw("ALTER TABLE 'horse' ADD COLUMN status INTEGER DEFAULT 0;");
                horseDao.updateRaw("UPDATE 'horse' SET status = 1 WHERE 'birthdate makes them a foal");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }
}