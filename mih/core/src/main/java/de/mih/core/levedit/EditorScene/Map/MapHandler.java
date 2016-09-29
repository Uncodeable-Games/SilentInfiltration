package de.mih.core.levedit.EditorScene.Map;

import de.mih.core.levedit.EditorScene.Map.Types.Map;

import java.io.File;

/**
 * Created by Cataract on 13.09.2016.
 */
public class MapHandler {
    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }


    public void newMap(String name,int tilesize, int height, int width){
        File dir = new File(".tmp");
        if (dir.exists()) deleteFolder(dir);
        dir.mkdir();
        new Map(name,tilesize,height,width);
    }
}
