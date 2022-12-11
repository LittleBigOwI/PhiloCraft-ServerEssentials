package dev.littlebigowl.serveressentials.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

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
    private ArrayList<Shape> chunks = new ArrayList<>();
    private Shape shape;

    public Area(String areaName, UUID playerUUID, Shape shape) {
        this.name = areaName;
        this.id = playerUUID.toString() + "." + areaName;
        this.chunks.add(shape);
        this.shape = shape;
    }

    private static boolean compareVector2d(Vector2d vector1, Vector2d vector2) {
        return (vector1.getX() == vector2.getX() && vector1.getY() == vector2.getY());
    }

    private static boolean compareSides(Vector2d side1start, Vector2d side1end, Vector2d side2start, Vector2d side2end) {
        return (compareVector2d(side1start, side2start) && compareVector2d(side1end, side2end)) || (compareVector2d(side1start, side2end) && compareVector2d(side1end, side2start));
    }

    private boolean containsPoint(Vector2d point) {
        Vector2d[] points = this.shape.getPoints();
        int i = 0;
        
        while(i < this.shape.getPointCount() && !(compareVector2d(point, points[i]))) {
            i += 1;
        }

        if(i == this.shape.getPointCount()) {
            return false;
        }   
        return true;
    }

    private ArrayList<Vector2d[]> getCommonSides(Shape shape) {
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

    private List<Vector2d> getArrayPoints() {
        return new ArrayList<>(Arrays.asList(this.shape.getPoints()));
    }

    private Shape merge(Shape shape) {

        ArrayList<Vector2d[]> commonSides = this.getCommonSides(shape);
        if(commonSides.size() == 0) {
            return null;
        }
        
        List<Vector2d> points = this.getArrayPoints();
        List<Vector2d> newPoints = new ArrayList<>();
        if(commonSides.size() == 1) {
            int i = 0;
            while(i < this.shape.getPointCount() - 1) {
                if(compareSides(points.get(i), points.get(i+1), commonSides.get(0)[0], commonSides.get(0)[1])) {
                    newPoints.addAll(points.subList(0, i+1));
                    
                    int j = 0;
                    while(j < shape.getPointCount() && this.containsPoint(shape.getPoint(j))) {
                        j += 1;
                    }
                    
                    if(j != shape.getPointCount()) {
                        if(j != 3) {
                            if(compareVector2d(shape.getPoint(j+1), points.get(i+1))) {
                                newPoints.add(shape.getPoint(3));
                                newPoints.add(shape.getPoint(0));
                            } else {
                                newPoints.add(shape.getPoint(j));
                                newPoints.add(shape.getPoint(j+1));
                            }
                        } else {
                            if(compareVector2d(shape.getPoint(0), points.get(i+1))) {
                                newPoints.add(shape.getPoint(2));
                                newPoints.add(shape.getPoint(3));
                            } else {
                                newPoints.add(shape.getPoint(j));
                                newPoints.add(shape.getPoint(0));
                            }
                        }
                    }
                    newPoints.addAll(points.subList(i+1, this.shape.getPointCount()));
                }
                i += 1;
            }
            
            if(compareSides(points.get(this.shape.getPointCount() - 1), points.get(0), commonSides.get(0)[0], commonSides.get(0)[1])) {
                newPoints.addAll(points);
                if(compareVector2d(newPoints.get(newPoints.size() - 1), shape.getPoint(3))) {
                    newPoints.add(shape.getPoint(0));
                    newPoints.add(shape.getPoint(1));
                } else {
                    newPoints.add(shape.getPoint(3));
                    newPoints.add(shape.getPoint(0));
                }
            }
            
        } else if(commonSides.size() == 2) {
            int i = 0;
            while(i < this.shape.getPointCount() - 2) {
                if(compareSides(points.get(i), points.get(i+1), commonSides.get(0)[0], commonSides.get(0)[1]) && compareVector2d(points.get(i+2), commonSides.get(1)[1])) {
                    newPoints.addAll(points.subList(0, i+1));
                    
                    for(Vector2d point : shape.getPoints()) {
                        if(!this.containsPoint(point)) {
                            newPoints.add(point);                            
                        }
                    }

                    newPoints.addAll(points.subList(i+2, this.shape.getPointCount()));
                }
                i += 1;
            }

            if(newPoints.size() == 0) {
                Vector2d[] commonSide = commonSides.get(0);

                int j = 0;
                while(j < this.shape.getPointCount() && !compareVector2d(this.shape.getPoint(j), commonSide[0])) {
                    j += 1;
                }
                
                newPoints.addAll(points.subList(0, j+1));
                newPoints.add(shape.getPoint(3));
                newPoints.add(shape.getPoint(0));
                newPoints.addAll(points.subList(j+1, points.size()));
            }
        }
        
        return new Shape(newPoints);
    }

    public boolean addChunk(Shape shape) {
        if(this.getCommonSides(shape).size() == 0) {
            return false;
        }
        
        this.chunks.add(shape);
        return true;
    }

    public void sortChunks() {
        ArrayList<Shape> sortedChunks = new ArrayList<>();
        Double[][] chunkCorners = new Double[this.chunks.size()][2];

        for(int i = 0; i < this.chunks.size(); i++) {
            chunkCorners[i] = new Double[]{this.chunks.get(i).getPoint(0).getY()*-1, this.chunks.get(i).getPoint(0).getX()};
        }
        

        for(Double[] corner : chunkCorners) {
            Bukkit.getLogger().warning("[DEBUG] : (" + corner[0] + ", " + corner[1] + ")");
        }
        Bukkit.getLogger().warning("[DEBUG] : Sort");

        Arrays.sort(chunkCorners, new Comparator<Double[]>() {
            public int compare(Double[] a, Double[] b)
            {
                int res = a[0].compareTo(b[0]);
                if(res!=0)
                    return res;
                return a[1].compareTo(b[1]);
            }
        });

        for(Double[] corner : chunkCorners) {
            Bukkit.getLogger().warning("[DEBUG] : (" + corner[1] + ", " + corner[0]*-1 + ")");
            Double x = corner[1];
            Double z = corner[0]*-1;
            sortedChunks.add(new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x+16, z+16), new Vector2d(x+16, z)));
        }

        this.chunks = sortedChunks;
    }

    public void draw() {
        sortChunks(); 
        this.shape = this.chunks.get(0);
        for(int i = 1; i < this.chunks.size(); i++) {
            this.shape = this.merge(this.chunks.get(i));
        }
        
        /*if(this.chunks.size() > 1) {
            this.shape = merge(this.chunks.get(this.chunks.size() - 1));
        }*/

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

    public static Area getAreaByName(UUID playerUUID, String name) {
        ArrayList<Area> areas = ServerEssentials.database.playerAreas.get(playerUUID);
        if(areas.get(0).getName().equals(name)) {
            return areas.get(0);
        }
        return areas.get(1);
    }

}
