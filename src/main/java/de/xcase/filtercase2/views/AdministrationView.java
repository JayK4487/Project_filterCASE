package de.xcase.filtercase2.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.backend.entities.EMailAdresses;
import de.xcase.filtercase2.backend.entities.Keyword;
import de.xcase.filtercase2.backend.entities.UsedFolders;
import de.xcase.filtercase2.backend.respositories.KeywordRepository;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = AdministrationView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "Administration")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
@CssImport("styles/menu-layout-styles.css")
public class AdministrationView extends BaseView {
    public static final String VIEW_NAME = "administration";

    public AdministrationView(@Autowired KeywordRepository repository) {

        HorizontalLayout hl1 = new HorizontalLayout();

        Card cardpanel1 = new Card("Ausführungszeiten:");
        cardpanel1.getElement().getStyle().set("width", "50%");
        cardpanel1.add(changeExecutionTime());

        Card cardpanel2 = new Card("Schlüsselbegriffe:");
        cardpanel2.getElement().getStyle().set("width", "50%");
        cardpanel2.add(changeKeywords());

        HorizontalLayout hl2 = new HorizontalLayout();

        Card cardpanel3 = new Card("Aktuelle E-Mailadressen für Benachrichtigungen:");
        cardpanel3.getElement().getStyle().set("width", "50%");
        cardpanel3.add(notificationAddresses());

        Card cardpanel4 = new Card("Aktuelle Quell- und Zielordner:");
        cardpanel4.getElement().getStyle().set("width", "50%");
        cardpanel4.add(usedFolders());

        hl1.add(cardpanel1, cardpanel2);
        hl1.setWidthFull();
        hl2.add(cardpanel3, cardpanel4);
        hl2.setWidthFull();

        add(hl1, hl2);

       //TODO: DELETE repository.findByUserKeyword("jsauer");
    }

    private Component changeExecutionTime() {

        VerticalLayout content = new VerticalLayout();
        content.getElement().getStyle().set("length", "50%");

        Label message1 = new Label("1. Ausführung:");
        Label message2 = new Label("2. Ausführung:");
        Label message3 = new Label(" ");
        Label message4 = new Label("1. Neuen Ausführungszeitpunkt festlegen:");
        Label message5 = new Label("2. Neuen Ausführungszeitpunkt festlegen:");

        content.add(message1);
        content.add(message2);
        content.add(message3);
        content.add(message4);
        content.add(datePicker());
        content.add(timePicker());
        content.add(message5);
        content.add(datePicker());
        content.add(timePicker());

        return content;
    }

    private Component changeKeywords() {

        VerticalLayout content = new VerticalLayout();
        content.getElement().getStyle().set("length", "50%");

        Grid<Keyword> gridKeyword = new Grid();
        gridKeyword.addColumn(Keyword::getKeyword).setHeader("Schlüsselbegriff");
        gridKeyword.addColumn(Keyword::getUserKeyword).setHeader("gesetzt durch");
        gridKeyword.addColumn(Keyword::getStatus).setHeader("Status");

        content.add(gridKeyword);

        FlexLayout fl = new FlexLayout();
        return content;
    }

    private Component notificationAddresses() {

        VerticalLayout content = new VerticalLayout();
        content.getElement().getStyle().set("length", "50%");

        Grid<EMailAdresses> gridNotifications = new Grid();
        gridNotifications.addColumn(EMailAdresses::getUser).setHeader("User");
        gridNotifications.addColumn(EMailAdresses::getEmailAddress).setHeader("E-Mailadresse");
        gridNotifications.addColumn(EMailAdresses::getDepartment).setHeader("Abteilung");

        content.add(gridNotifications);

        FlexLayout fl = new FlexLayout();
        return content;
    }

    private Component usedFolders() {

        VerticalLayout content = new VerticalLayout();
        content.getElement().getStyle().set("length", "50%");

        Grid<UsedFolders> gridFolders = new Grid();
        gridFolders.addColumn(UsedFolders::getSourceFolder).setHeader("Quellordner");
        gridFolders.addColumn(UsedFolders::getSourceFolder).setHeader("Zielordner");
       // gridFolders.addColumn(UsedFolder::getUserFolder).setHeader("Abteilung");

        content.add(gridFolders);

        FlexLayout fl = new FlexLayout();
        return content;
    }

    private Component datePicker() {

        DatePicker datePicker = new DatePicker();
        datePicker.addValueChangeListener(
                event -> Notification.show(event.getValue().toString()));

        FlexLayout fdatePicker = new FlexLayout();

        return datePicker;
    }

    private Component timePicker() {

        TimePicker timePicker = new TimePicker();
        timePicker.addValueChangeListener(
                event -> Notification.show(event.getValue().toString()));

        FlexLayout ftimePicker = new FlexLayout();
        return timePicker;
    }
}
