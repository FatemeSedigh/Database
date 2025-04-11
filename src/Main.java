import db.*;
import db.exception.*;
import todo.entity.*;
import todo.service.*;
import todo.validator.TaskService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws InvalidEntityException {
        while (true) {
            String command = sc.nextLine();
            if (command.equals("exit")) {
                break;
            }
            commandline(command);
            System.out.println();
        }
    }

    public static void commandline(String command) {
        try {
            switch (command) {
                case "add task":
                    addTask();
                    break;
                case "add step":
                    addStep();
                    break;
                case "update task":
                    updateTask();
                    break;
                case "update step":
                    updateStep();
                    break;
                case "delete":
                    delete();
                    break;
                case "get task-by-id":
                    getTaskById();
                    break;
                case "get incomplete-tasks":
                    getIncompleteTasks();
                    break;
                case "get all-tasks":
                    getAllTasks();
                    break;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addTask() throws InvalidEntityException {
        System.out.println("Title:");
        String title = sc.nextLine();

        System.out.println("Description:");
        String description = sc.nextLine();

        System.out.println("Due date:");
        String dueDateString = sc.nextLine();

        Date dueDate = TaskService.date(dueDateString);

        int id = TaskService.createTask(title, description, dueDate);

        System.out.println("Task saved successfully.");
        System.out.println("ID: " + id);
    }

    public static void addStep() throws InvalidEntityException {
        System.out.println("TaskID:");
        int taskId = Integer.parseInt(sc.nextLine());

        Task task = TaskService.getTaskById(taskId);

        System.out.println("Title:");
        String title = sc.nextLine();

        int id = StepService.createStep(taskId, title);
        System.out.println("Step saved successfully.");
        System.out.println("ID: " + id);
    }

    public static void updateTask() throws InvalidEntityException {
        System.out.println("ID:");
        int id = Integer.parseInt(sc.nextLine());

        Task task = TaskService.getTaskById(id);

        System.out.println("Field:");
        String field = sc.nextLine();
        System.out.println("New Value:");
        String newValue;
        String oldValue;
        switch (field) {
            case "title":
                oldValue = task.getTitle();
                newValue = sc.nextLine();
                TaskService.updateTaskTitle(id, newValue);
                break;
            case "description":
                oldValue = task.getDescription();
                newValue = sc.nextLine();
                TaskService.updateTaskDescription(id, newValue);
                break;
            case "due date":
                oldValue = dateFormat.format(task.getDueDate());
                newValue = sc.nextLine();
                Date dueDate = TaskService.date(oldValue);
                TaskService.updateTaskDueDate(id, dueDate);
                break;
            case "status":
                newValue = sc.nextLine();
                oldValue = task.getStatus().toString();
                Task.Status newStatus = Task.Status.valueOf(newValue);
                TaskService.updateTaskStatus(id, newStatus);
                break;
            default:
                System.out.println("Invalid field.");
                return;
        }

        System.out.println("Successfully updated the task.");
        System.out.println("Field: " + field);
        System.out.println("Old Value: " + oldValue);
        System.out.println("New Value: " + newValue);
        System.out.println("Modification Date: " + task.getLastModificationDate());

    }

    public static void updateStep() throws InvalidEntityException {
        System.out.println("ID:");
        int id = Integer.parseInt(sc.nextLine());

        Step step = StepService.getStepById(id);

        System.out.println("Field:");
        String field = sc.nextLine();
        System.out.println("New Value:");
        String newValue;
        String oldValue;
        switch (field) {
            case "title":
                oldValue = step.getTitle();
                newValue = sc.nextLine();
                StepService.updateStepTitle(id, newValue);
                break;
            case "status":
                newValue = sc.nextLine();
                oldValue = step.getStatus().toString();
                Step.Status newStatus = Step.Status.valueOf(newValue);
                StepService.updateStepStatus(id, newStatus);
                break;
            default:
                System.out.println("Invalid field.");
                return;
        }

        System.out.println("Successfully updated the step.");
        System.out.println("Field: " + field);
        System.out.println("Old Value: " + oldValue);
        System.out.println("New Value: " + newValue);

    }

    public static void delete() throws InvalidEntityException {
        System.out.println("ID:");
        int id = Integer.parseInt(sc.nextLine());
        Entity entity = Database.get(id);
        if (entity instanceof Task) {
            TaskService.deleteTask(id);
        }
        if (entity instanceof Step) {
            StepService.deleteStep(id);
        }
    }

    public static void getTaskById() {
        System.out.print("ID: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Task task = TaskService.getTaskById(id);
        printTask(task);
    }

    public static void getIncompleteTasks() {
        ArrayList<Task> tasks = TaskService.incompleteTasks();
        for (Task task : tasks) {
            printTask(task);
        }
    }

    public static void getAllTasks() {
        ArrayList<Task> tasks = TaskService.sortedTasks();
        for (Task task : tasks) {
            printTask(task);
        }
    }

    public static void printTask(Task task) {
        System.out.println("ID: " + task.id);
        System.out.println("Title: " + task.title);
        System.out.println("Due Date: " + dateFormat.format(task.dueDate));
        System.out.println("Status: " + task.getStatus());
        ArrayList<Step> steps = TaskService.getAllStepsForTask(task.id);
        if (!steps.isEmpty()) {
            System.out.println("Steps:");
            for (Step step : steps) {
                System.out.println("    + " + step.getTitle() + ":");
                System.out.println("        ID: " + step.id);
                System.out.println("        Status: " + step.getStatus());
            }
        }
        System.out.println();
    }
}