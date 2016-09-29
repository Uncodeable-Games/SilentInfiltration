package de.mih.core.levedit.EditorScene.Map.Types;



/**
 * Created by Cataract on 13.09.2016.
 */
public class Map {

    public String mapname;
    public Tile[][] tiles;
    public int tilesize,height,width;

    public Map(String mapname, int tilesize, int height, int width) {
        this.mapname = mapname;
        this.tilesize = tilesize;
        this.height = height;
        this.width = width;

        this.tiles = new Tile[width][height];

        for (int i = 0;i<tiles.length;i++){
            for(int j = 0; j<tiles[0].length;j++){
                tiles[i][j] = new Tile(i,j);
            }
        }
    }
}
