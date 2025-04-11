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

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public int getTaskRef() { return taskRef; }

    @Override
    public int getEntityCode() {
        return 2;
    }

    @Override
    public Entity copy() {
        Step copyStep = new Step(this.title, this.taskRef);
        copyStep.id = this.id;
        copyStep.status = this.status;
        return copyStep;
    }

}
