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

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Route(value = ExecutionTimesView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class ExecutionTimesView extends BaseView {

    public static final String VIEW_NAME = "ExecutionTimesView";

    private final HorizontalLayout hlMain = new HorizontalLayout();
    private final VerticalLayout vlCardMain = new VerticalLayout();
    private final Card cPanel = new Card("Festlegen der Ausführungszeiten");

    private Optional<CronJob> job1;
    private Optional<CronJob> job2;

    private final TimePicker tpFistExecution = new TimePicker("1. Neuen Ausführungszeitpunkt festlegen:");
    private final TimePicker tpSecondExecution = new TimePicker("2. Neuen Ausführungszeitpunkt festlegen:");

    private Label lbFirst = new Label("1. Ausführungszeitpunkt: Noch nicht gesetzt!");
    private Label lbSecond = new Label("2. Ausführungszeitpunkt: Noch nicht gesetzt!");

    public ExecutionTimesView(@Autowired CronJobRepository cronJobRepository) {
        cPanel.getElement().getStyle().set("width", "100%");

        job1 = cronJobRepository.findById(1L);

        job2 = cronJobRepository.findById(2L);

        job1.ifPresent(cronJob -> {
            tpFistExecution.setValue(cronJob.getExecution());
            lbFirst.setText("1. Ausführungszeitpunkt: " + cronJob.getExecution());
        });
        job2.ifPresent(cronJob -> {
            tpSecondExecution.setValue(cronJob.getExecution());
            lbSecond.setText("2. Ausführungszeitpunkt: " + cronJob.getExecution());
        });

        tpFistExecution.addValueChangeListener(valueChangeEvent -> {
            lbFirst.setText("1. Ausführungszeitpunkt: " + valueChangeEvent.getValue());
            if (job1.isEmpty()) {
                job1 = Optional.of(new CronJob());
            }
            job1.get().setExecution(valueChangeEvent.getValue());
            cronJobRepository.save(job1.get());
        });

        tpSecondExecution.addValueChangeListener(valueChangeEvent -> {
            lbSecond.setText("2. Ausführungszeitpunkt: " + valueChangeEvent.getValue());
            if (job2.isEmpty()) {
                job2 = Optional.of(new CronJob());
            }
            job2.get().setExecution(valueChangeEvent.getValue());
            cronJobRepository.save(job2.get());
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
}
