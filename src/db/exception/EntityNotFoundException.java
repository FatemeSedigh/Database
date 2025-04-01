package db.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(int id) {
        super("موجودیتی با شناسه " + id + " یافت نشد");
    }
}