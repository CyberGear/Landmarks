package lt.markav.legendsoflayouts.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({FragmentsType.ALL, FragmentsType.NATIVE, FragmentsType.SUPPORT})
public @interface FragmentsType {

    String ALL = "FragmentsType.ALL";
    String NATIVE = "FragmentsType.NATIVE";
    String SUPPORT = "FragmentsType.SUPPORT";

}
