package me.pk2.canalosaland.db.obj;

// Just base object for returning from db
public class DBUserDataObj {
    public final int id;
    public final String job;
    public final String locale;

    public DBUserDataObj(int id, String job, String locale) {
        this.id = id;
        this.job = job;
        this.locale = locale;
    }
}