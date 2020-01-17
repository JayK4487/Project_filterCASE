package de.xcase.filtercase2.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import de.xcase.filtercase2.backend.entities.Folder;
import de.xcase.filtercase2.backend.respositories.LDAPRepository;
import de.xcase.filtercase2.backend.respositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@UIScope
public class AddFolderDialog extends Dialog {
    private final FormLayout formLayout = new FormLayout();
    private final TextField tfDestinationFolder = new TextField();
    private final Button btAdd = new Button("Speichern");
    private final Button btCancel = new Button("Abbrechen");

    @Autowired
    private FolderRepository usedFolderRepository;

    @Autowired
    private LDAPRepository ldapRepository;

    public AddFolderDialog(@Autowired FolderRepository usedFolderRepository) {
        tfDestinationFolder.setValueChangeMode(ValueChangeMode.EAGER);
        tfDestinationFolder.addValueChangeListener(valueChangeEvent -> {
           if (valueChangeEvent != null && !valueChangeEvent.getValue().trim().equals("")) {
               btAdd.setEnabled(true);
            } else {
               btAdd.setEnabled(false);
            }
        });
        //Todo Add function is still incomplete. I deleted the option to add a new sourceFolder because it will be always the same sourceFolder.
        btAdd.setEnabled(false);
        btAdd.addClickListener(event -> {
            Folder folder = new Folder();
            folder.setDestinationFolder(tfDestinationFolder.getValue());
            //usedFolder.setUser(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
            if (usedFolderRepository.findByDestinationFolder(tfDestinationFolder.getValue()) != null) {
                usedFolderRepository.save(folder);
                Notification.show("Dieser Eintrag existiert bereits");
            } else {
                usedFolderRepository.save(folder);
                Notification.show("Erfolgreich gespeichert");
                this.close();
            }
        });

        btCancel.addClickListener(event -> this.close());

        formLayout.addFormItem(tfDestinationFolder, "Zielordner");
        formLayout.addFormItem(btAdd, "");
        formLayout.addFormItem(btCancel, "");
        this.add(formLayout);
    }
}
