package todo.service;

import db.Database;
import db.exception.*;
import todo.entity.*;
import todo.validator.StepValidator;

import java.util.List;

public class StepService {

    private StepService() {}

    static {
        Database.registerValidator(2, new StepValidator());
    }

    public static int createStep(int taskId, String title)
            throws InvalidEntityException, EntityNotFoundException {
        Task task = TaskService.getTaskById(taskId);
        Step step = new Step(title, taskId);
        Database.add(step);
        return step.id;
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
        return TaskService.getAllStepsForTask(taskId).stream()
                .filter(step -> step.getStatus() == Step.Status.Completed)
                .count();
    }

    public static boolean areAllStepsCompleted(int taskId) {
        List<Step> steps = TaskService.getAllStepsForTask(taskId);
        return !steps.isEmpty() &&
                steps.stream().allMatch(s -> s.getStatus() == Step.Status.Completed);
    }

}
