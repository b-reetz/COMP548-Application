package com.example.bcr6.assignment1.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by bcr6 on 3/2/17.
 * Database table mapping to class
 */

@DatabaseTable(tableName = "friend")
public class Friend {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    String name;
    @DatabaseField
    String imagePath;

    public Friend(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }


}
