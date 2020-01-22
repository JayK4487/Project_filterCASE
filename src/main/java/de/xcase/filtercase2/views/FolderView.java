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
import de.xcase.filtercase2.backend.entities.Folder;
import de.xcase.filtercase2.backend.respositories.LDAPRepository;
import de.xcase.filtercase2.backend.respositories.FolderRepository;
import de.xcase.filtercase2.components.AddFolderDialog;
import de.xcase.filtercase2.components.AppRouterLayout;
import de.xcase.filtercase2.components.Card;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

@Route(value = FolderView.VIEW_NAME, layout = AppRouterLayout.class)
@PageTitle(value = "Ordnerstruktur")
@NpmPackage(value = "@polymer/paper-card", version = "3.0.1")
@JsModule("@polymer/paper-card/paper-card.js")
public class FolderView extends BaseView {

    public static final String VIEW_NAME = "FolderView";

    private final HorizontalLayout hlMain = new HorizontalLayout();
    private final Card cPanel = new Card("Bearbeitung der Quell- und Zielordner");
    private final VerticalLayout vlCardMain = new VerticalLayout();

    private final Grid<Folder> grUsedFolder = new Grid<>();
    private final Button btAdd = new Button("+");

    // For Editor
    private final Binder<Folder> binder = new Binder<>(Folder.class);
    @PropertyId("sourceFolder")
    private final TextField SourceFolderField = new TextField();
    @PropertyId("destinationFolder")
    private final TextField editDestinationFolderField = new TextField();
    @PropertyId("user")
    private final TextField editUserField = new TextField();
    Button btEditorSave = new Button("Speichern");
    Button btEditorCancel = new Button("Abbrechen");
    Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());

    public FolderView(@Autowired FolderRepository usedFolderRepository, @Autowired AddFolderDialog folderDialog, @Autowired LDAPRepository ldapRepository) {
        //binder.forMemberField(SourceFolderField);
        binder.forMemberField(editDestinationFolderField);
        binder.forMemberField(editUserField);
        binder.bindInstanceFields(this);

        btEditorSave.addClickListener(click -> grUsedFolder.getEditor().save());
        btEditorCancel.addClickListener(click -> grUsedFolder.getEditor().cancel());

        Div editorButtons = new Div(btEditorSave, btEditorCancel);

        folderDialog.addOpenedChangeListener(changeEvent -> {
           if (!changeEvent.isOpened()) {
               //Refresh on close
               grUsedFolder.setItems(usedFolderRepository.findAll());
           }
        });

        btAdd.addClickListener(click -> {
            folderDialog.open();
        });

        cPanel.getElement().getStyle().set("width", "100%");

        grUsedFolder.addColumn(usedFolder -> usedFolder.getDestinationFolder() == null ? "Kein Zielordner hinterlegt" : usedFolder.getDestinationFolder())
                .setEditorComponent(editDestinationFolderField)
                .setHeader("Zielordner");
        grUsedFolder.addColumn(usedFolder -> usedFolder.getUser() == null ? "Kein Name hinterlegt" : usedFolder.getUser())
                .setEditorComponent(editUserField)
                .setHeader("User");
        grUsedFolder.addComponentColumn(usedFolder -> {
           Button button = new Button("Bearbeiten");
           button.addClickListener(click -> {
               grUsedFolder.getEditor().editItem(usedFolder);
           });
           button.setEnabled(!grUsedFolder.getEditor().isOpen());
           editButtons.add(button);
           return button;
        }).setEditorComponent(editorButtons);
           grUsedFolder.addComponentColumn(usedFolder -> {
               Button button = new Button (VaadinIcon.TRASH.create());
               button.addClickListener(click -> {
                  usedFolderRepository.delete(usedFolder);
                  grUsedFolder.setItems(usedFolderRepository.findAll());
               });
               return button;
        });
        grUsedFolder.setItems(usedFolderRepository.findAll());
        grUsedFolder.getDataProvider().refreshAll();

        grUsedFolder.getEditor().setBuffered(true);
        grUsedFolder.getEditor().setBinder(binder);
        grUsedFolder.getEditor().addOpenListener(openEvent -> editButtons.forEach(button -> button.setEnabled(!grUsedFolder.getEditor().isOpen())));
        grUsedFolder.getEditor().addCloseListener(closeEvent -> editButtons.forEach(button -> button.setEnabled(!grUsedFolder.getEditor().isOpen())));
        grUsedFolder.getEditor().addSaveListener(saveEvent -> usedFolderRepository.save(saveEvent.getItem()));

        vlCardMain.getElement().getStyle().set("length", "100%");
        vlCardMain.add(btAdd, grUsedFolder);

        cPanel.getElement().getStyle().set("length", "100%");
        cPanel.add(vlCardMain);

        hlMain.add(cPanel);
        hlMain.setSizeFull();
        add(hlMain);
    }
}
