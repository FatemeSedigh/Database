package todo.validator;

import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;
import todo.service.TaskValidator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Task> getAllTasks() {
        try {
            return Database.getAll(Task.class.getSimpleName()).stream().map(Task.class::cast).sorted(Comparator.comparing(Task::getDueDate)).collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error fetching tasks: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }

    public List<Task> getIncompleteTasks() {
        try {
            return getAllTasks().stream()
                    .filter(task -> task.getStatus() != Task.Status.Completed)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error fetching incomplete tasks: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }

    public static Task getTaskById(int taskId) throws EntityNotFoundException {
        return (Task) Database.get(taskId);
    }

    public static List<Step> getAllStepsForTask(int taskId) {
        return Database.getAll(Step.class.getSimpleName()).stream()
                .map(entity -> (Step) entity)
                .filter(step -> step.getTaskRef() == taskId)
                .collect(Collectors.toList());
    }

    private static void completeAllStepsForTask(int taskId)
            throws EntityNotFoundException, InvalidEntityException {
        List<Step> steps = getAllStepsForTask(taskId);
        for (Step step : steps) {
            if (step.getStatus() != Step.Status.Completed) {
                step.setStatus(Step.Status.Completed);
                Database.update(step);
            }
        }
    }

    public static void updateTaskStatusBasedOnSteps(int taskId)
            throws EntityNotFoundException, InvalidEntityException {
        List<Step> steps = getAllStepsForTask(taskId);
        Task task = (Task) Database.get(taskId);

        boolean allCompleted = steps.stream()
                .allMatch(step -> step.getStatus() == Step.Status.Completed);

        boolean anyCompleted = steps.stream()
                .anyMatch(step -> step.getStatus() == Step.Status.Completed);

        if (allCompleted && !steps.isEmpty()) {
            task.setStatus(Task.Status.Completed);
        } else if (anyCompleted) {
            task.setStatus(Task.Status.InProgress);
        } else {
            task.setStatus(Task.Status.NotStarted);
        }

        Database.update(task);
    }
}

