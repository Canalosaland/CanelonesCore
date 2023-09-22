package me.pk2.canalosaland.interfaces;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import me.pk2.canalosaland.user.User;
import org.bukkit.Bukkit;

import static me.pk2.canalosaland.util.Wrapper.*;

public class GInterfaceJob extends GInterface {
    private String job;
    public GInterfaceJob(User user, String job) {
        super(user, "&2&l"+job, 3);

        this.job = job;
    }

    @Override
    public void open() {
        super.open();
        _SOUND_PAGE(owner.player);

        final JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(owner.player);
        final Job job = Jobs.getJob(this.job);
        if(jobsPlayer.isInJob(job))
            setItem(13, job.getGuiItem().getType(), 0, "&c"+this.job, "", owner.translateC("INTERFACE_JOB_LEAVE"));
        else setItem(13, job.getGuiItem().getType(), 0, "&a"+this.job, "", owner.translateC("INTERFACE_JOB_JOIN"));
    }

    @Override
    public void init() {

    }

    @Override
    public void click(int slot) {
        if(slot != 13)
            return;

        final JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(owner.player);
        final Job job = Jobs.getJob(this.job);
        if(jobsPlayer.isInJob(job))
            Bukkit.dispatchCommand(owner.player, "jobs leave " + this.job);
        else Bukkit.dispatchCommand(owner.player, "jobs join " + this.job);

        owner.player.getInventory().close();
        _SOUND_PAGE(owner.player);
    }
}