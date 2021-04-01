package ru.topjava.lunchvote.util;

import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.AbstractEntity;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, long id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, long id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new.");
        }
    }

    public static void assureIdConsistent(AbstractEntity entity, long id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }
}
