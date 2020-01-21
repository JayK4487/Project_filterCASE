package de.xcase.filtercase2.components;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.NameResolutionCollection;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;

/**
 * Class for executing the sort task.
 */
@Component
public class Executor {

    @Bean
    public void execute()  {
        /*
        try {
            ExchangeService service = new ExchangeService();
            ExchangeCredentials credentials = new WebCredentials("jsauer", "    ", "X-CASE");
            service.setCredentials(credentials);
            service.setUrl(new URI("https://sonne.x-case.local/ews/Exchange.asmx"));
            service.setPreAuthenticate(true);

            NameResolutionCollection name =  service.resolveName("leon");
            System.out.println(name.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
             */
    }
}
