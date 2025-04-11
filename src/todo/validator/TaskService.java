package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;
import todo.validator.TaskValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    private TaskService() {}

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

        if (newStatus == Task.Status.Completed) {
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

    public static Task getTaskById(int taskId) throws EntityNotFoundException {
        return (Task) Database.get(taskId);
    }

    public static ArrayList<Task> sortedTasks() {
        ArrayList<Entity> tasks = Database.getAll(1);
        ArrayList<Task> sortedTasks = new ArrayList<>();
        for (Entity entity : tasks) {
            Task task = (Task) entity;
            sortedTasks.add(task);
        }
        sortedTasks.sort(Comparator.comparing(task -> task.dueDate));
        return sortedTasks;
    }

    public static ArrayList<Task> incompleteTasks() {
        ArrayList<Task> tasks = sortedTasks();
        ArrayList<Task> incompleteTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus() == Task.Status.InProgress || task.getStatus() == Task.Status.NotStarted) {
                incompleteTasks.add(task);
            }
        }
        return incompleteTasks;
    }

    public static ArrayList<Step> getAllStepsForTask(int taskId) {
        ArrayList<Entity> step = Database.getAll(2);
        ArrayList<Step> steps = new ArrayList<>();
        for (Entity entity : step) {
            Step stepEntity = (Step) entity;
            if (taskId == stepEntity.getTaskRef()) {
                steps.add(stepEntity);
            }
        }
        return steps;
    }

    private static void completeAllStepsForTask(int taskId)
            throws EntityNotFoundException, InvalidEntityException {
        List<Step> steps = getAllStepsForTask(taskId);
        for (Step step : steps) {
            step.setStatus(Step.Status.Completed);
            Database.update(step);
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

    public static Date date(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot save task.\nError: Invalid date format.");
        }
    }

}

