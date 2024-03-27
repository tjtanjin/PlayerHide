package tk.taverncraft.playerhide.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import tk.taverncraft.playerhide.Main;

public class EventManager {
    private final Main main;
    private List<Listener> listeners;

    public EventManager(Main main) {
        this.main = main;
        registerEvents();
    }

    public void registerEvents() {
        listeners = new ArrayList<>();
        listeners.add(new PlayerHopOnEvent(main));
        listeners.add(new PlayerHopOffEvent(main));
        listeners.add(new PlayerThrowItemEvent(main));
        listeners.add(new PlayerUseItemEvent(main));
        listeners.add(new PlayerMoveItemEvent(main));

        for (Listener listener : listeners) {
            main.getServer().getPluginManager().registerEvents(listener, main);
        }
    }

    public void unregisterEvents() {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
        listeners.clear();
    }
}