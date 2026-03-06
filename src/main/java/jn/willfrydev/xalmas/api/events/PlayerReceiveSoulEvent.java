package jn.willfrydev.xalmas.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerReceiveSoulEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled = false;

    private final Player killer;
    private final Player victim;
    private String soulType;

    public PlayerReceiveSoulEvent(Player killer, Player victim, String soulType) {
        this.killer = killer;
        this.victim = victim;
        this.soulType = soulType;
    }

    public Player getKiller() { return killer; }
    public Player getVictim() { return victim; }

    // Permite que otro plugin cambie qué alma va a recibir
    public String getSoulType() { return soulType; }
    public void setSoulType(String soulType) { this.soulType = soulType; }

    @Override
    public boolean isCancelled() { return isCancelled; }

    @Override
    public void setCancelled(boolean cancel) { this.isCancelled = cancel; }

    @NotNull
    @Override
    public HandlerList getHandlers() { return HANDLERS; }

    public static HandlerList getHandlerList() { return HANDLERS; }
}