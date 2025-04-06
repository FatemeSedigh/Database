package todo.entity;

import db.Entity;

public class Step extends Entity {

    public enum Status {
        NotStarted, Completed
    }

    private String title;
    private Status status;
    private int taskRef;

    public Step(String title, int taskRef) {
        this.title = title;
        this.taskRef = taskRef;
        this.status = Status.NotStarted;
    }



}
