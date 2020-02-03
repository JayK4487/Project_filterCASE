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
import de.xcase.filtercase2.backend.entities.EMailAdress;
import de.xcase.filtercase2.backend.respositories.EMailAdressesRespository;
import de.xcase.filtercase2.backend.respositories.KeywordRepository;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;
import de.xcase.filtercase2.components.Executor;
import de.xcase.filtercase2.components.RuntimeVariables;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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

    @Autowired
    private Executor executor;

    public StartsiteView(
            @Autowired RuntimeVariables runtimeVariables,
            @Autowired KeywordRepository keywordRepository,
            @Autowired EMailAdressesRespository eMailAdressesRespository

    ) {
        HorizontalLayout hl1 = new HorizontalLayout();

        Card cardpanel1 = new Card("Überblick");
        cardpanel1.getElement().getStyle().set("width", "50%");
        cardpanel1.addContent(buildWelcomeMessage(runtimeVariables));

        Card cardpanel2 = new Card("Anzahl aktuell verwendeter Suchbegriffe:");
        cardpanel2.getElement().getStyle().set("width", "50%");
        cardpanel2.addContent(buildSearches(keywordRepository));

        HorizontalLayout hl2 = new HorizontalLayout();

        Card cardpanel3 = new Card("Sie können die Ausführung manuell Starten:");
        cardpanel3.getElement().getStyle().set("width", "50%");
        cardpanel3.addContent(buildmanualStart(runtimeVariables));

        Card cardpanel4 = new Card("Aktuelle E-Mailadressen für Benachrichtigungen:");
        cardpanel4.getElement().getStyle().set("width", "50%");
        cardpanel4.addContent(buildNotifications(eMailAdressesRespository));

        hl1.add(cardpanel1, cardpanel2);
        hl1.setWidthFull();
        hl2.add(cardpanel3, cardpanel4);
        hl2.setWidthFull();

        add(hl1, hl2);

    }

    private Component buildWelcomeMessage(RuntimeVariables runtimeVariables) {

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Label message1 = new Label(runtimeVariables.getLastRun() == null ? "Seit Start hat noch kein Suchlauf stattgefunden." : DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(runtimeVariables.getLastRun()));
        Label message2 = new Label("Insgesamt abgerufene E-Mails: " + String.valueOf(runtimeVariables.getTotalMails()));
        Label message3 = new Label("Uneindeutige E-Mails: " + String.valueOf(runtimeVariables.getNoKeywordMatchingMails()));
        Label message4 = new Label("Weitergeleitete E-Mails: " + String.valueOf(runtimeVariables.getDistributedMails()));
        //Label message5 = new Label("Uneindeutige E-Mails: " + String.valueOf(runtimeVariables.getAmbiguousMails()));

        content.add(message1);
        content.add(message2);
        content.add(message3);
        content.add(message4);
        //content.add(message5);

        return content;

    }

    private Component buildSearches(KeywordRepository keywordRepository) {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Label message1 = new Label(String.valueOf(keywordRepository.count()));

        content.add(message1);
        return content;
    }

    private Component buildmanualStart(RuntimeVariables runtimeVariables) {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        Button button = new Button("Starten");
        button.addClickListener(click -> {
            executor.execute();
            getUI().ifPresent(ui -> ui.getPage().reload());

        });
        Label message1 = new Label(runtimeVariables.getLastRun() == null ? "Seit Start hat noch kein Suchlauf stattgefunden." : DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(runtimeVariables.getLastRun()));
        content.add(button);
        content.add(message1);
        return content;
    }

    private Component buildNotifications(EMailAdressesRespository eMailAdressesRespository) {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        for (EMailAdress emailAddress: eMailAdressesRespository.findAll()) {
            Label emailLabel = new Label(emailAddress.getEmailAddress());
            content.add(emailLabel);
        }

        return content;
    }

}
