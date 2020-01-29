package de.xcase.filtercase2.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(Map<String, String> values) {
        Context context = new Context();
        for(String key: values.keySet()) {
            context.setVariable(key, values.get(key));
        }
        return templateEngine.process("eMailTemplate", context);
    }
}
