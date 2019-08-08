package com.github.vanbv.ds.ui.i18n;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.UIInitEvent;
import com.vaadin.flow.server.UIInitListener;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class TranslationPageTitleEngine implements VaadinServiceInitListener, UIInitListener, BeforeEnterListener {

    private final TranslationProvider translationProvider;

    public TranslationPageTitleEngine(TranslationProvider translationProvider) {
        this.translationProvider = translationProvider;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Class<?> navigationTarget = event.getNavigationTarget();
        TranslationPageTitle annotation = navigationTarget.getAnnotation(TranslationPageTitle.class);

        if (annotation != null) {
            UI.getCurrent().getPage().setTitle(
                    translationProvider.getTranslation("app.page.title",
                            translationProvider.getTranslation(annotation.value()))
            );
        }

    }

    @Override
    public void uiInit(UIInitEvent event) {
        event.getUI().addBeforeEnterListener(this);
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(this);
    }
}
