package me.pk2.canalosaland.db.obj.shops.items;

import java.io.Serializable;

public class DBSItemCommand extends DBSItem implements Serializable {
    private String command;
    public DBSItemCommand(double price, byte[] material, String command) {
        super(price, material);

        this.command = command;
    }

    public void setCommand(String command) { this.command = command; }
    public String getCommand() { return command; }
}