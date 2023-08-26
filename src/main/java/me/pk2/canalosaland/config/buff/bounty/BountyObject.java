package me.pk2.canalosaland.config.buff.bounty;

public class BountyObject {
    private String target;
    private String player;
    private double amount;

    public BountyObject(String target, String player, double amount) {
        this.target = target;
        this.player = player;
        this.amount = amount;
    }

    public String getTarget() {
        return this.target;
    }

    public String getPlayer() {
        return this.player;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}