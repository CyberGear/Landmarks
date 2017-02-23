package lt.markav.legendsoflayouts.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({FragmentsType.NATIVE, FragmentsType.SUPPORT})
public @interface FragmentsType {

    String NATIVE = "FragmentsType.NATIVE";
    String SUPPORT = "FragmentsType.SUPPORT";

}
