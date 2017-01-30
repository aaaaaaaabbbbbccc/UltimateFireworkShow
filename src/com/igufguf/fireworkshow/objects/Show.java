package com.igufguf.fireworkshow.objects;

import com.igufguf.fireworkshow.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
public class Show extends ArrayList<Frame> implements ConfigurationSerializable {

    private ArrayList<Integer> taskids = new ArrayList<Integer>();
    private boolean running = false;

    public void play() {
        if ( running ) return;
        running = true;

        long current = 0;
        for ( final Frame f : this ) {
            current += f.getDelay();
            int tid = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
                @Override
                public void run() {
                    f.play();
                }
            }, current);


            taskids.add(tid);
            if ( taskids.size() == this.size() ) {
                tid = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
                    @Override
                    public void run() {
                        running = false;
                        taskids.clear();
                    }
                }, current);
                taskids.add(tid);
            }
        }
    }

    public void stop() {
        if ( !running ) return;

        for ( int id : taskids ) {
            Bukkit.getScheduler().cancelTask(id);
        }
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("frames", this);

        return map;
    }

    public static Show deserialize(Map<String, Object> args) {
        Show show = new Show();
        for ( Frame f : (Show) args.get("frames") ) {
            show.add(f);
        }
        return show;
    }
}
