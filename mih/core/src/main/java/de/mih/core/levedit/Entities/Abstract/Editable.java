package de.mih.core.levedit.Entities.Abstract;

import java.lang.annotation.*;

/**
 * Created by Cataract on 29.09.2016.
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Editable {
    String value() default "";
    boolean bool() default false;
}
