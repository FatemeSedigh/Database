package todo.service;

import db.Database;
import db.Entity;
import db.Validator;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;


public class StepValidator implements Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Step)) {
            throw new IllegalArgumentException("ورودی باید از نوع Step باشد");
        }

        Step step = (Step) entity;
        if (step.getTitle() == null || step.getTitle().trim().isEmpty()) {
            throw new InvalidEntityException("عنوان قدم نمی‌تواند خالی باشد");
        }

        try {
            Database.get(step.getTaskRef());
        } catch (EntityNotFoundException e) {
            throw new InvalidEntityException("تسک مرتبط با این قدم وجود ندارد");
        }
    }

}
