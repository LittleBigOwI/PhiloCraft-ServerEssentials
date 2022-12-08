package dev.littlebigowl.serveressentials.models;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.flowpowered.math.vector.Vector2d;

import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.ExtrudeMarker;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.math.Color;
import de.bluecolored.bluemap.api.math.Shape;
import dev.littlebigowl.serveressentials.ServerEssentials;

public class Area {
    
    private String name;
    private String id;
    private Shape shape;

    public Area(String areaName, UUID playerUUID, Shape shape) {
        this.name = areaName;
        this.id = playerUUID.toString() + "." + areaName;
        this.shape = shape;
    }

    private static boolean compareVector2d(Vector2d vector1, Vector2d vector2) {
        return (vector1.getX() == vector2.getX() && vector1.getY() == vector2.getY());
    }

    private static boolean compareSides(Vector2d side1start, Vector2d side1end, Vector2d side2start, Vector2d side2end) {
        return (compareVector2d(side1start, side2start) && compareVector2d(side1end, side2end)) || (compareVector2d(side1start, side2end) && compareVector2d(side1end, side2start));
    }

    public Vector2d[] isTouching(Shape shape) {
        boolean touching = false;

        int i = 0;
        int j = 0;
        while(!(touching) && i < this.shape.getPointCount() - 1) {            
            j = 0;
            while(!(touching) && j < shape.getPointCount() - 1) {
                touching = compareSides(this.shape.getPoints()[i], this.shape.getPoints()[i+1], shape.getPoints()[j], shape.getPoints()[j+1]);
                j++;
            }
            if(!(touching)) {
                touching = compareSides(this.shape.getPoints()[i], this.shape.getPoints()[i+1], shape.getPoints()[3], shape.getPoints()[0]);
            }
            i++;
        }

        if(!(touching)) {
            i = this.shape.getPointCount() - 1;
            j = 0;
            while(!(touching) && j < shape.getPointCount() - 1) {
                touching = compareSides(this.shape.getPoints()[i], this.shape.getPoints()[0], shape.getPoints()[j], shape.getPoints()[j+1]);
                j++;
            }
        }

        if(touching) {
            if(i == this.shape.getPointCount() - 1) {
                return new Vector2d[]{this.shape.getPoints()[i], this.shape.getPoints()[0]};
            }
            return new Vector2d[]{this.shape.getPoints()[i], this.shape.getPoints()[i+1]};
        } else {
            return null;
        }
    }

    public void create(Player player) {

        ExtrudeMarker marker = ExtrudeMarker.builder()
            .label("TestName")
            .shape(this.shape, -64, 320)
            .lineColor(new Color(255, 0, 0, (float)1.0))
            .fillColor(new Color(200, 0, 0, (float)0.3))
            .minDistance(0.0)
            .build();

        MarkerSet markerSet = MarkerSet.builder()
            .label(this.name)
            .build();
        
        markerSet.put(this.id + ".marker", marker);
                
        ServerEssentials.blueMapAPI.getWorld("world").ifPresent(world -> {
            for(BlueMapMap map : world.getMaps()) {
                map.getMarkerSets().put(this.id + ".markerset", markerSet);
            }
        });
    }

    public String getName() {
        return this.name;
    }

}
