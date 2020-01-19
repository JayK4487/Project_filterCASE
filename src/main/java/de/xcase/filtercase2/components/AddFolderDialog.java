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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
        btAdd.setEnabled(false);
        btAdd.addClickListener(event -> {
            Folder folder = new Folder();
            folder.setDestinationFolder(tfDestinationFolder.getValue());
            //TODO Automatic User assignment ist not working.
            //folder.setUser(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
            if (usedFolderRepository.findByDestinationFolder(tfDestinationFolder.getValue()) != null) {
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
