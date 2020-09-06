package me.onlyfire.firefreeze.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFreezeRemoveEvent extends Event {
    private static HandlerList handlers = new HandlerList();

    private Player staff;
    private Player frozen;

    public PlayerFreezeRemoveEvent(Player staff, Player frozen) {
        this.staff = staff;
        this.frozen = frozen;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getStaff() {
        return staff;
    }

    public Player getFrozen() {
        return frozen;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
