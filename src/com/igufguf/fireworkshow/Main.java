package com.igufguf.fireworkshow;

import com.igufguf.fireworkshow.objects.Frame;
import com.igufguf.fireworkshow.objects.Show;
import com.igufguf.fireworkshow.objects.fireworks.NormalFireworks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Copyrighted 2017 iGufGuf
 *
 * This file is part of Ultimate Fireworkshow.
 *
 * Ultimate Fireworkshow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ultimate Fireworkshow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Ultimate Fireworkshow.  If not, see http://www.gnu.org/licenses/
 *
 **/
public class Main extends JavaPlugin {

    public static HashMap<String, Show> shows = new HashMap<String, Show>();

    public static FileConfiguration showsfile = new YamlConfiguration();
    public static Main main;

    @Override
    public void onEnable() {
        main = this;

        File f = new File(getDataFolder(), "shows.yml");
        if ( !f.exists() ) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ConfigurationSerialization.registerClass(Show.class);
        ConfigurationSerialization.registerClass(Frame.class);
        ConfigurationSerialization.registerClass(NormalFireworks.class);
        try {
            showsfile.load(f);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        for ( String key : showsfile.getKeys(false) ) {
            Show show = new Show();
            for ( Frame frame : (ArrayList<Frame>) showsfile.getList(key) ) {
                show.add(frame);
            }
            shows.put(key, show);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ( !cmd.getName().equalsIgnoreCase("fireworkshow") ) {
            return true;
        }

        if ( args.length == 2 && args[0].equalsIgnoreCase("play")) {
            if ( sender instanceof Player && !sender.hasPermission("fireworkshow.play")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if ( !shows.containsKey(args[1].toLowerCase()) ) {
                sender.sendMessage(ChatColor.RED + "That fireworkshow does not exist!");
                return true;
            }

            shows.get(args[1].toLowerCase()).play();
            sender.sendMessage(ChatColor.GREEN + "You just started fireworkshow " + ChatColor.DARK_GREEN + args[1].toLowerCase() + ChatColor.GREEN + "!");
        }

        if ( !(sender instanceof Player) ) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players!");
            return true;
        }

        if ( args.length == 2 && args[0].equalsIgnoreCase("create") ) {
            if ( !sender.hasPermission("fireworkshow.create") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            String name = args[1].toLowerCase();

            if ( shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name already exists!");
                return true;
            }

            shows.put(name, new Show());
            showsfile.set(name, shows.get(name));
            sender.sendMessage(ChatColor.GREEN + "You created a fireworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        } else if ( args.length == 2 && args[0].equalsIgnoreCase("delete") ) {
            if ( !sender.hasPermission("fireworkshow.delete") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if ( !shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            shows.remove(name);
            showsfile.set(name, null);
            sender.sendMessage(ChatColor.GREEN + "You deleted the fireworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        } else if ( args.length == 3 && args[0].equalsIgnoreCase("addframe") ) {
            if ( !sender.hasPermission("fireworkshow.addframe") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if ( !shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if ( !args[2].matches("[0-9]+") ) {
                sender.sendMessage(ChatColor.RED + "Invalid delay!");
                return true;
            }
            long delay = Long.valueOf(args[2]);

            if ( delay > 600 ) {
                sender.sendMessage(ChatColor.RED + "The delay can't be longer than 30 seconds!");
                return true;
            }

            shows.get(name).add(new Frame(delay));
            showsfile.set(name, shows.get(name));
            sender.sendMessage(ChatColor.GREEN + "You added a new frame (#" + ChatColor.YELLOW + shows.get(name).size() + ChatColor.GREEN + ") " +
                    "to the firworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        } else if ( args.length == 3 && args[0].equalsIgnoreCase("delframe") ) {
            if ( !sender.hasPermission("fireworkshow.delframe") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if ( !shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if ( !args[2].matches("[0-9]+") ||shows.get(name).size() < Integer.valueOf(args[2]) ) {
                sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                return true;
            }
            int frame = Integer.valueOf(args[2]);

            shows.get(name).remove(frame-1);
            showsfile.set(name, shows.get(name));
            sender.sendMessage(ChatColor.GREEN + "You removed a frame from the fireworkshow with name" + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        } else if ( args.length == 3 && args[0].equalsIgnoreCase("dupframe") ) {
            if ( !sender.hasPermission("fireworkshow.dupframe") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if ( !shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if ( !args[2].matches("[0-9]+") ||shows.get(name).size() < Integer.valueOf(args[2]) ) {
                sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                return true;
            }
            int frameid = Integer.valueOf(args[2]);

            Frame frame = shows.get(name).get(frameid-1);
            shows.get(name).add(frame);
            showsfile.set(name, shows.get(name));
            sender.sendMessage(ChatColor.GREEN + "You duplicated a frame (#" + ChatColor.YELLOW + shows.get(name).size() + ChatColor.GREEN + ") " +
                    "from the fireworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        } else if ( args.length >= 1 && args[0].equalsIgnoreCase("newfw") ) {
            if ( !sender.hasPermission("fireworkshow.newfw") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if ( !shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if ( args.length >= 2 ) {

                int frame = shows.get(name).size();
                if ( args.length == 2 ) {
                    if ( !args[2].matches("[0-9]+") ||shows.get(name).size() < Integer.valueOf(args[2]) ) {
                        sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                        return true;
                    }
                    frame = Integer.valueOf(args[2]);
                }

                Player p = (Player) sender;
                if ( p.getItemInHand() == null || p.getItemInHand().getType() != Material.FIREWORK ) {
                    sender.sendMessage(ChatColor.RED + "Please hold a firework or firework charge in your hand!");
                    return true;
                }
                FireworkMeta meta = (FireworkMeta) p.getItemInHand().getItemMeta();

                NormalFireworks nf = new NormalFireworks(meta, p.getLocation());
                shows.get(name).get(frame-1).add(nf);
                sender.sendMessage(ChatColor.GREEN + "You added firework to the fireworkshow with name " + ChatColor.DARK_GREEN + name
                        + ChatColor.GREEN + " on frame (#" + ChatColor.YELLOW + frame + ChatColor.GREEN + ")!");
            } else {
                Player p = (Player) sender;
                if ( p.getItemInHand() == null || p.getItemInHand().getType() != Material.FIREWORK ) {
                    sender.sendMessage(ChatColor.RED + "Please hold a firework or firework charge in your hand!");
                    return true;
                }
                FireworkMeta meta = (FireworkMeta) p.getItemInHand().getItemMeta();

                NormalFireworks nf = new NormalFireworks(meta, p.getLocation());
                shows.get(name).get(shows.get(name).size()-1).add(nf);
                sender.sendMessage(ChatColor.GREEN + "You added firework to the fireworkshow with name " + ChatColor.DARK_GREEN + name
                        + ChatColor.GREEN + " on frame (#" + ChatColor.YELLOW + shows.get(name).size() + ChatColor.GREEN + ")!");
            }
            showsfile.set(name, shows.get(name));
        }

        try {
            showsfile.save(new File(getDataFolder(), "shows.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
