package de.xcase.filtercase2.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import de.xcase.filtercase2.backend.entities.Keyword;
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
    private final Button btAdd = new Button("Speichern");
    private final Button btCancel = new Button("Abbrechen");

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private LDAPRepository ldapRepository;

    public AddKeywordDialog(@Autowired KeywordRepository keywordRepository) {
        tfKeyword.setValueChangeMode(ValueChangeMode.EAGER);
        tfKeyword.addValueChangeListener(valueChangeEvent -> {
            if (valueChangeEvent != null && !valueChangeEvent.getValue().trim().equals("")) {
                btAdd.setEnabled(true);
            } else {
                btAdd.setEnabled(false);
            }
        });

        btAdd.setEnabled(false);
        btAdd.addClickListener(event -> {
            Keyword keyword = new Keyword();
            keyword.setKeyword(tfKeyword.getValue());
            //TODO Automatic User assignment ist not working.
            //keyword.setUserName(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
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
        formLayout.addFormItem(btAdd, "");
        formLayout.addFormItem(btCancel, "");
        this.add(formLayout);

    }
}

