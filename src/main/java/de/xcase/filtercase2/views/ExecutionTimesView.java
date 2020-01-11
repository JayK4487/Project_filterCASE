package de.xcase.filtercase2.views;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.backend.entities.CronJob;
import de.xcase.filtercase2.backend.respositories.CronJobRepository;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;

@Route(value = ExecutionTimesView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class ExecutionTimesView extends BaseView {

    public static final String VIEW_NAME = "ExecutionTimesView";

    private final HorizontalLayout hlMain = new HorizontalLayout();
    private final VerticalLayout vlCardMain = new VerticalLayout();
    private final Card cPanel = new Card("Festlegen der Ausführungszeiten");

    private final TimePicker tpFistExecution = new TimePicker("1. Neuen Ausführungszeitpunkt festlegen:");
    private final TimePicker tpSecondExecution = new TimePicker("2. Neuen Ausführungszeitpunkt festlegen:");

    private Label lbFirst = new Label("1. Ausführungszeitpunkt: Noch nicht gesetzt!");
    private Label lbSecond = new Label("2. Ausführungszeitpunkt: Noch nicht gesetzt!");

    private CronJobRepository cronJobRepository;

    public ExecutionTimesView(@Autowired CronJobRepository cronJobRepository) {
        this.cronJobRepository = cronJobRepository;
        cPanel.getElement().getStyle().set("width", "100%");

        List<CronJob> cronJobs = cronJobRepository.findAll();
        boolean first = true;
        for (CronJob cronJob : cronJobs) {
            if (first) {
                lbFirst.setText("1. Ausführungszeitpunkt: " + convertLocalTimeToString(cronJob.getExecution()));
                first = false;
            } else {
                lbSecond.setText("2. Ausführungszeitpunkt: " + convertLocalTimeToString(cronJob.getExecution()));
            }
        }

        tpFistExecution.addValueChangeListener(valueChangeEvent -> {
            lbFirst.setText("1. Ausführungszeitpunkt: "
                    + convertLocalTimeToString(createCronJob(1L, valueChangeEvent.getValue()).getExecution()));
        });

        tpSecondExecution.addValueChangeListener(valueChangeEvent -> {
            lbSecond.setText("2. Ausführungszeitpunkt: "
                    + convertLocalTimeToString(createCronJob(2L, valueChangeEvent.getValue()).getExecution()));
        });

        vlCardMain.add(tpFistExecution);
        vlCardMain.add(lbFirst);
        vlCardMain.add(tpSecondExecution);
        vlCardMain.add(lbSecond);

        cPanel.add(vlCardMain);
        hlMain.add(cPanel);
        hlMain.setSizeFull();
        add(hlMain);

    }


    private String convertLocalTimeToString(final LocalTime localTime) {
        return localTime.getHour() + ":" + localTime.getMinute();
    }

    private CronJob createCronJob(final Long id, final LocalTime localTime) {
        CronJob cronJob = new CronJob();
        cronJob.setId(id);
        cronJob.setExecution(localTime);

        if (cronJobRepository.existsById(id)) {
            cronJobRepository.deleteById(id);
        }
        cronJobRepository.save(cronJob);
        return cronJob;
    }
}
