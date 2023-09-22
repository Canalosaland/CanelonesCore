package me.pk2.canalosaland.interfaces.jobs;

import me.pk2.canalosaland.interfaces.GInterfaceJob;
import me.pk2.canalosaland.user.User;

public class GInterfaceJobBrewer extends GInterfaceJob {
    public GInterfaceJobBrewer(User user) {
        super(user, "Brewer");
    }
}