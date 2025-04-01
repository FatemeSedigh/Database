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

}
