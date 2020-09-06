package me.onlyfire.firefreeze.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFreezeQuitEvent extends Event {
    private static HandlerList handlers = new HandlerList();

    private Player frozen;

    public PlayerFreezeQuitEvent(Player frozen) {
        this.frozen = frozen;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getFrozen() {
        return frozen;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
