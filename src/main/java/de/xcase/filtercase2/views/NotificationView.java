package de.xcase.filtercase2.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.backend.entities.EMailAdresse;
import de.xcase.filtercase2.backend.enums.Department;
import de.xcase.filtercase2.backend.respositories.EMailAdressesRespository;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = NotificationView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "Benachrichtigunen")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class NotificationView extends BaseView {

    public static final String VIEW_NAME = "NotificationView";

    private final HorizontalLayout hlMain = new HorizontalLayout();
    private final Card cPanel = new Card("Empfänger der Benachrichtigungen");
    private final VerticalLayout vlCardMain = new VerticalLayout();

    private final Grid<EMailAdresse> grNotifications = new Grid<>();
    private final Button btAdd = new Button("+");

    private List<EMailAdresse> eMailAdresseList = new ArrayList<>();

    public NotificationView(@Autowired EMailAdressesRespository eMailAdressesRespository) {
        cPanel.getElement().getStyle().set("width", "100%");

        eMailAdresseList.addAll(eMailAdressesRespository.findAll());

        grNotifications.addColumn(EMailAdresse::getUser).setHeader("User");
        grNotifications.addColumn(EMailAdresse::getEmailAddress).setHeader("E-Mailadresse");
        grNotifications.addColumn(eMailAdresse -> Department.valueOf(eMailAdresse.getDepartment()).getName()).setHeader("Abteilung");
        grNotifications.addComponentColumn(keyword -> new Button("Bearbeiten"));
        grNotifications.addComponentColumn(keyword -> new Button(VaadinIcon.TRASH.create()));
        grNotifications.setItems(eMailAdresseList);
        grNotifications.getDataProvider().refreshAll();

        vlCardMain.getElement().getStyle().set("length", "100%");
        vlCardMain.add(btAdd, grNotifications);

        cPanel.getElement().getStyle().set("width", "100%");
        cPanel.add(vlCardMain);

        hlMain.add(cPanel);
        hlMain.setSizeFull();
        add(hlMain);
    }
}
