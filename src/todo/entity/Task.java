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

    public Task(String title, String description, Date dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = Status.NotStarted;
    }

    @Override public void setCreationDate(Date date) { this.creationDate = date; }
    @Override public Date getCreationDate() { return creationDate; }
    @Override public void setLastModificationDate(Date date) { this.lastModificationDate = date; }
    @Override public Date getLastModificationDate() { return lastModificationDate; }


}
