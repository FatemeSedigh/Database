package todo.entity;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Task extends Entity implements Trackable {
    public enum Status {
        NotStarted, InProgress , Completed
    }

    public String title;
    public String description;
    public Date dueDate;
    private Status status;
    private Date creationDate;
    private Date lastModificationDate;



}
