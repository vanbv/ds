package com.github.vanbv.ds.ui;

import com.github.vanbv.ds.app.security.SecurityUtils;
import com.github.vanbv.ds.ui.i18n.TranslationProvider;
import com.github.vanbv.ds.ui.views.task.TaskCallView;
import com.github.vanbv.ds.ui.views.task.TaskView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AbstractAppRouterLayout;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;

public class MainView extends AbstractAppRouterLayout {

    public MainView(TranslationProvider translationProvider) {
        getAppLayout().setBranding(new Span(translationProvider.getTranslation("delivery.service")));

        if (SecurityUtils.isUserLoggedIn()) {

            if (SecurityUtils.isAccessGranted(TaskView.class)) {
                setMenuItem(getAppLayoutMenu(), new AppLayoutMenuItem(VaadinIcon.PHONE_LANDLINE.create(),
                        translationProvider.getTranslation("tasks"), TaskView.ROUTE));
            }

            if (SecurityUtils.isAccessGranted(TaskCallView.class)) {
                setMenuItem(getAppLayoutMenu(), new AppLayoutMenuItem(VaadinIcon.PHONE_LANDLINE.create(),
                        translationProvider.getTranslation("tasks"), TaskCallView.ROUTE));
            }

            setMenuItem(getAppLayoutMenu(), new AppLayoutMenuItem(VaadinIcon.ARROW_RIGHT.create(),
                    translationProvider.getTranslation("logout"),
                    e -> UI.getCurrent().getPage().executeJavaScript("location.assign('logout')")));
        }
    }

    @Override
    protected void configure(AppLayout appLayout, AppLayoutMenu appLayoutMenu) {
    }

    private void setMenuItem(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
        menuItem.getElement().setAttribute("theme", "icon-on-top");
        menu.addMenuItem(menuItem);
    }
}
