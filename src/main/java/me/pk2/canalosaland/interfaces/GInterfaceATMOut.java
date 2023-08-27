package me.pk2.canalosaland.interfaces;

import me.pk2.canalosaland.config.buff.ConfigAtmBuffer;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.user.User;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static me.pk2.canalosaland.util.Wrapper.*;

public class GInterfaceATMOut extends GInterface {
    public GInterfaceATMOut(User user) {
        super(user, "&eATM - Retirar", 6);
    }

    @Override
    public void init() {
        for(int i = 0; i < 54; i++)
            setItem(i, Material.WHITE_STAINED_GLASS_PANE, 0, "&r");
        setItem(45, Material.GOLD_INGOT, 0, "&6&lSaldo", "&e0.00$");
        setItem(53, Material.BARRIER, 0, "&c&lSalir", "&7Clic para salir.");
    }

    @Override
    public void click(int slot) {
        switch(slot) {
            case 53 -> {
                owner.player.closeInventory();
                owner.openI(GInterfaceATM.class);

                _SOUND_CLOSE(owner.player);
                return;
            }
            default -> { }
        }

        double balance = DependencyVault.getBalance(owner.player);
        double price = 0;

        Material slotMaterial = getInstance().getItem(slot) == null ? Material.AIR : getInstance().getItem(slot).getType();
        if(slotMaterial == Material.AIR) {
            _SOUND_CLICK(owner.player);
            return;
        }

        try {
            price = ConfigAtmBuffer.buffer.prices.class.getDeclaredField(slotMaterial.name().toLowerCase()).getDouble(null);
        } catch(Exception e) {
            //e.printStackTrace();
        }

        if(price == 0) {
            owner.player.sendMessage(_COLOR("&cNo se pudo encontrar el precio de este item."));

            _SOUND_ERROR(owner.player);
            return;
        }

        Inventory inv = owner.player.getInventory();
        double money = DependencyVault.getBalance(owner.player);

        if(money < price) {
            owner.player.sendMessage(_COLOR("&cNo tienes suficiente dinero para sacar este item."));

            _SOUND_ERROR(owner.player);
            return;
        }

        inv.addItem(new ItemStack(slotMaterial, 1));
        DependencyVault.economy.withdrawPlayer(owner.player, price);

        owner.player.sendMessage(_COLOR("&aHas retirado &e" + slotMaterial.name().toLowerCase() + " &apor &e" + price + "$&a."));
        updateGUI();
        _SOUND_NOTIFICATION(owner.player);
    }

    public void updateGUI() {
        double balance = DependencyVault.getBalance(owner.player);

        setItem(45, Material.GOLD_INGOT, 0, "&6&lSaldo", String.format("&e%.2f$", balance));

        /*setItem(10, Material.MUSIC_DISC_PIGSTEP, 0, "&e&lDisco Pigstep", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.music_disc_pigstep), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.music_disc_pigstep + balance), "", "&7Clic para depositar.");
        setItem(11, Material.PIGLIN_BANNER_PATTERN, 0, "&e&lBanner Piglin", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.piglin_banner_pattern), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.piglin_banner_pattern + balance), "", "&7Clic para depositar.");
        setItem(12, Material.GILDED_BLACKSTONE, 0, "&e&lGilded Blackstone", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.gilded_blackstone), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.gilded_blackstone + balance), "", "&7Clic para depositar.");
        setItem(13, Material.DRAGON_HEAD, 0, "&e&lCabeza de dragon", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.dragon_head), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.dragon_head + balance), "", "&7Clic para depositar.");
        setItem(14, Material.NETHERITE_INGOT, 0, "&e&lLingote de netherite", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.netherite_ingot), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.netherite_ingot + balance), "", "&7Clic para depositar.");
        setItem(15, Material.MOJANG_BANNER_PATTERN, 0, "&e&lBanner Mojang", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.mojang_banner_pattern), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.mojang_banner_pattern + balance), "", "&7Clic para depositar.");
        setItem(16, Material.ENCHANTED_GOLDEN_APPLE, 0, "&e&lNotch", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.enchanted_golden_apple), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.enchanted_golden_apple + balance), "", "&7Clic para depositar.");
        */
        setItem(10, Material.DIAMOND, 0, "&e&lDiamond", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.diamond), String.format("&e&lBalance despues: &e%.2f$", -ConfigAtmBuffer.buffer.prices.diamond + balance), "", "&7Clic para retirar.");
        //setItem(11, Material.DIAMOND_ORE, 0, "&e&lDiamond Ore", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.diamond_ore), String.format("&e&lBalance despues: &e%.2f$", -ConfigAtmBuffer.buffer.prices.diamond_ore + balance), "", "&7Clic para retirar.");
        //setItem(21, Material.HEART_OF_THE_SEA, 0, "&e&lCorazon del mar", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.heart_of_the_sea), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.heart_of_the_sea + balance), "", "&7Clic para depositar.");
        //setItem(12, Material.EMERALD_ORE, 0, "&e&lEmerald Ore", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.emerald_ore), String.format("&e&lBalance despues: &e%.2f$", -ConfigAtmBuffer.buffer.prices.emerald_ore + balance), "", "&7Clic para retirar.");
        /*setItem(23, Material.CONDUIT, 0, "&e&lConduit", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.conduit), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.conduit + balance), "", "&7Clic para depositar.");
        setItem(24, Material.ANCIENT_DEBRIS, 0, "&e&lAncient Debris", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.ancient_debris), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.ancient_debris + balance), "", "&7Clic para depositar.");
        setItem(25, Material.NETHERITE_SCRAP, 0, "&e&lNetherite Scrap", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.netherite_scrap), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.netherite_scrap + balance), "", "&7Clic para depositar.");
        */
        /*
        setItem(28, Material.GOLDEN_APPLE, 0, "&e&lManzana dorada", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.golden_apple), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.golden_apple + balance), "", "&7Clic para depositar.");
        setItem(29, Material.NETHER_GOLD_ORE, 0, "&e&lNether Gold Ore", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.nether_gold_ore), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.nether_gold_ore + balance), "", "&7Clic para depositar.");
        setItem(30, Material.IRON_HORSE_ARMOR, 0, "&e&lArmadura de caballo de hierro", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.iron_horse_ore), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.iron_horse_ore + balance), "", "&7Clic para depositar.");
        */
        setItem(11, Material.LAPIS_ORE, 0, "&e&lLapis Ore", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.lapis_ore), String.format("&e&lBalance despues: &e%.2f$", -ConfigAtmBuffer.buffer.prices.lapis_ore + balance), "", "&7Clic para retirar.");
        //setItem(32, Material.DIAMOND_HORSE_ARMOR, 0, "&e&lArmadura de caballo de diamante", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.diamond_horse_armor), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.diamond_horse_armor + balance), "", "&7Clic para depositar.");
        setItem(12, Material.EMERALD, 0, "&e&lEsmeralda", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.emerald), String.format("&e&lBalance despues: &e%.2f$", -ConfigAtmBuffer.buffer.prices.emerald + balance), "", "&7Clic para retirar.");
        //setItem(34, Material.SHULKER_SHELL, 0, "&e&lShulker shell", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.shulker_shell), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.shulker_shell + balance), "", "&7Clic para depositar.");

        //setItem(37, Material.RABBIT_FOOT, 0, "&e&lPie de conejo", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.rabbit_foot), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.rabbit_foot + balance), "", "&7Clic para depositar.");
        setItem(13, Material.COAL_ORE, 0, "&e&lCoal ore", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.coal_ore), String.format("&e&lBalance despues: &e%.2f$", -ConfigAtmBuffer.buffer.prices.coal_ore + balance), "", "&7Clic para retirar.");
        setItem(14, Material.IRON_ORE, 0, "&e&lIron ore", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.iron_ore), String.format("&e&lBalance despues: &e%.2f$", -ConfigAtmBuffer.buffer.prices.iron_ore + balance), "", "&7Clic para retirar.");
        setItem(15, Material.GOLD_ORE, 0, "&e&lGold ore", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.gold_ore), String.format("&e&lBalance despues: &e%.2f$", -ConfigAtmBuffer.buffer.prices.gold_ore + balance), "", "&7Clic para retirar.");
        setItem(16, Material.REDSTONE_ORE, 0, "&e&lRedstone ore", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.redstone_ore), String.format("&e&lBalance despues: &e%.2f$", -ConfigAtmBuffer.buffer.prices.redstone_ore + balance), "", "&7Clic para retirar.");
        //setItem(42, Material.GOLDEN_HORSE_ARMOR, 0, "&e&lArmadura de caballo de oro", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.golden_horse_armor), String.format("&e&lBalance despues: &e%.2f$", ConfigAtmBuffer.buffer.prices.golden_horse_armor + balance), "", "&7Clic para depositar.");
        setItem(22, Material.NETHER_QUARTZ_ORE, 0, "&e&lQuartz ore", String.format("&e&lValor: &e%.2f$", ConfigAtmBuffer.buffer.prices.nether_quartz_ore), String.format("&e&lBalance despues: &e%.2f$", -ConfigAtmBuffer.buffer.prices.nether_quartz_ore + balance), "", "&7Clic para retirar.");
    }

    @Override
    public void open() {
        super.open();
        _SOUND_OPEN(owner.player);

        updateGUI();
    }
}