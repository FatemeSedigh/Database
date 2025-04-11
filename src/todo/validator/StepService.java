package todo.validator;

import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.service.StepValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<Step> getAllStepsForTask(int taskId) {
        try {
            return Database.getAll(Step.class.getSimpleName()).stream()
                    .map(Step.class::cast)
                    .filter(step -> step.getTaskRef() == taskId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("خطا در دریافت قدم‌ها: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static Step getStepById(int stepId) throws EntityNotFoundException {
        return (Step) Database.get(stepId);
    }

    public static void updateStepStatus(int stepId, Step.Status newStatus)
            throws EntityNotFoundException, InvalidEntityException {
        Step step = getStepById(stepId);
        step.setStatus(newStatus);
        Database.update(step);
        TaskService.updateTaskStatusBasedOnSteps(step.getTaskRef());
    }

    public static void updateStepTitle(int stepId, String newTitle)
            throws EntityNotFoundException, InvalidEntityException {
        Step step = getStepById(stepId);
        step.setTitle(newTitle);
        Database.update(step);
    }

    public static void deleteStep(int stepId)
            throws EntityNotFoundException, InvalidEntityException {
        Step step = getStepById(stepId);
        int taskId = step.getTaskRef();
        Database.delete(stepId);
        TaskService.updateTaskStatusBasedOnSteps(taskId);
    }

    public static long getCompletedStepsCount(int taskId) {
        return getAllStepsForTask(taskId).stream()
                .filter(step -> step.getStatus() == Step.Status.Completed)
                .count();
    }

    public static boolean areAllStepsCompleted(int taskId) {
        List<Step> steps = getAllStepsForTask(taskId);
        return !steps.isEmpty() &&
                steps.stream().allMatch(s -> s.getStatus() == Step.Status.Completed);
    }

}
