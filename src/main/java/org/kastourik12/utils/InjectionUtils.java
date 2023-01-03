package org.kastourik12.utils;

import static org.burningwave.core.assembler.StaticComponentContainer.Fields;

import org.burningwave.core.classes.FieldCriteria;
import org.kastourik12.Injector;
import org.kastourik12.anotations.Autowired;
import org.kastourik12.anotations.Qualifier;

import java.lang.reflect.Field;
import java.util.Collection;

public class InjectionUtils {

    private InjectionUtils() {
        super();
    }

    /**
     * Perform injection recursively, for each service inside the Client class
     */
    public static void autowire(Injector injector, Class<?> classz, Object classInstance)
            throws InstantiationException, IllegalAccessException {

        Collection<Field> fields = Fields.findAllAndMakeThemAccessible(
                FieldCriteria.forEntireClassHierarchy().allThoseThatMatch(field ->
                        field.isAnnotationPresent(Autowired.class)), classz
        );
        for (Field field : fields) {
            String qualifier = field.isAnnotationPresent(Qualifier.class)
                    ? field.getAnnotation(Qualifier.class).value()
                    : null;
            Object fieldInstance = injector.getBeanInstance(field.getType(), field.getName(), qualifier);
            Fields.setDirect(classInstance, field, fieldInstance);
            autowire(injector, fieldInstance.getClass(), fieldInstance);
        }
    }

}

