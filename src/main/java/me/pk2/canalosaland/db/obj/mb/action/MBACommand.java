package me.pk2.canalosaland.db.obj.mb.action;

import java.io.Serializable;

public class MBACommand extends MysteryBoxAction implements Serializable {
    private String command;
    public MBACommand(byte[] material, String command) {
        super("command", material);
        this.command = command;
    }

    public String getCommand() { return command; }
    public void setCommand(String command) { this.command = command; }
}