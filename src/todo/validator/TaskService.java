package todo.validator;

import db.Database;
import todo.service.TaskValidator;

public class TaskService {

    static {
        Database.registerValidator(1, new TaskValidator());
    }



}
