package moe.katelyn.citadel;

public class Upgrade {
    private final int cooldown;

    public Upgrade(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldown() { return cooldown; }

    public String getCooldownSeconds() {
        return String.format("%.1f", cooldown / 20.0);
    }
}
