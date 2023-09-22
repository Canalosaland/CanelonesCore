package me.pk2.canalosaland.interfaces.jobs;

import me.pk2.canalosaland.interfaces.GInterfaceJob;
import me.pk2.canalosaland.user.User;

public class GInterfaceJobEnchanter extends GInterfaceJob {
    public GInterfaceJobEnchanter(User user) {
        super(user, "Enchanter");
    }
}