package bcr6.uow.comp553.assignment1.database;

import android.content.Context;

/**
 * Created by Brendan on 15/01/2017.
 *
 */

class DatabaseManager {

    static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (instance==null)
            instance = new DatabaseManager(ctx);
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private final DatabaseHelper helper;
    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }
}