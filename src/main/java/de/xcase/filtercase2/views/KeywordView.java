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
import de.xcase.filtercase2.backend.entities.Keyword;
import de.xcase.filtercase2.backend.respositories.KeywordRepository;
import de.xcase.filtercase2.components.AddKeywordDialog;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Route(value = KeywordView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "Suchbegriffe")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class KeywordView extends BaseView {

    public static final String VIEW_NAME = "KeywordView";

    private final HorizontalLayout hlMain = new HorizontalLayout();
    private final VerticalLayout vlSite = new VerticalLayout();
    private final Card cPanel = new Card("Bearbeiten der Schlüsselbegriffe");

    private final Button btAdd = new Button("+");
    private final Grid<Keyword> grKeywords = new Grid<>();

    // For Editor
    private final Binder<Keyword> binder = new Binder<>(Keyword.class);
    @PropertyId("keyword")
    private final TextField editKeywordField = new TextField();
    @PropertyId("userKeyword")
    private final TextField editUserField = new TextField();
    Button btEditorSave = new Button("Speichern");
    Button btEditorCancel = new Button("Abbrechen");
    Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());

    public KeywordView(@Autowired KeywordRepository keywordRepository, @Autowired AddKeywordDialog keywordDialog) {
        binder.forMemberField(editKeywordField);
        binder.forMemberField(editUserField);
        binder.bindInstanceFields(this);

        btEditorSave.addClickListener(click -> grKeywords.getEditor().save());
        btEditorCancel.addClickListener(click -> grKeywords.getEditor().cancel());

        Div editorButtons = new Div(btEditorSave, btEditorCancel);

        keywordDialog.addOpenedChangeListener(changeEvent -> {
            if(!changeEvent.isOpened()) {
                //Refresh on close
                grKeywords.setItems(keywordRepository.findAll());
            }
        });

        btAdd.addClickListener(click -> {
            keywordDialog.open();
        });

        cPanel.getElement().getStyle().set("width", "100%");

        grKeywords.addColumn(keyword -> keyword.getKeyword() == null ? "Kein Schlüsselbegriff hinterlegt" : keyword.getKeyword())
                .setEditorComponent(editKeywordField)
                .setHeader("Schlüsselbegriff");
        grKeywords.addColumn(keyword -> keyword.getUserKeyword() == null ? "Kein Name hinterlegt" : keyword.getUserKeyword())
                .setEditorComponent(editUserField)
                .setHeader("User");
        grKeywords.addComponentColumn(keyword -> {
            Button button = new Button("Bearbeiten");
            button.addClickListener(click -> {
                grKeywords.getEditor().editItem(keyword);
            });
            button.setEnabled(!grKeywords.getEditor().isOpen());
            editButtons.add(button);
            return button;
        }).setEditorComponent(editorButtons);
        grKeywords.addComponentColumn(keyword -> {
            Button button = new Button(VaadinIcon.TRASH.create());
            button.addClickListener(click -> {
                keywordRepository.delete(keyword);
                grKeywords.setItems(keywordRepository.findAll());
            });
            return button;
        });
        grKeywords.setItems(keywordRepository.findAll());
        grKeywords.getDataProvider().refreshAll();

        grKeywords.getEditor().setBuffered(true);
        grKeywords.getEditor().setBinder(binder);
        grKeywords.getEditor().addOpenListener(openEvent -> editButtons.forEach(button -> button.setEnabled(!grKeywords.getEditor().isOpen())));
        grKeywords.getEditor().addCloseListener(closeEvent -> editButtons.forEach(button -> button.setEnabled(!grKeywords.getEditor().isOpen())));
        grKeywords.getEditor().addSaveListener(saveEvent -> keywordRepository.save(saveEvent.getItem()));

        vlSite.getElement().getStyle().set("length", "100%");
        vlSite.add(btAdd, grKeywords);

        cPanel.getElement().getStyle().set("width", "100%");
        cPanel.add(vlSite);

        hlMain.add(cPanel);
        hlMain.setSizeFull();
        add(hlMain);

    }
}
