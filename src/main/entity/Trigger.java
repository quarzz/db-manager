package main.entity;

/**
 * Created by User on 29.10.2016.
 */
public class Trigger {
    private String name;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
