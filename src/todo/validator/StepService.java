package todo.validator;

import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.service.StepValidator;

public class StepService {

    static {
        Database.registerValidator(Step.class.getSimpleName(), new StepValidator());
    }

    public static int createStep(int taskId, String title)
            throws InvalidEntityException, EntityNotFoundException {
        TaskService.getTaskById(taskId);

        Step step = new Step(title, taskId);
        Database.add(step);
        return step.id;
    }



}
