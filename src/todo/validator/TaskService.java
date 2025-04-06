package todo.validator;

import db.Database;
import db.exception.InvalidEntityException;
import todo.entity.Task;
import todo.service.TaskValidator;

public class TaskService {

    static {
        Database.registerValidator(1, new TaskValidator());
    }

    public static void createTask(String title, String description, Date dueDate)
            throws InvalidEntityException {
        Task task = new Task(title, description, dueDate);
        Database.add(task);
    }



}
