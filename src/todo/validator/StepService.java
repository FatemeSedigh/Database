package todo.validator;

import db.Database;
import todo.entity.Step;

public class StepService {

    static {
        Database.registerValidator(Step.class.getSimpleName(), new StepValidator());
    }



}
