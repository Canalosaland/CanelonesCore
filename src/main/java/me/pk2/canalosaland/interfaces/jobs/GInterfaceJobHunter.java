package me.pk2.canalosaland.interfaces.jobs;

import me.pk2.canalosaland.interfaces.GInterfaceJob;
import me.pk2.canalosaland.user.User;

public class GInterfaceJobHunter extends GInterfaceJob {
    public GInterfaceJobHunter(User user) {
        super(user, "Hunter");
    }
}