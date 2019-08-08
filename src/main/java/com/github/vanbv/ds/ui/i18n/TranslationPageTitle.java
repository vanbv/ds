package com.github.vanbv.ds.ui.i18n;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TranslationPageTitle {

    String value();
}
