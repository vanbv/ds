package com.github.vanbv.ds.ui.views.task;

import com.github.vanbv.ds.app.security.SecurityUtils;
import com.github.vanbv.ds.backend.domain.TaskCallInfo;
import com.github.vanbv.ds.backend.service.TaskCallService;
import com.github.vanbv.ds.ui.MainView;
import com.github.vanbv.ds.ui.i18n.TranslationPageTitle;
import com.github.vanbv.ds.ui.i18n.TranslationProvider;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.Map;

@Secured("OPERATOR")
@Route(value = TaskCallView.ROUTE, layout = MainView.class)
@TranslationPageTitle("tasks")
public class TaskCallView extends VerticalLayout {

    public static final String ROUTE = "tasks/call";
    private static final int DEFAULT_LIMIT = 50;
    private static final String[] COLUMNS = {"id", "dateCreate", "orderNumber", "state", "description", "clientEmail",
            "clientMobile"};
    private static final Map<String, String> COL_NAME_MAP = Map.of("dateCreate", "date.create",
            "orderNumber", "order.number", "clientEmail", "client.email", "clientMobile",
            "client.mobile");

    public TaskCallView(TranslationProvider translationProvider, TaskCallService taskCallService) {
        Grid<TaskCallInfo> grid = createGrid(translationProvider, taskCallService);
        add(grid);
    }
    private Grid<TaskCallInfo> createGrid(TranslationProvider translationProvider, TaskCallService taskCallService) {
        Grid<TaskCallInfo> grid = new Grid<>(TaskCallInfo.class);
        DataProvider<TaskCallInfo, String> dataProvider = DataProvider.fromFilteringCallbacks(
                query -> taskCallService.findAll(query.getLimit(),
                        query.getOffset(), query.getFilter().orElse("")).stream(),
                query -> taskCallService.count(query.getFilter().orElse("")));
        grid.setDataProvider(dataProvider);
        grid.setColumns(COLUMNS);
        grid.getColumns().forEach(col -> {
            col.setHeader(
                    translationProvider.getTranslation(COL_NAME_MAP.getOrDefault(col.getKey(), col.getKey())));
            col.setSortable(false);
        });

        HeaderRow filterRow = grid.appendHeaderRow();
        TextField orderNumberField = new TextField();
        orderNumberField.addValueChangeListener(event -> {
            dataProvider.withConfigurableFilter().setFilter(event.getValue());
            grid.setItems(taskCallService.findAll(DEFAULT_LIMIT, 0, event.getValue()));
        });
        orderNumberField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(grid.getColumnByKey("orderNumber")).setComponent(orderNumberField);
        orderNumberField.setSizeFull();

        grid.addColumn(new ComponentRenderer<>(taskCallInfo -> {

            if (TaskCallInfo.State.NEW.equals(taskCallInfo.getState())) {
                Button process = new Button(new Icon(VaadinIcon.CHECK), event -> {
                    taskCallInfo.setState(TaskCallInfo.State.PROCESSED);
                    updateTaskCall(taskCallInfo, taskCallService, translationProvider, grid);
                });

                process.getElement().setAttribute("title", translationProvider.getTranslation("process"));

                Button reject = new Button(new Icon(VaadinIcon.CLOSE_SMALL), event -> {
                    taskCallInfo.setState(TaskCallInfo.State.CANCELED);
                    updateTaskCall(taskCallInfo, taskCallService, translationProvider, grid);
                });

                reject.getElement().setAttribute("title", translationProvider.getTranslation("reject"));

                HorizontalLayout buttons = new HorizontalLayout(process, reject);
                return new VerticalLayout(buttons);
            }

            return new VerticalLayout();
        }));

        return grid;
    }

    private void updateTaskCall(TaskCallInfo taskCallInfo, TaskCallService taskCallService,
                                TranslationProvider translationProvider, Grid<TaskCallInfo> grid) {
        grid.getDataProvider().refreshItem(taskCallInfo);

        if (!taskCallService.update(SecurityUtils.getUser(), taskCallInfo)) {
            new Notification(translationProvider.getTranslation("error.has.occurred.please.contact.support"),
                    3000).open();
        }
    }
}
