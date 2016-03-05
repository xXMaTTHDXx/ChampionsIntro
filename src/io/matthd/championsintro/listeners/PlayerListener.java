package io.matthd.championsintro.listeners;

import io.matthd.championsintro.ChampionsIntro;
import io.matthd.championsintro.IntroManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Matthew on 2016-03-03.
 */
public class PlayerListener implements Listener {

    private ChampionsIntro instance = ChampionsIntro.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player pl = e.getPlayer();
        instance.getManager().startIntroForPlayer(pl);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(instance.getManager().getIntroPlayers().contains(e.getPlayer())){
            e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }

        Player pl = (Player) e.getEntity();
        if(instance.getManager().getIntroPlayers().contains(pl)){
            e.setCancelled(true);
            e.setDamage(0);
        }
    }
}
