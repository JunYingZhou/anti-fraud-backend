package team.weyoung.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AuthCheck is an annotation used for permission checking.
 * It can be applied to methods.
 * It is retained at runtime.
 *
 * @author <a href="https://github.com/Tunan81">图南</a>
 * @since 2023-12-18
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * Specifies a role that must be possessed for the annotated method to be accessed.
     * If no role is specified, the default is an empty string.
     *
     * @return A string representing the required role.
     */
    String mustRole() default "";

}