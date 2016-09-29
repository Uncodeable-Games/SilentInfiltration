package de.mih.core.levedit.Entities;

import de.mih.core.levedit.Entities.Abstract.ComponentType;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Cataract on 28.09.2016.
 */
public class ComponentManager {
    ArrayList<ComponentType> allComponents = new ArrayList<>();

    public ComponentManager() {
        List<Class> classes = null;
        try {
            classes = getClasses(Thread.currentThread().getContextClassLoader(),"de/mih/core/levedit/Entities/Components");
            for(Class c:classes){
                allComponents.add(new ComponentType(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<Class> getClasses(ClassLoader cl, String pack) throws Exception{

        String dottedPackage = pack.replaceAll("[/]", ".");
        List<Class> classes = new ArrayList<>();
        URL upackage = cl.getResource(pack);

        DataInputStream dis = new DataInputStream((InputStream) upackage.getContent());
        String line = null;
        while ((line = dis.readLine()) != null) {
            if(line.endsWith(".class")) {
                classes.add(Class.forName(dottedPackage+"."+line.substring(0,line.lastIndexOf('.'))));
            }
        }
        return classes;
    }

    public ArrayList<ComponentType> getAllComponents() {
        return allComponents;
    }


}
