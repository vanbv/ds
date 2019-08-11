package com.github.vanbv.ds.ui.views.error;

import com.github.vanbv.ds.ui.MainView;
import com.github.vanbv.ds.ui.i18n.TranslationPageTitle;
import com.github.vanbv.ds.ui.i18n.TranslationProvider;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainView.class)
@TranslationPageTitle("page.is.under.development")
public class DevelopmentView extends VerticalLayout {

    public DevelopmentView(TranslationProvider translationProvider) {
        Dialog dialog = new Dialog();
        dialog.add(new Label(translationProvider.getTranslation("page.is.under.development")));
        dialog.setOpened(true);
        add(dialog);
    }
}
