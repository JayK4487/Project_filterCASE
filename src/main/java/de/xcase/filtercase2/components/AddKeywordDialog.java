package de.xcase.filtercase2.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.UIScope;
import de.xcase.filtercase2.backend.entities.Keyword;
import de.xcase.filtercase2.backend.respositories.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@UIScope

public class AddKeywordDialog extends Dialog {
    private final FormLayout formLayout = new FormLayout();
    private final ComboBox<Keyword> cbKeyword = new ComboBox<>();
    private final Button btAdd= new Button("Speichern");
    private final Button btCancel = new Button("Abbrechen");

    @Autowired
    private KeywordRepository keywordRepository;

    //Todo Saving new keyword is not working
    public AddKeywordDialog(@Autowired KeywordRepository keywordRepository) {
        cbKeyword.setItems(keywordRepository.findAll());
        cbKeyword.setItemLabelGenerator(Keyword::getKeyword);
        cbKeyword.addValueChangeListener(valueChange -> {
            if (valueChange.getValue() != null) {
                btAdd.setEnabled(true);
            } else {
                btAdd.setEnabled(false);
            }
        });

        btAdd.setEnabled(false);
        btAdd.addClickListener(event -> {
            Keyword keyword = new Keyword();
            keyword.setKeyword(cbKeyword.getValue().getKeyword());
            if(keywordRepository.findByKeyword(cbKeyword.getValue().getKeyword()) != null) {
                Notification.show("Dieser Eintrag existiert bereits");
            } else {
                keywordRepository.save(keyword);
                Notification.show("Erfolgreich gespeichert");
                this.close();
            }
            //keyword.setUserKeyword(cbKeyword.getValue().getUserKeyword());
        });
        btCancel.addClickListener(event -> this.close());

        formLayout.addFormItem(cbKeyword, "Schlüsselbegriff");
        formLayout.addFormItem(btAdd, "");
        formLayout.addFormItem(btCancel, "");
        this.add(formLayout);

    }
}

