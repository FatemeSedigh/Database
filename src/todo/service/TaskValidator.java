package todo.service;

import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;
import todo.entity.Task;

public class TaskValidator implements Validator{

    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Task)) {
            throw new IllegalArgumentException("ورودی باید از نوع Task باشد");
        }

        Task task = (Task) entity;
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new InvalidEntityException("عنوان تسک نمی‌تواند خالی باشد");
        }
    }
}
