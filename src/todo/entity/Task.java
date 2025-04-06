package todo.entity;

import db.Entity;

public class Task extends Entity implements Trackable {
    public enum Status {
        NotStarted, InProgress , Completed
    }



}
