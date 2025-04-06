package todo.validator;

import db.Database;
import db.exception.EntityNotFoundException;
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

    public static void updateTaskStatus(int taskId, Task.Status newStatus)
            throws EntityNotFoundException, InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(newStatus);
        Database.update(task);
    }

}
