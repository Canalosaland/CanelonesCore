package me.pk2.canalosaland.db.obj.mb.action;

import java.io.Serializable;

public class MBAItem extends MysteryBoxAction implements Serializable {
    private byte[] item;
    public MBAItem(byte[] material, byte[] item) {
        super("item", material);
        this.item = item;
    }

    public byte[] getItem() { return item; }
    public void setItem(byte[] item) { this.item = item; }
}