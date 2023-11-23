package me.pk2.canalosaland.jobs;

import me.pk2.canalosaland.CanelonesCore;
import me.pk2.canalosaland.jobs.def.JobFisherman;
import me.pk2.canalosaland.jobs.def.JobHunter;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class JobSystem {
    private final CanelonesCore INSTANCE;
    private final HashMap<String, Job> jobList;
    public JobSystem(CanelonesCore plugin) {
        INSTANCE = plugin;

        this.jobList = new HashMap<>();
    }

    public void registerJob(Job job) {
        Bukkit.getPluginManager().registerEvents(job, INSTANCE);
        jobList.put(job.getName(), job);
    }
    public void unregisterJob(String job) { jobList.remove(job); }
    public Job job(String job) { return jobList.get(job); }

    /* REFERENCE */
    public static JobSystem build(CanelonesCore plugin) {
        JobSystem system = new JobSystem(plugin);

        // Register jobs
        system.registerJob(new JobHunter());
        system.registerJob(new JobFisherman());

        return system;
    }
}