package de.xcase.filtercase2.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.xcase.filtercase2.backend.entities.EMailAdress;
import de.xcase.filtercase2.backend.respositories.EMailAdressesRespository;
import de.xcase.filtercase2.components.AddMailDialog;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

@Route(value = NotificationView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "Benachrichtigunen")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class NotificationView extends BaseView {

    public static final String VIEW_NAME = "NotificationView";

    private final HorizontalLayout hlMain = new HorizontalLayout();
    private final Card cPanel = new Card("Empfänger der Benachrichtigungen");
    private final VerticalLayout vlCardMain = new VerticalLayout();

    private final Grid<EMailAdress> grNotifications = new Grid<>();
    private final Button btAdd = new Button("+");

    // For Editor
    private final Binder<EMailAdress> binder = new Binder<>(EMailAdress.class);
    @PropertyId("emailAddress")
    private final TextField editEmailField = new TextField();
    @PropertyId("user")
    private final TextField editUserField = new TextField();
    Button btEditorSave = new Button("Speichern");
    Button btEditorCancel = new Button("Abbrechen");
    Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());

    public NotificationView(@Autowired EMailAdressesRespository eMailAdressesRespository, @Autowired AddMailDialog mailDialog) {
        binder.forMemberField(editEmailField);
        binder.forMemberField(editUserField);
        binder.bindInstanceFields(this);

        btEditorSave.addClickListener(click -> grNotifications.getEditor().save());
        btEditorCancel.addClickListener(click -> grNotifications.getEditor().cancel());

        Div editorButtons = new Div(btEditorSave, btEditorCancel);

        mailDialog.addOpenedChangeListener(changeEvent -> {
           if(!changeEvent.isOpened()) {
               //Refresh on close
               grNotifications.setItems(eMailAdressesRespository.findAll());
           }
        });

        btAdd.addClickListener(click -> {
            mailDialog.open();
        });

        cPanel.getElement().getStyle().set("width", "100%");

        grNotifications.addColumn(eMailAdress -> eMailAdress.getUser() == null ? "Keine Name hinterlegt" : eMailAdress.getUser())
                .setEditorComponent(editUserField)
                .setHeader("User");
        grNotifications.addColumn(eMailAdress -> eMailAdress.getEmailAddress() == null ? "Keine Adresse hinterlegt" : eMailAdress.getEmailAddress())
                .setEditorComponent(editEmailField)
                .setHeader("EMail");
        grNotifications.addComponentColumn(eMailAdress -> {
            Button button = new Button("Bearbeiten");
            button.addClickListener(click -> {
                grNotifications.getEditor().editItem(eMailAdress);
            });
            button.setEnabled(!grNotifications.getEditor().isOpen());
            editButtons.add(button);
            return button;
        }).setEditorComponent(editorButtons);
        grNotifications.addComponentColumn(eMailAdress -> {
            Button button = new Button(VaadinIcon.TRASH.create());
            button.addClickListener(click -> {
                eMailAdressesRespository.delete(eMailAdress);
                grNotifications.setItems(eMailAdressesRespository.findAll());
            });
            return button;
        });
        grNotifications.setItems(eMailAdressesRespository.findAll());
        grNotifications.getDataProvider().refreshAll();

        grNotifications.getEditor().setBuffered(true);
        grNotifications.getEditor().setBinder(binder);
        grNotifications.getEditor().addOpenListener(openEvent -> editButtons.forEach(button -> button.setEnabled(!grNotifications.getEditor().isOpen())));
        grNotifications.getEditor().addCloseListener(closeEvent -> editButtons.forEach(button -> button.setEnabled(!grNotifications.getEditor().isOpen())));
        grNotifications.getEditor().addSaveListener(saveEvent -> eMailAdressesRespository.save(saveEvent.getItem()));

        vlCardMain.getElement().getStyle().set("length", "100%");
        vlCardMain.add(btAdd, grNotifications);

        cPanel.getElement().getStyle().set("width", "100%");
        cPanel.add(vlCardMain);

        hlMain.add(cPanel);
        hlMain.setSizeFull();
        add(hlMain);
    }
}
