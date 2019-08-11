package com.github.vanbv.ds.ui.views.error;

import com.github.vanbv.ds.ui.i18n.TranslationProvider;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

@Route(RouteNotFoundError.ROUTE)
public class RouteNotFoundError extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    public static final String ROUTE = "404";

    public RouteNotFoundError(TranslationProvider translationProvider) {
        Dialog dialog = new Dialog();
        dialog.add(new Label(translationProvider.getTranslation("page.not.found")));
        dialog.setOpened(true);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        add(dialog);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
