package db;

import java.util.ArrayList;
import db.exception.EntityNotFoundException;

public class Database {

    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static int nextId = 1;

    private Database() {}

    public static void add(Entity e){
        e.id = nextId++;
        entities.add(e);
    }

    public static Entity get(int id){
        for (Entity e : entities){
            if (e.id == id){
                return e;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException{
        Entity e = get(id);
        entities.remove(e);
    }

    public static void update(Entity e) throws EntityNotFoundException{
        Entity e_update = get(e.id);
        int index = entities.indexOf(e_update);
        entities.set(index, e);
    }



}
