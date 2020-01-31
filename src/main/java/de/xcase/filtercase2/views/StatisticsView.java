package de.xcase.filtercase2.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;
import de.xcase.filtercase2.components.RuntimeVariables;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = StatisticsView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "Statistics")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class StatisticsView extends BaseView {
    public static final String VIEW_NAME = "StatisticsView";

    public StatisticsView(@Autowired RuntimeVariables runtimeVariables) {

        HorizontalLayout hl = new HorizontalLayout();

        Card cardpanel = new Card("Detaillierter Überlick");
        cardpanel.getElement().getStyle().set("width", "100%");
        cardpanel.add(statistic(runtimeVariables));

        hl.add(cardpanel);
        hl.setSizeFull();
        add(hl);

    }

    public Component statistic(RuntimeVariables runtimeVariables) {

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.AUTO);

        Label message1 = new Label(runtimeVariables.getLastRun() == null ? "Seit Start hat noch kein Suchlauf stattgefunden." : runtimeVariables.getLastRun().toString());
        Label message2 = new Label("Insgesamt abgerufene E-Mails: " + String.valueOf(runtimeVariables.getTotalMails()));
        Label message3 = new Label("Uneindeutige E-Mails: " + String.valueOf(runtimeVariables.getDeletedMails()));
        Label message4 = new Label("Weitergeleitete E-Mails: " + String.valueOf(runtimeVariables.getDistributedMails()));
        //Label message5 = new Label("Uneindeutige E-Mails: " + String.valueOf(runtimeVariables.getAmbiguousMails()));

        content.add(message1);
        content.add(message2);
        content.add(message3);
        content.add(message4);
        //content.add(message5);

        return content;
    }
}
