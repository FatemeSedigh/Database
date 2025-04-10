package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final List<Entity> entities = new ArrayList<>();
    private static int nextId = 1;
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    private Database() {}

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator برای این entityCode قبلا ثبت شده");
        }
        validators.put(entityCode, validator);
    }

    public static void add(Entity e) throws InvalidEntityException {

        Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        if (e instanceof Trackable) {
            Trackable trackable = (Trackable) e;
            Date now = new Date();
            trackable.setCreationDate(now);
            trackable.setLastModificationDate(now);
        }

        e.id = nextId;
        entities.add(e.copy());
        nextId++;
    }


    public static Entity get(int id){
        for (Entity e : entities){
            if (e.id == id){
                return e.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException{
        Entity e = get(id);
        entities.remove(e);
    }

    public static void update(Entity e) throws InvalidEntityException {
        Entity existing = null;
        try {
            existing = get(e.id);
        } catch (EntityNotFoundException ex) {
            throw new InvalidEntityException("Entity with id " + e.id + " not found");
        }

        int index = -1;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new IllegalStateException("Entity with id " + e.id + " not found in list despite get() success");
        }

        Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        if (e instanceof Trackable) {
            ((Trackable) e).setLastModificationDate(new Date());
        }

        entities.set(index, e.copy());
    }

    public static ArrayList<Entity> getAll(int entityCode) {
        ArrayList<Entity> entityList = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getEntityCode() == entityCode) {
                entityList.add(entity.copy());
            }
        }
        return entityList;
    }

}
