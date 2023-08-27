package me.pk2.canalosaland.interfaces;

import static me.pk2.canalosaland.util.Wrapper.*;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.dependencies.DependencyVault;
import me.pk2.canalosaland.user.User;
import me.pk2.canalosaland.user.UserManager;
import me.pk2.canalosaland.util.SignMenuFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GInterfacePhoneBizum extends GInterface {
    private int page = 1, maxPage;
    private List<User> users;

    public GInterfacePhoneBizum(User user) {
        super(user, "&9Phone - Bizum", 6);

        users = new ArrayList<>();
    }

    @Override
    public void init() {
        // 0 - 44 > Player selection
        setItem(45, Material.GOLD_INGOT, 0, "&6&lSaldo", "&e0.00$");
        setItem(48, Material.SUNFLOWER, 0, "&aAnterior", "&7Clic para ir a la pagina anterior.");
        setItem(49, Material.PAPER, 0, "&ePagina", "&e1/1");
        setItem(50, Material.SUNFLOWER, 0, "&aSiguiente", "&7Clic para ir a la pagina siguiente.");
        setItem(53, Material.BARRIER, 0, "&c&lSalir", "&7Clic para salir.");
    }

    @Override
    public void click(int slot) {
        if(slot <= 44) {
            _SOUND_CLICK(owner.player);
            int index = (page - 1) * 45 + slot;
            if(index >= users.size())
                return;

            User user = users.get(index);
            if(user == null || user.player == null || !user.player.isValid())
                return;

            if(user.player.getName().contentEquals(owner.player.getName())) {
                owner.player.sendMessage(_COLOR("&7[&eBIZUM&7] &cNo puedes enviarte dinero a ti mismo."));
                _SOUND_CLICK(owner.player);
                return;
            }

            owner.player.closeInventory();
            Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                SignMenuFactory.Menu menu = CanelonesCore.INSTANCE.signMenuFactory
                        .newMenu(Arrays.asList("&e&lBizum a:", "&e" + user.player.getName(), "&aCantidad:", "0.00"))
                        .reopenIfFail(false)
                        .response((player, args) -> {
                            double amount = 0.00d;
                            try {
                                amount = Double.parseDouble(args[3]);
                            } catch (Exception ex) {
                                player.sendMessage(_COLOR("&7[&eBIZUM&7] &cCantidad invalida."));
                                return false;
                            }

                            if(amount <= 0) {
                                player.sendMessage(_COLOR("&7[&eBIZUM&7] &cCantidad invalida."));
                                return false;
                            }

                            if(!DependencyVault.has(player, amount)) {
                                player.sendMessage(_COLOR("&7[&eBIZUM&7] &cNo tienes suficiente dinero."));
                                return false;
                            }

                            DependencyVault.economy.withdrawPlayer(player, amount);
                            DependencyVault.economy.depositPlayer(user.player, amount);

                            player.sendMessage(_COLOR("&7[&eBIZUM&7] &aHas enviado &e" + amount + "$ &aa &e" + user.player.getName() + "&a."));
                            user.player.sendMessage(_COLOR("&7[&eBIZUM&7] &aHas recibido &e" + amount + "$ &ade &e" + player.getName() + "&a."));

                            _SOUND_NOTIFICATION(player);
                            _SOUND_NOTIFICATION(user.player);
                            return true;
                        });
                menu.open(owner.player);
            }, 2L);

            return;
        }

        switch (slot) {
            case 48 -> {
                page--;
                updatePage();

                _SOUND_PAGE(owner.player);
            }
            case 49 -> {
                updatePage();

                _SOUND_PAGE(owner.player);
            }
            case 50 -> {
                page++;
                updatePage();

                _SOUND_PAGE(owner.player);
            }
            case 53 -> {
                owner.player.closeInventory();
                Bukkit.getScheduler().runTaskLater(CanelonesCore.INSTANCE, () -> {
                    owner.openI(GInterfacePhone.class);
                }, 2L);

                _SOUND_CLOSE(owner.player);
            }
            default -> {
                _SOUND_CLICK(owner.player);
            }
        }
    }

    public void updatePage() {
        users.clear();
        users.addAll(UserManager.users.values());

        maxPage = (int) Math.ceil(users.size() / 45.0);
        if(page > maxPage)
            page = maxPage;
        if(page < 1)
            page = 1;

        setItem(45, Material.GOLD_INGOT, 0, "&6&lSaldo", String.format("&e%.2f$", DependencyVault.getBalance(owner.player)));
        setItem(49, Material.PAPER, 0, "&ePagina", "&e" + page + "/" + maxPage);

        int fromIdx = (page - 1) * 45;
        int toIdx = fromIdx + 45;
        if(toIdx > users.size())
            toIdx = users.size();

        for(int i = 0; i < 45; i++) {
            int index = fromIdx + i;
            if(index >= toIdx) {
                setItem(i, Material.RED_STAINED_GLASS_PANE, 0, "&c&lNo hay mas usuarios", "&7No hay mas usuarios para mostrar.");
                continue;
            }

            User user = users.get(index);
            if(user == null) {
                setItem(i, Material.BLACK_STAINED_GLASS_PANE, 0, "&c&lError", "&7No se ha podido cargar el usuario.");
                continue;
            }

            setHead(i, user.player, "&e" + user.player.getName(), "&6Saldo: &e" + String.format("%.2f$", DependencyVault.getBalance(user.player)));
        }
    }

    @Override
    public void open() {
        super.open();
        _SOUND_OPEN(owner.player);

        updatePage();
    }
}