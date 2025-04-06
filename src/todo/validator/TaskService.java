package todo.validator;

import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Task;
import todo.service.TaskValidator;

import java.util.Date;

public class TaskService {

    static {
        Database.registerValidator(1, new TaskValidator());
    }

    public static int createTask(String title, String description, Date dueDate)
            throws InvalidEntityException {
        Task task = new Task(title, description, dueDate);
        Database.add(task);
        return task.id;
    }

    public static void updateTaskStatus(int taskId, Task.Status newStatus)
            throws EntityNotFoundException, InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(newStatus);
        Database.update(task);


    }

    public static void updateTaskTitle(int taskId, String newTitle)
            throws EntityNotFoundException, InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setTitle(newTitle);
        Database.update(task);
    }

    public static void updateTaskDescription(int taskId, String newDescription)
            throws EntityNotFoundException, InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setDescription(newDescription);
        Database.update(task);
    }



}
