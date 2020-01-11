package de.xcase.filtercase2.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;

/**
 * Home site of the app.
 */
@Route(value = StartsiteView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "Startseite")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
@CssImport("styles/menu-layout-styles.css")
public class StartsiteView extends BaseView {

    public static final String VIEW_NAME = "StartsiteView";


    public StartsiteView() {

        HorizontalLayout hl1 = new HorizontalLayout();

        Card cardpanel1 = new Card("Überblick");
        cardpanel1.getElement().getStyle().set("width", "50%");
        cardpanel1.addContent(buildWelcomeMessage());

        Card cardpanel2 = new Card("Anzahl aktuell verwendeter Suchbegriffe:");
        cardpanel2.getElement().getStyle().set("width", "50%");
        cardpanel2.addContent(buildSearches());

        HorizontalLayout hl2 = new HorizontalLayout();

        Card cardpanel3 = new Card("Sie können die Ausführung manuell Starten:");
        cardpanel3.getElement().getStyle().set("width", "50%");
        cardpanel3.addContent(buildmanualStart());

        Card cardpanel4 = new Card("Aktuelle E-Mailadressen für Benachrichtigungen:");
        cardpanel4.getElement().getStyle().set("width", "50%");
        cardpanel4.addContent(buildNotifications());
        //.addAction(new Button("Mach was!"));

        hl1.add(cardpanel1, cardpanel2);
        hl1.setWidthFull();
        hl2.add(cardpanel3, cardpanel4);
        hl2.setWidthFull();

        add(hl1, hl2);

    }

    private Component buildWelcomeMessage() {

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Label message1 = new Label("Zeitpunkt letzter Abruf: 15.11.2019 15:12 Uhr");
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

    private Component buildSearches() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Label message1 = new Label("Anzahl: 21");

        content.add(message1);
        return content;
    }

    private Component buildmanualStart() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Button button = new Button("Starten");
        Label message1 = new Label("Letzte Ausführung: 15.11.2019 15:12 Uhr");

        content.add(button);
        content.add(message1);

        return content;
    }

    private Component buildNotifications() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Label message1 = new Label("lutz.schmidt@x-case.de");
        Label message2 = new Label("christopher.pohlmann@x-case.de");
        Label message3 = new Label("sebastian.buesch@x-case.de");
        Label message4 = new Label("markus.ehrhardt@x-case.de");

        content.add(message1);
        content.add(message2);
        content.add(message3);
        content.add(message4);

        return content;
    }

}
