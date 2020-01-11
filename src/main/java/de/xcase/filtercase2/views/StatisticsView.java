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

@Route(value = StatisticsView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "Statistics")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class StatisticsView extends BaseView {
    public static final String VIEW_NAME = "StatisticsView";

    public StatisticsView() {

        HorizontalLayout hl = new HorizontalLayout();

        Card cardpanel = new Card("Detaillierter Überlick");
        cardpanel.getElement().getStyle().set("width", "100%");
        cardpanel.add(statistic());

        hl.add(cardpanel);
        hl.setSizeFull();
        add(hl);

    }

    public Component statistic() {

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.AUTO);

        Label message1 = new Label("Zeitpunkt letzter Abruf: 15.11.2019");
        Label message2 = new Label("Ingesamt abgerufen: 98");
        Label message3 = new Label("Gelöscht: 54");
        Label message4 = new Label("Verteilt: 42");
        Label message5 = new Label("Uneindeutig: 2");

        content.add(message1);
        content.add(message2);
        content.add(message3);
        content.add(message4);
        content.add(message5);

        return content;
    }
}
