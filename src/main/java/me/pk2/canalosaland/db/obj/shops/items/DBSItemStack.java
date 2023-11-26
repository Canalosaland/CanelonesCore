package me.pk2.canalosaland.db.obj.shops.items;

import java.io.Serializable;

public class DBSItemStack extends DBSItem implements Serializable {
    private byte[] itemStack;
    public DBSItemStack(double price, byte[] material, byte[] itemStack) {
        super(price, material);

        this.itemStack = itemStack;
    }

    public void setItemStack(byte[] itemStack) { this.itemStack = itemStack; }
    public byte[] getItemStack() { return itemStack; }
}
