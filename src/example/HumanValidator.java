package example;

import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;

public class HumanValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {

        if (!(entity instanceof Human)) {
            throw new IllegalArgumentException("ورودی باید از نوع Human باشد");
        }
        Human human = (Human) entity;

        if (human.name == null || human.name.trim().isEmpty()) {
            throw new InvalidEntityException("نام نمی‌تواند خالی باشد");
        }


    }
}