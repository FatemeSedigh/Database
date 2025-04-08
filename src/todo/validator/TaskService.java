package todo.validator;

import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;
import todo.service.TaskValidator;

import java.util.Date;
import java.util.List;

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

        if (newStatus == Status.Completed) {
            completeAllStepsForTask(taskId);
        }

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

    public static void updateTaskDueDate(int taskId, Date newDueDate)
            throws EntityNotFoundException, InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setDueDate(newDueDate);
        Database.update(task);
    }

    public static void deleteTask(int taskId)
            throws EntityNotFoundException {
        List<Step> taskSteps = getAllStepsForTask(taskId);
        for (Step step : taskSteps) {
            Database.delete(step.id);
        }
        Database.delete(taskId);
    }


}
