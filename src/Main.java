package todo;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Task;
import todo.entity.Task.TaskStatus;
import todo.service.StepService;
import todo.service.TaskService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        System.out.println("=== سیستم مدیریت تسک‌ها ===");

        try {
            // تست ایجاد تسک
            testTaskCreation();

            // تست ایجاد قدم
            testStepCreation();

            // تست به‌روزرسانی
            testUpdates();

            // تست نمایش اطلاعات
            testDisplayFunctions();

            // تست حذف
            testDeletion();

        } catch (Exception e) {
            System.err.println("خطای غیرمنتظره: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            System.out.println("\n=== پایان تست‌ها ===");
        }
    }

    private static void testTaskCreation() throws InvalidEntityException, ParseException {
        System.out.println("\n--- تست ایجاد تسک ---");

        // ایجاد تسک معتبر
        Date dueDate = dateFormat.parse("2025-06-30");
        int taskId = TaskService.createTask("پروژه پایانی", "پیاده‌سازی سیستم مدیریت تسک", dueDate);
        System.out.println("تسک ایجاد شد با ID: " + taskId);

        // تست ایجاد تسک نامعتبر
        try {
            TaskService.createTask("", "توضیحات خالی", dueDate);
        } catch (InvalidEntityException e) {
            System.out.println("تسک نامعتبر به درستی شناسایی شد: " + e.getMessage());
        }
    }

    private static void testStepCreation() throws EntityNotFoundException, InvalidEntityException {
        System.out.println("\n--- تست ایجاد قدم ---");

        // ایجاد قدم معتبر
        int stepId = StepService.createStep(1, "طراحی معماری سیستم");
        System.out.println("قدم ایجاد شد با ID: " + stepId);

        // تست ایجاد قدم نامعتبر
        try {
            StepService.createStep(999, "قدم برای تسک ناموجود");
        } catch (EntityNotFoundException e) {
            System.out.println("تسک والد به درستی شناسایی نشد: " + e.getMessage());
        }
    }

    private static void testUpdates() throws EntityNotFoundException, InvalidEntityException {
        System.out.println("\n--- تست به‌روزرسانی‌ها ---");

        // به‌روزرسانی عنوان تسک
        TaskService.updateTaskTitle(1, "پروژه نهایی");
        System.out.println("عنوان تسک به‌روزرسانی شد");

        // به‌روزرسانی وضعیت قدم
        StepService.updateStepStatus(1, Step.StepStatus.COMPLETED);
        System.out.println("وضعیت قدم به‌روزرسانی شد");

        // نمایش وضعیت تسک پس از تغییر
        Task task = TaskService.getTaskById(1);
        System.out.println("وضعیت جدید تسک: " + task.getStatus());
    }

    private static void testDisplayFunctions() throws EntityNotFoundException {
        System.out.println("\n--- تست نمایش اطلاعات ---");

        // نمایش تمام تسک‌ها
        System.out.println("\nتمام تسک‌ها:");
        List<Task> allTasks = TaskService.getAllTasks();
        allTasks.forEach(t -> System.out.println("- " + t.getTitle() + " (وضعیت: " + t.getStatus() + ")"));

        // نمایش تسک‌های ناتمام
        System.out.println("\nتسک‌های ناتمام:");
        List<Task> incompleteTasks = TaskService.getIncompleteTasks();
        incompleteTasks.forEach(t -> System.out.println("- " + t.getTitle()));

        // نمایش قدم‌های یک تسک
        System.out.println("\nقدم‌های تسک 1:");
        List<Step> steps = StepService.getAllStepsForTask(1);
        steps.forEach(s -> System.out.println("- " + s.getTitle() + " (وضعیت: " + s.getStatus() + ")"));
    }

    private static void testDeletion() throws EntityNotFoundException, InvalidEntityException {
        System.out.println("\n--- تست حذف ---");

        // حذف قدم
        StepService.deleteStep(1);
        System.out.println("قدم با موفقیت حذف شد");

        // حذف تسک
        TaskService.deleteTask(1);
        System.out.println("تسک با موفقیت حذف شد");

        // تأیید حذف
        try {
            TaskService.getTaskById(1);
        } catch (EntityNotFoundException e) {
            System.out.println("تسک به درستی حذف شده است");
        }
    }
}