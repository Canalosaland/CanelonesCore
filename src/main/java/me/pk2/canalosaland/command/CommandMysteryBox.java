package me.pk2.canalosaland.command;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.db.DBApi;
import me.pk2.canalosaland.db.buffer.DBBufferMB;
import me.pk2.canalosaland.db.obj.DBUserMBObj;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxLocationObj;
import me.pk2.canalosaland.db.obj.mb.DBMysteryBoxObj;
import me.pk2.canalosaland.db.obj.mb.action.MBACommand;
import me.pk2.canalosaland.db.obj.mb.action.MBAItem;
import me.pk2.canalosaland.db.obj.mb.action.MysteryBoxAction;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import me.pk2.canalosaland.util.BukkitSerialization;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

import static me.pk2.canalosaland.util.Wrapper.*;

public class CommandMysteryBox implements CommandExecutor {
    private void sendHelp(CommandSender player, int id) {
        switch (id) {
            case 1: // Main
                player.sendMessage(_COLOR("&7&l&m_________&d &lMystery&dBox &7&l&m_________"));
                player.sendMessage(_COLOR("&8&l> &e/mb box"));
                player.sendMessage(_COLOR("&8&l> &e/mb location"));
                player.sendMessage(_COLOR("&8&l> &e/mb user"));
                player.sendMessage(_COLOR("&7&l_______________________________"));
                break;
            case 2: // Box
                player.sendMessage(_COLOR("&7&l&m_________&d &lMystery&dBox &7&l&m_________"));
                player.sendMessage(_COLOR("&8&l> &e/mb box list"));
                player.sendMessage(_COLOR("&8&l> &e/mb box create <name>"));
                player.sendMessage(_COLOR("&8&l> &e/mb box remove <id>"));
                player.sendMessage(_COLOR("&8&l> &e/mb box name <id> <name>"));
                player.sendMessage(_COLOR("&8&l> &e/mb box icon <id>"));
                player.sendMessage(_COLOR("&8&l> &e/mb box slot list <id>"));
                player.sendMessage(_COLOR("&8&l> &e/mb box slot add <id> <item/cmd> [cmd]"));
                player.sendMessage(_COLOR("&8&l> &e/mb box slot rem <id> <slot>"));
                player.sendMessage(_COLOR("&8&l> &e/mb box slot force <id> <slot>"));
                player.sendMessage(_COLOR("&7&l_______________________________"));
                break;
            case 3:
                player.sendMessage(_COLOR("&7&l&m_________&d &lMystery&dBox &7&l&m_________"));
                player.sendMessage(_COLOR("&8&l> &e/mb location list"));
                player.sendMessage(_COLOR("&8&l> &e/mb location create"));
                player.sendMessage(_COLOR("&8&l> &e/mb location delete <id>"));
                player.sendMessage(_COLOR("&7&l_______________________________"));
                break;
            case 4:
                player.sendMessage(_COLOR("&7&l&m_________&d &lMystery&dBox &7&l&m_________"));
                player.sendMessage(_COLOR("&8&l> &e/mb user list <player>"));
                player.sendMessage(_COLOR("&8&l> &e/mb user give <player> <id> [amount]"));
                player.sendMessage(_COLOR("&8&l> &e/mb user take <player> <id> [amount]"));
                player.sendMessage(_COLOR("&8&l> &e/mb user giveall <id> [amount]"));
                player.sendMessage(_COLOR("&7&l_______________________________"));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("canalosaland.mysterybox")) {
            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_NO_PERMISSION"));
            return true;
        }

        if(args.length < 1) {
            sendHelp(sender, 1);
            return true;
        }

        if(args[0].equalsIgnoreCase("box")) {
            if(!(sender instanceof Player player)) {
                sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_ONLY_PLAYER"));
                return true;
            }

            User user = UserManager.get(player);
            if(user == null) {
                player.sendMessage(_SENDER_TRANSLATE(player, "COMMAND_USER_NOT_FOUND"));
                return true;
            }

            if(args.length < 2) {
                sendHelp(player, 2);
                return true;
            }

            switch (args[1].toLowerCase()) {
                case "list" -> {
                    if (DBBufferMB.BUFFER.getTypes().length == 0) {
                        user.sendLocale("COMMAND_MBA_BOXES_EMPTY");
                        return true;
                    }

                    player.sendMessage(_COLOR("&6MysteryBoxes:"));
                    for (DBMysteryBoxObj obj : DBBufferMB.BUFFER.getTypes())
                        player.sendMessage(_COLOR(" &7- &8id: &f" + obj.getId() + "&8, name: '&f" + obj.getName() + "&8'"));
                }
                case "create" -> {
                    if (args.length < 3) {
                        sendHelp(sender, 2);
                        return true;
                    }

                    StringBuilder builder = new StringBuilder();
                    for (int i = 2; i < args.length; i++)
                        builder.append(args[i] + " ");

                    user.sendLocale("COMMAND_MBA_CREATING");

                    DBMysteryBoxObj temp = genTempMB(player, builder.toString().trim());
                    DBApi.enqueue(() -> {
                        Connection conn = DBApi.connect();

                        int ex = DBApi.API.mystery_boxes.register(conn, temp);
                        if (ex != 1) {
                            user.sendLocale("COMMAND_MBA_CREATING_ERROR");
                            return;
                        }

                        DBBufferMB.BUFFER.updateMysteryBoxes();
                        DBApi.disconnect(conn);

                        player.sendMessage(user.translateC("COMMAND_MBA_CREATED").replace("%name%", _COLOR(temp.getName())));
                        Bukkit.getScheduler().runTask(CanelonesCore.INSTANCE, () -> _SOUND_PAGE(player));
                    });
                }
                case "remove" -> {
                    if (args.length < 3) {
                        sendHelp(sender, 2);
                        return true;
                    }

                    int id;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        user.sendLocale("COMMAND_MBA_REMOVE_ID_ERROR");
                        return true;
                    }

                    DBMysteryBoxObj obj = DBBufferMB.BUFFER.getById(id);
                    if (obj == null) {
                        user.sendLocale("COMMAND_MBA_REMOVE_ID_ERROR");
                        return true;
                    }

                    String name = obj.getName();

                    user.sendLocale("COMMAND_MBA_REMOVING");
                    DBApi.enqueue(() -> {
                        Connection conn = DBApi.connect();

                        int ex = DBApi.API.mystery_boxes.remove(conn, id);
                        if (ex != 1) {
                            user.sendLocale("COMMAND_MBA_REMOVE_ERROR");
                            return;
                        }

                        DBApi.API.users_mb.deleteAll(conn, id);

                        DBApi.disconnect(conn);

                        DBBufferMB.BUFFER.updateMysteryBoxes();
                        for(final User userF : UserManager.users.values())
                            userF.fetchData();

                        player.sendMessage(user.translateC("COMMAND_MBA_REMOVED").replace("%name%", _COLOR(name)));
                        Bukkit.getScheduler().runTask(CanelonesCore.INSTANCE, () -> _SOUND_PAGE(player));
                    });
                }
                case "name" -> {
                    if(args.length < 4) {
                        sendHelp(player, 2);
                        return true;
                    }

                    int id;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        user.sendLocale("COMMAND_MBA_NAME_ID_ERROR");
                        return true;
                    }

                    final DBMysteryBoxObj obj = DBBufferMB.BUFFER.getById(id);
                    if(obj == null) {
                        user.sendLocale("COMMAND_MBA_NAME_ID_ERROR");
                        return true;
                    }

                    user.sendLocale("COMMAND_MBA_RENAMING");

                    DBApi.enqueue(() -> {
                        StringBuilder builder = new StringBuilder();
                        for(int i = 3; i < args.length; i++)
                            builder.append(args[i] + " ");
                        obj.setName(builder.toString().trim());

                        Connection conn = DBApi.connect();

                        int ex = DBApi.API.mystery_boxes.modify(conn, obj);
                        if(ex != 1) {
                            user.sendLocale("COMMAND_MBA_RENAME_ERROR");
                            return;
                        }

                        DBApi.disconnect(conn);

                        player.sendMessage(user.translateC("COMMAND_MBA_RENAMED").replace("%name%", _COLOR(obj.getName())));
                    });
                }
                case "item" -> {
                    if(args.length < 4) {
                        sendHelp(player, 2);
                        return true;
                    }

                    int id;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        user.sendLocale("COMMAND_MBA_ITEM_ID_ERROR");
                        return true;
                    }

                    final DBMysteryBoxObj obj = DBBufferMB.BUFFER.getById(id);
                    if(obj == null) {
                        user.sendLocale("COMMAND_MBA_ITEM_ID_ERROR");
                        return true;
                    }

                    user.sendLocale("COMMAND_MBA_ITEM_CHANGING");

                    DBApi.enqueue(() -> {
                        StringBuilder builder = new StringBuilder();
                        for(int i = 3; i < args.length; i++)
                            builder.append(args[i]);
                        obj.setName(builder.toString().trim());

                        Connection conn = DBApi.connect();

                        int ex = DBApi.API.mystery_boxes.modify(conn, obj);
                        if(ex != 1) {
                            user.sendLocale("COMMAND_MBA_ITEM_ERROR");
                            return;
                        }

                        DBApi.disconnect(conn);

                        player.sendMessage(user.translateC("COMMAND_MBA_ITEM_CHANGED").replace("%name%", _COLOR(obj.getName())));
                    });
                }
                case "slot" -> {
                    if(args.length < 4) {
                        sendHelp(player, 2);
                        return true;
                    }

                    int id;
                    try {
                        id = Integer.parseInt(args[3]);
                    } catch (Exception ex) {
                        user.sendLocale("COMMAND_MBA_SLOT_ID_ERROR");
                        return true;
                    }

                    final DBMysteryBoxObj obj = DBBufferMB.BUFFER.getById(id);
                    if(args[2].equalsIgnoreCase("list")) {
                        if(obj == null) {
                            user.sendLocale("COMMAND_MBA_SLOT_ID_ERROR");
                            return true;
                        }

                        // When handling with big operations, put it in separate thread lol.
                        DBApi.enqueue(() -> {
                            player.sendMessage(_COLOR("&6" + obj.getName() + "&6's slots:"));
                            for(int i = 0; i < obj.getSlots().length; i++) {
                                String type = obj.getSlots()[i].getType();

                                if(type.equalsIgnoreCase("item")) {
                                    MBAItem item = (MBAItem)obj.getSlots()[i];

                                    // We need no buffer for items tbh, this is the unique method that needs to obtain all the ItemStacks.
                                    ItemStack stack = BukkitSerialization.deserializeItems(item.getItem())[0];
                                    player.sendMessage(_COLOR(" &7" + i + ". &8type: &f" + type + "&8, mat: &f" + stack.getType().name() + (stack.getItemMeta().hasDisplayName()?"&8, name: &f" + stack.getItemMeta().getDisplayName():"")));
                                    continue;
                                }

                                if(type.equalsIgnoreCase("command")) {
                                    MBACommand cmd = (MBACommand)obj.getSlots()[i];
                                    player.sendMessage(_COLOR(" &7" + i + ". &8type: &f" + type + "&8, cmd: &f" + cmd.getCommand()));
                                    continue;
                                }

                                // Unknown type(very rare & uncommon but a bit can flip making this happen[0.000001% probability])
                                //     - Another cause could be the deletion of old type. (don't even think I will add more)
                                player.sendMessage(_COLOR(" &7" + i + ". &8type: &f" + type));
                            }
                        });
                        return true;
                    }
                    if(args[2].equalsIgnoreCase("add")) {
                        if(args.length < 5) {
                            sendHelp(player, 2);
                            return true;
                        }

                        String type = args[4];
                        if(type.equalsIgnoreCase("cmd")) {
                            if(args.length < 6) {
                                sendHelp(player, 2);
                                return true;
                            }

                            StringBuilder cmdBuilder = new StringBuilder();
                            for(int i = 5; i < args.length; i++)
                                cmdBuilder.append(args[i] + " ");
                            String cmd = cmdBuilder.toString().trim();

                            ItemStack stack = player.getInventory().getItemInMainHand();

                            user.sendLocale("COMMAND_MBA_ITEM_ADD_ADDING");
                            DBApi.enqueue(() -> {
                                MBACommand mbaCommand = new MBACommand(BukkitSerialization.serializeItems(stack), cmd);
                                obj.addSlot(mbaCommand);

                                Connection conn = DBApi.connect();

                                int ex = DBApi.API.mystery_boxes.modify(conn, obj);
                                if(ex != 1) {
                                    user.sendLocale("COMMAND_MBA_ITEM_ADD_ERROR");
                                    return;
                                }

                                DBApi.disconnect(conn);

                                user.sendLocale("COMMAND_MBA_ITEM_ADD_ADDED");
                            });
                            return true;
                        }

                        if(type.equalsIgnoreCase("item")) {
                            ItemStack stack = player.getInventory().getItemInMainHand();
                            if(stack.getType() == Material.AIR) {
                                user.sendLocale("COMMAND_MBA_ITEM_ADD_AIR");
                                return true;
                            }

                            user.sendLocale("COMMAND_MBA_ITEM_ADD_ADDING");
                            DBApi.enqueue(() -> {
                                byte[] b = BukkitSerialization.serializeItems(stack);

                                MBAItem mbaItem = new MBAItem(b, b);
                                obj.addSlot(mbaItem);

                                Connection conn = DBApi.connect();

                                int ex = DBApi.API.mystery_boxes.modify(conn, obj);
                                if(ex != 1) {
                                    user.sendLocale("COMMAND_MBA_ITEM_ADD_ERROR");
                                    return;
                                }

                                DBApi.disconnect(conn);

                                user.sendLocale("COMMAND_MBA_ITEM_ADD_ADDED");
                            });
                            return true;
                        }

                        sendHelp(player, 2);
                        return true;
                    }
                    if(args[2].equalsIgnoreCase("rem")) {
                        int slot;
                        try {
                            slot = Integer.parseInt(args[4]);
                        } catch (Exception ex) {
                            user.sendLocale("COMMAND_MBA_SLOT_WRONG");
                            return true;
                        }

                        if(slot >= obj.getSlots().length || slot < 0) {
                            user.sendLocale("COMMAND_MBA_SLOT_WRONG");
                            return true;
                        }

                        user.sendLocale("COMMAND_MBA_ITEM_REMOVE_REMOVING");
                        DBApi.enqueue(() -> {
                            Connection conn = DBApi.connect();
                            obj.remSlot(slot);

                            int exCode = DBApi.API.mystery_boxes.modify(conn, obj);
                            if(exCode != 1) {
                                user.sendLocale("COMMAND_MBA_ITEM_REMOVE_ERROR");
                                return;
                            }

                            DBApi.disconnect(conn);
                            user.sendLocale("COMMAND_MBA_ITEM_REMOVE_REMOVED");
                        });

                        return true;
                    }
                    if(args[2].equalsIgnoreCase("force")) {
                        int slot;
                        try {
                            slot = Integer.parseInt(args[4]);
                        } catch (Exception ex) {
                            user.sendLocale("COMMAND_MBA_SLOT_WRONG");
                            return true;
                        }

                        if(slot >= obj.getSlots().length || slot < 0) {
                            user.sendLocale("COMMAND_MBA_SLOT_WRONG");
                            return true;
                        }

                        MysteryBoxAction action = obj.getSlots()[slot];
                        if(action.getType().equalsIgnoreCase("command")) {
                            String c = ((MBACommand)action).getCommand();

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), _PLACEHOLDER(player, c));
                            player.sendMessage(_COLOR("&6COMMAND: &e'" + c + "'"));
                            return true;
                        }

                        if(action.getType().equalsIgnoreCase("item")) {
                            ItemStack i = BukkitSerialization.deserializeItems(((MBAItem)action).getItem())[0];

                            player.getInventory().addItem(i);
                            player.sendMessage(_COLOR("&6ITEM: &e'" + i.getType().name() + "'"));
                            return true;
                        }

                        player.sendMessage(_COLOR("&cUNKNOWN"));
                        return true;
                    }
                }
                default -> sendHelp(player, 2);
            }

            return true;
        }

        if(args[0].equalsIgnoreCase("location")) {
            if(!(sender instanceof Player player)) {
                sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_ONLY_PLAYER"));
                return true;
            }

            User user = UserManager.get(player);
            if(user == null) {
                player.sendMessage(_SENDER_TRANSLATE(player, "COMMAND_USER_NOT_FOUND"));
                return true;
            }

            if(args.length < 2) {
                sendHelp(sender, 3);
                return true;
            }

            switch(args[1].toLowerCase()) {
                case "list" -> {
                    final DBMysteryBoxLocationObj[] locations = DBBufferMB.BUFFER.getLocations();

                    player.sendMessage(_COLOR("&d&lMystery&dBox &6locations:"));
                    for (final DBMysteryBoxLocationObj obj : locations)
                        player.sendMessage(_COLOR(" &8- " +
                                "id: &f" + obj.getId() +
                                "&8,x: &f" + obj.getLocation().getBlockX() +
                                "&8,y: &f" + obj.getLocation().getBlockY() +
                                "&8,z: &f" + obj.getLocation().getBlockZ() +
                                "&8,w: &f" + obj.getLocation().getWorld().getName() +
                                "&8,u: &f" + (obj.isInUse() ? "y" : "n")));
                }
                case "create" -> {
                    Block block = player.getTargetBlock(5);
                    if(block == null) {
                        user.sendLocale("COMMAND_MBA_LOCATION_CREATE_LOOK");
                        return true;
                    }

                    Location location = block.getLocation();
                    if(DBBufferMB.BUFFER.getAt(location) != null) {
                        user.sendLocale("COMMAND_MBA_LOCATION_CREATE_EXISTS");
                        return true;
                    }

                    user.sendLocale("COMMAND_MBA_LOCATION_CREATE_ADDING");
                    DBApi.enqueue(() -> {
                        Connection conn = DBApi.connect();

                        int exCode = DBApi.API.mystery_boxes_location.register(conn, location);
                        if(exCode != 1) {
                            user.sendLocale("COMMAND_MBA_LOCATION_CREATE_ERROR");
                            return;
                        }

                        DBApi.disconnect(conn);

                        DBBufferMB.BUFFER.updateMysteryBoxes();
                        user.sendLocale("COMMAND_MBA_LOCATION_CREATE_ADDED");
                    });
                }
                case "delete" -> {
                    if(args.length < 3) {
                        sendHelp(sender, 3);
                        return true;
                    }

                    int id;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        user.sendLocale("COMMAND_MBA_LOCATION_REMOVE_INVALID");
                        return true;
                    }

                    DBMysteryBoxLocationObj obj = DBBufferMB.BUFFER.getLocationById(id);
                    if(obj == null) {
                        user.sendLocale("COMMAND_MBA_LOCATION_REMOVE_INVALID");
                        return true;
                    }

                    user.sendLocale("COMMAND_MBA_LOCATION_REMOVE_REMOVING");
                    DBApi.enqueue(() -> {
                        Connection conn = DBApi.connect();

                        int exCode = DBApi.API.mystery_boxes_location.remove(conn, id);
                        if(exCode != 1) {
                            user.sendLocale("COMMAND_MBA_LOCATION_CREATE_ERROR");
                            return;
                        }

                        DBApi.disconnect(conn);

                        DBBufferMB.BUFFER.updateMysteryBoxes();
                        user.sendLocale("COMMAND_MBA_LOCATION_REMOVE_REMOVED");
                    });
                }

                default -> sendHelp(sender, 3);
            }
            return true;
        }

        if(args[0].equalsIgnoreCase("user")) {
            if(args.length < 3) {
                sendHelp(sender, 4);
                return true;
            }

            switch(args[1].toLowerCase()) {
                case "list" -> {
                    Player target = Bukkit.getPlayer(args[2]);
                    if(target == null) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_NOT_FOUND"));
                        return true;
                    }

                    final User targetUser = UserManager.get(target);
                    if(targetUser == null) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_NOT_FOUND"));
                        return true;
                    }

                    final DBUserMBObj[] boxes = targetUser.getBoxes();

                    sender.sendMessage(_COLOR("&e" + target.getName() + "'s &6boxes:"));
                    for(final DBUserMBObj obj : boxes)
                        sender.sendMessage(_COLOR(" &8- id: &f" + obj.mid + "&8, amount: &f" + obj.amount));
                }
                case "give" -> {
                    Player target = Bukkit.getPlayer(args[2]);
                    if(target == null) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_NOT_FOUND"));
                        return true;
                    }

                    final User targetUser = UserManager.get(target);
                    if(targetUser == null) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_NOT_FOUND"));
                        return true;
                    }

                    int id;
                    try {
                        id = Integer.parseInt(args[3]);
                    } catch (Exception ex) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_LOCATION_REMOVE_INVALID"));
                        return true;
                    }

                    DBMysteryBoxObj obj = DBBufferMB.BUFFER.getById(id);
                    if(obj == null) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_LOCATION_REMOVE_INVALID"));
                        return true;
                    }

                    int amount = 1;
                    if(args.length > 4) {
                        try {
                            amount = Integer.parseInt(args[4]);
                            if(amount < 1)
                                throw new Exception("amount below 1");
                        } catch (Exception ex) {
                            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_INVALID_AMOUNT"));
                            return true;
                        }
                    }

                    final int finalAmount = amount;

                    sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_GIVE_ADDING"));
                    DBApi.enqueue(() -> {
                        Connection conn = DBApi.connect();

                        int exCode = DBApi.API.users_mb.add(conn, targetUser.getUserId(), id, finalAmount);
                        if(exCode != 1) {
                            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_LOCATION_CREATE_ERROR"));
                            return;
                        }

                        DBApi.disconnect(conn);
                        targetUser.fetchData();

                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_GIVE_SENT"));
                        target.sendMessage(targetUser.translateC("COMMAND_MBA_USER_GIVE_RECEIVED").replace("%amount%", String.valueOf(finalAmount)).replace("%box%", _COLOR(obj.getName())));

                        Bukkit.getScheduler().runTask(CanelonesCore.INSTANCE, () -> _SOUND_EXP(target));
                    });
                }
                case "take" -> {
                    Player target = Bukkit.getPlayer(args[2]);
                    if(target == null) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_NOT_FOUND"));
                        return true;
                    }

                    final User targetUser = UserManager.get(target);
                    if(targetUser == null) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_NOT_FOUND"));
                        return true;
                    }

                    int id;
                    try {
                        id = Integer.parseInt(args[3]);
                    } catch (Exception ex) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_LOCATION_REMOVE_INVALID"));
                        return true;
                    }

                    DBMysteryBoxObj obj = DBBufferMB.BUFFER.getById(id);
                    if(obj == null) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_LOCATION_REMOVE_INVALID"));
                        return true;
                    }

                    int amount = 1;
                    if(args.length > 4) {
                        try {
                            amount = Integer.parseInt(args[4]);
                            if(amount < 1)
                                throw new Exception("amount below 1");
                        } catch (Exception ex) {
                            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_INVALID_AMOUNT"));
                            return true;
                        }
                    }

                    boolean has = false;
                    for(DBUserMBObj uobj : targetUser.getBoxes())
                        if(uobj.mid == id)
                            if(amount <= uobj.amount)
                                has = true;

                    if(!has) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_REMOVE_HAVE"));
                        return true;
                    }

                    final int aF = amount;

                    sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_REMOVE_REMOVING"));
                    DBApi.enqueue(() -> {
                        Connection conn = DBApi.connect();

                        int exCode = DBApi.API.users_mb.remove(conn, targetUser.getUserId(), id, aF);
                        if(exCode != 1) {
                            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_LOCATION_CREATE_ERROR"));
                            return;
                        }

                        DBApi.disconnect(conn);
                        targetUser.fetchData();

                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_REMOVE_REMOVED"));
                    });
                }
                case "giveall" -> {
                    int id;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_LOCATION_REMOVE_INVALID"));
                        return true;
                    }

                    DBMysteryBoxObj obj = DBBufferMB.BUFFER.getById(id);
                    if(obj == null) {
                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_LOCATION_REMOVE_INVALID"));
                        return true;
                    }

                    int amount = 1;
                    if(args.length > 3) {
                        try {
                            amount = Integer.parseInt(args[3]);
                            if(amount < 1)
                                throw new Exception("amount below 1");
                        } catch (Exception ex) {
                            sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_INVALID_AMOUNT"));
                            return true;
                        }
                    }

                    final int finalAmount = amount;

                    sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_GIVEALL_SENDING"));
                    DBApi.enqueue(() -> {
                        Connection conn = DBApi.connect();

                        Collection<User> users = new ArrayList<>(UserManager.users.values());
                        for(final User user : users) {
                            if(user == null || !user.player.isValid())
                                continue;
                            DBApi.API.users_mb.add(conn, user.getUserId(), obj.getId(), finalAmount);
                            user.fetchData();

                            user.player.sendMessage(user.translateC("COMMAND_MBA_USER_GIVE_RECEIVED").replace("%amount%", String.valueOf(finalAmount)).replace("%box%", _COLOR(obj.getName())));
                            Bukkit.getScheduler().runTask(CanelonesCore.INSTANCE, () -> _SOUND_EXP(user.player));
                        }

                        DBApi.disconnect(conn);

                        sender.sendMessage(_SENDER_TRANSLATE(sender, "COMMAND_MBA_USER_GIVEALL_SENT"));
                    });
                }

                default -> sendHelp(sender, 4);
            }
            return true;
        }

        sendHelp(sender, 1);
        return true;
    }

    @NotNull
    public static DBMysteryBoxObj genTempMB(Player player, String name) {
        MysteryBoxAction[] slots = new MysteryBoxAction[0];

        ItemStack material = new ItemStack(Material.ENDER_CHEST);
        if(player.getInventory().getItemInMainHand().getType() != Material.AIR)
            material = player.getInventory().getItemInMainHand();

        DBMysteryBoxObj temp = new DBMysteryBoxObj(-1, name, slots, material); // Setting id to -1 bc temp, db will set id.
        return temp;
    }
}