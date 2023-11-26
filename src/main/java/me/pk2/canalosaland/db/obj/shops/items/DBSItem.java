package me.pk2.canalosaland.db.obj.shops.items;

import java.io.Serializable;

public abstract class DBSItem implements Serializable {
    private double price;
    private byte[] material;
    public DBSItem(double price, byte[] material) {
        this.price = price;
        this.material = material;
    }

    public void setPrice(double price) { this.price = price; }
    public double getPrice() { return price; }

    public void setMaterial(byte[] material) { this.material = material; }
    public byte[] getMaterial() { return material; }
}