package de.xcase.filtercase2.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.UIScope;
import de.xcase.filtercase2.backend.entities.EMailAdress;
import de.xcase.filtercase2.backend.entities.LDAPUser;
import de.xcase.filtercase2.backend.respositories.EMailAdressesRespository;
import de.xcase.filtercase2.backend.respositories.LDAPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class AddMailDialog extends Dialog {
    private final FormLayout formLayout = new FormLayout();
    private final ComboBox<LDAPUser> cbUser = new ComboBox<>();
    private final Button btAdd = new Button("Speichern");
    private final Button btCancel = new Button("Abbrechen");

    @Autowired
    private EMailAdressesRespository mailAdressesRespository;



    public AddMailDialog(@Autowired LDAPRepository ldapRepository) {
        cbUser.setItems(ldapRepository.findAll());
        cbUser.setItemLabelGenerator(LDAPUser::getFullName);
        cbUser.addValueChangeListener(valueChange -> {
            if (valueChange.getValue() != null) {
                btAdd.setEnabled(true);
            } else {
                btAdd.setEnabled(false);
            }
        });

        btAdd.setEnabled(false);
        btAdd.addClickListener(event -> {
            EMailAdress adress = new EMailAdress();
            adress.setUser(cbUser.getValue().getFullName());
            adress.setEmailAddress(cbUser.getValue().getEMail());
            if (mailAdressesRespository.findByEmailAddress(cbUser.getValue().getEMail()) != null) {
                Notification.show("Dieser Eintrag existiert bereits");
            } else {
                mailAdressesRespository.save(adress);
                Notification.show("Erfolgreich gespeichert");
                this.close();
            }
        });
        btCancel.addClickListener(event -> this.close());

        formLayout.addFormItem(cbUser, "Nutzer");
        formLayout.addFormItem(btAdd, "");
        formLayout.addFormItem(btCancel, "");
        this.add(formLayout);
    }
}
