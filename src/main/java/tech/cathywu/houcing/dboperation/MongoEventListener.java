package tech.cathywu.houcing.dboperation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import tech.cathywu.houcing.annotation.CascadeCreate;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Collection;

@Slf4j
@Component
public class MongoEventListener extends AbstractMongoEventListener<Object> {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        final Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeCreate.class)) {
                final Object fieldValue = field.get(source);
                if (fieldValue == null) {
                    return;
                }
                if (ClassUtils.isAssignable(Collection.class, fieldValue.getClass())) {
                    Collection collection = (Collection) fieldValue;
                    collection.forEach(this::processEachData);
                } else {
                    processEachData(fieldValue);
                }
            }
        });
    }

    private void processEachData(Object fieldValue) {
        DbRefFieldCallback callback = new DbRefFieldCallback();
        ReflectionUtils.doWithFields(fieldValue.getClass(), callback);

        if (!callback.isIdFound()) {
            throw new NotImplementedException("Cannot perform cascade save on child object: No field of " + fieldValue.getClass().getSimpleName() + " annotated with @Id");
        }
        Field idField = ReflectionUtils.findField(fieldValue.getClass(), callback.getIdFieldName());
        Object id = null;
        try {
            id = idField.get(fieldValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (id != null) {
            log.warn("id is not empty, DBRef object would not be updated");
            return;
        }
        mongoOperations.save(fieldValue);
    }

    private static class DbRefFieldCallback implements ReflectionUtils.FieldCallback {

        private boolean idFound = false;
        private String idFieldName;

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            ReflectionUtils.makeAccessible(field);
            if (field.isAnnotationPresent(Id.class)) {
                idFound = true;
                idFieldName = field.getName();
            }
        }

        public boolean isIdFound() {
            return idFound;
        }

        public String getIdFieldName() {
            return idFieldName;
        }
    }
}
