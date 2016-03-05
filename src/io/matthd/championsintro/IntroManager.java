package io.matthd.championsintro;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityZombie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthew on 2016-03-02.
 */
public class IntroManager {

    private ChampionsIntro instance = ChampionsIntro.getInstance();

    private List<Player> introPlayers = new ArrayList<>();
    private Map<Player, HashMap<Integer, List<Entity>>> visibleEntites = new HashMap<>();


    public void startIntroForPlayer(Player player){
        introPlayers.add(player);
        visibleEntites.put(player, new HashMap<>());
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 4));
        player.teleport(new Location(player.getWorld(), 130.644, 116.5, -96.518, -90,0));
        playTitle(player);
    }

    public void playTitle(Player player){
        final int[] id = {0};
        new BukkitRunnable() {
            public void run(){
                if (id[0] >= instance.getConfig().getStringList("title-messages").size()){
                    cancel();
                    rollSceneTwo(player);
                }
                else {
                    player.sendTitle(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getStringList("title-messages").get(id[0])), "");
                }
                id[0]++;
            }
        }.runTaskTimer(instance, 0L, 20 * 5);
    }

    public void rollSceneTwo(Player player) {

        for (PotionEffect eff : player.getActivePotionEffects()) {
            player.removePotionEffect(eff.getType());
        }

        player.sendMessage(ChatColor.GOLD + "Here they come... Champion, we need your help!");
        Location startPoint = new Location(player.getWorld(), 157, 116.6, -98);

        new BukkitRunnable() {
            public void run() {
                player.sendMessage(org.bukkit.ChatColor.GOLD + "Get Ready!");
                for (int i = 0; i < 4; i++) {
                    Location toSpawn = startPoint.clone().add(0, 0, -i);
                    Zombie zom = startPoint.getWorld().spawn(toSpawn, Zombie.class);
                    visibleEntites.get(player).put(2, new ArrayList<>());
                    visibleEntites.get(player).get(2).add(zom);
                    followPlayer(player, zom, 0.75);

                    new BukkitRunnable() {
                        public void run() {
                            rollSceneThree(player);
                        }
                    }.runTaskLater(instance, 20 * 5L);
                }
            }
        }.runTaskLater(instance, 20 * 3L);
    }

    public void rollSceneThree(Player player){
        clearEntitiesFromStage(2, player);
    }

    public void clearEntitiesFromStage(int stageID, Player player){
        for(Map.Entry<Player, HashMap<Integer, List<Entity>>> entry : visibleEntites.entrySet()){
            if(entry.getKey().getName().equalsIgnoreCase(player.getName())){

                for(Map.Entry<Integer, List<Entity>> entryList : entry.getValue().entrySet()){
                    if(entryList.getKey() == stageID){
                        for(Entity e : entryList.getValue()){
                            e.remove();
                        }
                    }
                    entryList.getValue().clear();
                }
            }
        }
    }

    public void followPlayer(Player player, LivingEntity entity, double d) {
        final LivingEntity e = entity;
        final Player p = player;
        final float f = (float) d;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> ((EntityInsentient) ((CraftEntity) e).getHandle()).getNavigation().a(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), f), 0 * 20, 2 * 20);
    }

    public void movePlayerToLocation(Player player, Location location, int speed){

    }

    public List<Player> getIntroPlayers() {
        return introPlayers;
    }
}
