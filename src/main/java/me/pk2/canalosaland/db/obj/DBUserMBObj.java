package me.pk2.canalosaland.db.obj;

public class DBUserMBObj {
    public final int id;
    public final int uid;
    public final int mid;
    public final int amount;

    public DBUserMBObj(int id, int uid, int mid, int amount) {
        this.id = id;
        this.uid = uid;
        this.mid = mid;
        this.amount = amount;
    }
}