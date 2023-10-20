package me.pk2.canalosaland.jobs;

import me.pk2.canalosaland.CanelonesCore;
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
}