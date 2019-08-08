package com.github.vanbv.ds.ui.views.login;

import com.github.vanbv.ds.app.security.SecurityUtils;
import com.github.vanbv.ds.ui.i18n.TranslationPageTitle;
import com.github.vanbv.ds.ui.i18n.TranslationProvider;
import com.github.vanbv.ds.ui.views.error.DevelopmentView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;


@Route(LoginView.ROUTE)
@TranslationPageTitle("login")
public class LoginView extends VerticalLayout implements AfterNavigationObserver, BeforeEnterObserver {

    public static final String ROUTE = "login";
    public static final String ERROR_PARAM = "error";

    private LoginOverlay login = new LoginOverlay();

    public LoginView(TranslationProvider translationProvider) {
        LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle(translationProvider.getTranslation("delivery.service"));
        i18n.getHeader().setDescription(translationProvider
                .getTranslation("your.assistant.in.order.delivery.management"));

        i18n.setForm(new LoginI18n.Form());
        i18n.getForm().setTitle(translationProvider.getTranslation("login"));
        i18n.getForm().setUsername(translationProvider.getTranslation("email"));
        i18n.getForm().setPassword(translationProvider.getTranslation("password"));
        i18n.getForm().setSubmit(translationProvider.getTranslation("log.in"));

        i18n.setErrorMessage(new LoginI18n.ErrorMessage());
        i18n.getErrorMessage().setTitle(translationProvider.getTranslation("failed.to.log.in"));
        i18n.getErrorMessage().setMessage(
                translationProvider.getTranslation("please.make.sure.that.you.have.entered.your.data.correctly"));

        login.setI18n(i18n);
        login.setAction(ROUTE);
        login.setOpened(true);
        getElement().appendChild(login.getElement());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if(event.getLocation().getQueryParameters().getParameters().containsKey(ERROR_PARAM)) {
            login.setError(true);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (SecurityUtils.isUserLoggedIn()) {
            UI.getCurrent().getPage().getHistory().replaceState(null, "");
            event.rerouteTo(DevelopmentView.class);
        }
    }
}
