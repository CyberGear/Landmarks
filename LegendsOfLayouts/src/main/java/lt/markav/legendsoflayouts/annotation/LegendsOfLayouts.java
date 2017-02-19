package lt.markav.legendsoflayouts.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface LegendsOfLayouts {

    @Alias(forValue = "rClass")
    Class<?> value() default Void.class;

    @Alias(forValue = "value")
    Class<?> rClass() default Void.class;

    @FragmentsType
    String fragmentsType() default FragmentsType.SUPPORT;

}
