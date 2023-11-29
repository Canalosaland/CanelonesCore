package me.pk2.canalosaland.db.obj;

public class DBBanObj {
    private final int id;
    private final short type;
    private final String player;
    private final long time;
    private final String reason;
    private boolean pardon;
    public DBBanObj(int id, short type, String player, long time, String reason, boolean pardon) {
        this.id = id;
        this.type = type;
        this.player = player;
        this.time = time;
        this.reason = reason;
        this.pardon = pardon;
    }

    public int getId() { return id; }
    public short getType() { return type; }
    public String getPlayer() { return player; }

    public long getTime() { return time; }
    public String getTimeExp() {
        if(time == 0)
            return "FOREVER";
        long currentTime = System.currentTimeMillis();
        long diff = time - currentTime;
        if(diff <= 0)
            return "EXPIRED";

        long days, hours, minutes, seconds = minutes = hours = days = 0;
        seconds = diff / 1000;

        if(seconds > 59) {
            minutes += seconds / 60;
            seconds -= minutes * 60;

            if(minutes > 59) {
                hours += minutes / 60;
                minutes -= hours * 60;

                if(hours > 24) {
                    days += hours / 24;
                    hours -= days * 24;
                }
            }
        }

        String ret = "";
        if(days > 0)
            ret += days + "d ";
        if(hours > 0)
            ret += hours + "h ";
        if(minutes > 0)
            ret += minutes + "m ";
        if(seconds > 0)
            ret += seconds + "s";

        return ret;
    }
    public boolean expired() { return pardon || (time != 0 && System.currentTimeMillis() > time); }

    public String getReason() { return reason; }

    public boolean pardon() { return pardon; }
    public void pardon(boolean pardon) { this.pardon = pardon; }
}