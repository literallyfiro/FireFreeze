package me.onlyfire.firefreeze.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFreezeAddEvent extends Event {
    private static HandlerList handlers = new HandlerList();

    private Player staff;
    private Player frozen;

    public PlayerFreezeAddEvent(Player staff, Player frozen){
        this.staff = staff;
        this.frozen = frozen;
    }

    public Player getStaff() {
        return staff;
    }

    public Player getFrozen() {
        return frozen;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
