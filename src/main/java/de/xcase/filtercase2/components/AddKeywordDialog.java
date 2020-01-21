package de.xcase.filtercase2.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import de.xcase.filtercase2.backend.entities.Folder;
import de.xcase.filtercase2.backend.entities.Keyword;
import de.xcase.filtercase2.backend.respositories.FolderRepository;
import de.xcase.filtercase2.backend.respositories.KeywordRepository;
import de.xcase.filtercase2.backend.respositories.LDAPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class AddKeywordDialog extends Dialog {
    private final FormLayout formLayout = new FormLayout();
    private final TextField tfKeyword = new TextField();
    private final ComboBox<Folder> cbFolders = new ComboBox<>();
    private final Button btAdd = new Button("Speichern");
    private final Button btCancel = new Button("Abbrechen");

    public AddKeywordDialog(@Autowired KeywordRepository keywordRepository, @Autowired LDAPRepository ldapRepository, @Autowired FolderRepository folderRepository) {
        cbFolders.setItems(folderRepository.findAll());
        cbFolders.setItemLabelGenerator(Folder::getDestinationFolder);
        cbFolders.addValueChangeListener(valueChange -> {
           checkSaveButton();
        });
        tfKeyword.setValueChangeMode(ValueChangeMode.EAGER);
        tfKeyword.addValueChangeListener(valueChangeEvent -> {
            checkSaveButton();
        });

        btAdd.setEnabled(false);
        btAdd.addClickListener(event -> {
            Keyword keyword = new Keyword();
            keyword.setKeyword(tfKeyword.getValue());
            keyword.setUserName(ldapRepository.findByAccountName(SecurityContextHolder.getContext().getAuthentication().getName()).getFullName());
            keyword.setFolder(cbFolders.getValue());
            if(keywordRepository.findByKeyword(tfKeyword.getValue()) != null) {
                Notification.show("Dieser Eintrag existiert bereits");
            } else {
                keywordRepository.save(keyword);
                Notification.show("Erfolgreich gespeichert");
                this.close();
            }
        });

        btCancel.addClickListener(event -> this.close());

        formLayout.addFormItem(tfKeyword, "Schlüsselbegriff");
        formLayout.addFormItem(cbFolders, "Ordner");
        formLayout.addFormItem(btAdd, "");
        formLayout.addFormItem(btCancel, "");
        this.add(formLayout);
    }

    private void checkSaveButton(){
        if (tfKeyword.getValue() != null && !tfKeyword.getValue().trim().equals("") && cbFolders.getValue() != null) {
            btAdd.setEnabled(true);
        } else {
            btAdd.setEnabled(false);
        }
    }
}

