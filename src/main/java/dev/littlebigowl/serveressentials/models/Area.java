package dev.littlebigowl.serveressentials.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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

    public ArrayList<Vector2d[]> getCommonSides(Shape shape) {
        ArrayList<Vector2d[]> commonSides = new ArrayList<>();

        int i = 0;
        int j = 0;
        while(i < this.shape.getPointCount() - 1) {            
            j = 0;
            while(j < shape.getPointCount() - 1) {
                if (compareSides(this.shape.getPoints()[i], this.shape.getPoints()[i+1], shape.getPoints()[j], shape.getPoints()[j+1])) {
                    commonSides.add(new Vector2d[]{this.shape.getPoints()[i], this.shape.getPoints()[i+1]});
                }
                j++;
            }
            if(compareSides(this.shape.getPoints()[i], this.shape.getPoints()[i+1], shape.getPoints()[3], shape.getPoints()[0])) {
                commonSides.add(new Vector2d[]{this.shape.getPoints()[i], this.shape.getPoints()[i+1]});
            }
            i++;
        }

    
        i = this.shape.getPointCount() - 1;
        j = 0;
        while(j < shape.getPointCount() - 1) {
            if(compareSides(this.shape.getPoints()[i], this.shape.getPoints()[0], shape.getPoints()[j], shape.getPoints()[j+1])) {
                commonSides.add(new Vector2d[]{this.shape.getPoints()[i], this.shape.getPoints()[0]});
            }
            j++;
        }
    
        return commonSides;
    }

    public List<Vector2d> getArrayPoints() {
        return new ArrayList<>(Arrays.asList(this.shape.getPoints()));
    }

    public void expand(Shape shape) {

        ArrayList<Vector2d[]> commonSides = this.getCommonSides(shape);
        if(commonSides.size() == 0) {
            return;
        }
        
        List<Vector2d> points = this.getArrayPoints();
        List<Vector2d> newPoints = new ArrayList<>();
        if(commonSides.size() == 1) {
            int i = 0;
            while(i < this.shape.getPointCount() - 1) {
                if(compareVector2d(points.get(i), commonSides.get(0)[0]) && compareVector2d(points.get(i+1), commonSides.get(0)[1])) {
                    newPoints.addAll(points.subList(0, i+1));
                    newPoints.add(shape.getPoints()[i]);
                    newPoints.add(shape.getPoints()[i+1]);
                    newPoints.addAll(points.subList(i+1, this.shape.getPointCount()));  
                }
                i += 1;
            }

            if(compareVector2d(points.get(this.shape.getPointCount() - 1), commonSides.get(0)[0]) && compareVector2d(points.get(0), commonSides.get(0)[1])) {
                i = 3;
                newPoints.addAll(points);
                newPoints.add(shape.getPoints()[3]);
                newPoints.add(shape.getPoints()[0]);
            }
            
            for(Vector2d point : newPoints) {
                Bukkit.getLogger().warning("[DEBUG] : (" + point.getX() + ", " + point.getY() + ")");
            }
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
