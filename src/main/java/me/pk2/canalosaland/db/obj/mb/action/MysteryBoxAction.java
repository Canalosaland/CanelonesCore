package me.pk2.canalosaland.db.obj.mb.action;

import java.io.Serializable;

public abstract class MysteryBoxAction implements Serializable {
    private String type;
    private byte[] material;
    public MysteryBoxAction(String type, byte[] material) {
        this.type = type;
        this.material = material;
    }

    public String getType() { return type; }

    public byte[] getMaterial() { return material; }
    public void setMaterial(byte[] material) { this.material = material; }
}