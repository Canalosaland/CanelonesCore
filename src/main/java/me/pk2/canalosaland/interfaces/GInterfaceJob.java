package me.pk2.canalosaland.interfaces;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.jobs.Job;
import me.pk2.canalosaland.user.User;
import org.bukkit.Bukkit;

import static me.pk2.canalosaland.util.Wrapper.*;

public class GInterfaceJob extends GInterface {
    private String job;
    private Job guiJob;
    public GInterfaceJob(User user, String job) {
        super(user, "&2&l"+job, 3);

        this.job = job;
        this.guiJob = CanelonesCore.INSTANCE.jobSystem.job(job);
    }

    @Override
    public void open() {
        super.open();
        _SOUND_PAGE(owner.player);
		
		if(guiJob == null)
			return;

        Job job = owner.getJob();
        if(job == null)
            setItem(13, guiJob.getMaterial(), 0, "&a"+this.job, "", owner.translateC("INTERFACE_JOB_JOIN"));
        else if(!job.getName().equals(this.job))
            setItem(13, guiJob.getMaterial(), 0, "&a"+this.job, "", owner.translateC("INTERFACE_JOB_ANOTHER"));
        else setItem(13, guiJob.getMaterial(), 0, "&a"+this.job, "", owner.translateC("INTERFACE_JOB_LEAVE"));
    }

    @Override
    public void init() {

    }

    @Override
    public void click(int slot) {
        if(slot != 13 || guiJob == null)
            return;

        Job job = owner.getJob();
        if(job == null) { // Join job.
            owner.newJob(guiJob);
            owner.sendLocaleArgs("JOBS_JOINED", this.job);
        } else if(job.getName().equals(this.job)) { // Leave job.
            owner.newJob(null);
            owner.sendLocaleArgs("JOBS_LEFT", this.job);
        } else owner.sendLocaleArgs("JOBS_MUST_LEAVE", job.getName());

        owner.player.getInventory().close();
        _SOUND_PAGE(owner.player);
    }
}