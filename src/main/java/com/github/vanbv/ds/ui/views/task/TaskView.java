package com.github.vanbv.ds.ui.views.task;

import com.github.vanbv.ds.app.security.SecurityUtils;
import com.github.vanbv.ds.backend.domain.TaskCall;
import com.github.vanbv.ds.backend.service.OrderService;
import com.github.vanbv.ds.backend.service.TaskCallService;
import com.github.vanbv.ds.ui.MainView;
import com.github.vanbv.ds.ui.i18n.TranslationPageTitle;
import com.github.vanbv.ds.ui.i18n.TranslationProvider;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudI18n;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.Map;

@Secured("COURIER")
@Route(value = TaskView.ROUTE, layout = MainView.class)
@TranslationPageTitle("tasks")
public class TaskView extends VerticalLayout {

    public static final String ROUTE = "tasks";
    private static final int ORDINAL_NUMBER_MAX_LENGTH = 10;
    private static final int DESCRIPTION_MAX_LENGTH = 200;
    private static final String[] COLUMNS = {"id", "orderNumber", "state", "description"};
    private static final Map<String, String> COL_NAME_MAP = Map.of("orderNumber", "order.number");

    public TaskView(TranslationProvider translationProvider, TaskCallService taskCallService,
                    OrderService orderService) {
        Crud<TaskCall> crud = new Crud<>(TaskCall.class, createGrid(translationProvider, taskCallService),
                createEditor(translationProvider, orderService));
        crud.setI18n(createCrudI18n(translationProvider));
        crud.addSaveListener(e -> {
            if (!taskCallService.create(SecurityUtils.getUser(), e.getItem())) {
                new Notification(translationProvider.getTranslation("error.has.occurred.please.contact.support"),
                        3000).open();
            }
        });
        add(crud);
    }
    private Grid<TaskCall> createGrid(TranslationProvider translationProvider, TaskCallService taskCallService) {
        Grid<TaskCall> grid = new Grid<>(TaskCall.class);
        grid.setDataProvider(DataProvider.fromCallbacks(
                query -> taskCallService.findByUser(SecurityUtils.getUser(), query.getLimit(),
                        query.getOffset()).stream(),
                query -> taskCallService.count(SecurityUtils.getUser())));
        grid.setColumns(COLUMNS);
        grid.getColumns().forEach(col -> {
            col.setHeader(
                    translationProvider.getTranslation(COL_NAME_MAP.getOrDefault(col.getKey(), col.getKey())));
            col.setSortable(false);
        });

        return grid;
    }

    private CrudEditor<TaskCall> createEditor(TranslationProvider translationProvider, OrderService orderService) {
        TextField orderNumberField = new TextField(translationProvider.getTranslation("order.number"));
        orderNumberField.setRequired(true);
        orderNumberField.setMaxLength(ORDINAL_NUMBER_MAX_LENGTH);
        TextField descriptionField = new TextField(translationProvider.getTranslation("description"));
        descriptionField.setRequired(true);
        descriptionField.setMaxLength(DESCRIPTION_MAX_LENGTH);
        FormLayout form = new FormLayout(orderNumberField, descriptionField);

        Binder<TaskCall> binder = new Binder<>(TaskCall.class);
        binder.forField(orderNumberField).withValidator(
                orderNumber -> orderNumber != null
                        && !orderNumber.isEmpty()
                        && orderNumber.length() <= ORDINAL_NUMBER_MAX_LENGTH,
                translationProvider.getTranslation("incorrect.value"))
                .withValidator(orderNumber -> !orderService.isExist(orderNumber),
                        translationProvider.getTranslation("order.with.this.number.does.not.exist"))
                .bind(TaskCall::getOrderNumber, TaskCall::setOrderNumber);

        binder.forField(descriptionField).withValidator(
                description -> description!= null
                        && !description.isEmpty()
                        && description.length() <= DESCRIPTION_MAX_LENGTH,
                translationProvider.getTranslation("incorrect.value"))
                .bind(TaskCall::getDescription, TaskCall::setDescription);

        return new BinderCrudEditor<>(binder, form);
    }

    private CrudI18n createCrudI18n(TranslationProvider translationProvider) {
        CrudI18n crudI18n = CrudI18n.createDefault();
        crudI18n.setNewItem(translationProvider.getTranslation("not.time"));
        crudI18n.setSaveItem(translationProvider.getTranslation("save"));
        crudI18n.setCancel(translationProvider.getTranslation("cancel"));
        return crudI18n;
    }
}
