package ru.topjava.lunchvote.util;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import ru.topjava.lunchvote.HasId;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.AbstractEntity;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, long id, Class clazz) {
        checkNotFoundWithId(object != null, id, clazz);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, long id, Class clazz) {
        checkNotFound(found, "id=" + id, clazz);
    }

    public static <T> T checkNotFound(T object, String msg, Class clazz) {
        checkNotFound(object != null, msg, clazz);
        return object;
    }

    public static void checkNotFound(boolean found, String msg, Class clazz) {
        if (!found) {
            throw new NotFoundException("Not found entity [" + clazz.getSimpleName() + "] with " + msg);
        }
    }

    public static void checkNew(HasId entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new.");
        }
    }

    public static void assureIdConsistent(HasId entity, long id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }
}
