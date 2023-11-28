package me.pk2.canalosaland.db.buffer.shop.items;

import me.pk2.canalosaland.db.obj.shops.items.DBSItem;
import me.pk2.canalosaland.db.obj.shops.items.DBSItemCommand;
import me.pk2.canalosaland.util.BukkitSerialization;

public class DBBSShopItemCommand extends DBBSShopItem {
    private final DBSItemCommand dbsItemCommand;
    private String command;
    public DBBSShopItemCommand(DBSItemCommand dbsItemCommand) {
        super(dbsItemCommand);

        this.dbsItemCommand = dbsItemCommand;
        this.command = dbsItemCommand.getCommand();
    }

    public DBSItemCommand getDbsItemCommand() { return dbsItemCommand; }

    public void setCommand(String command) { this.command = command; }
    public String getCommand() { return command; }

    @Override
    public void updateLocal() {
        setPrice(dbsItemCommand.getPrice());
        setMaterial(BukkitSerialization.deserializeItems(dbsItemCommand.getMaterial())[0]);

        this.command = dbsItemCommand.getCommand();
    }

    @Override
    public void updateDBS() {
        byte[] bytes = BukkitSerialization.serializeItems(getMaterial());

        dbsItemCommand.setPrice(getPrice());
        dbsItemCommand.setMaterial(bytes);
        dbsItemCommand.setCommand(command);
    }

    @Override
    public DBSItem asDBS() { return dbsItemCommand; }
}