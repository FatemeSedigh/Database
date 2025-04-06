package todo.entity;

import db.Entity;

public class Step extends Entity {

    public enum Status {
        NotStarted, Completed
    }

    private String title;
    private Status status;
    private int taskRef;


}
