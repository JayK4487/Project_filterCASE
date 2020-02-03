package de.xcase.filtercase2.components;

import de.xcase.filtercase2.backend.entities.Keyword;
import de.xcase.filtercase2.backend.respositories.EMailAdressesRespository;
import de.xcase.filtercase2.backend.respositories.FolderRepository;
import de.xcase.filtercase2.backend.respositories.KeywordRepository;
import de.xcase.filtercase2.utility.MailClient;
import de.xcase.filtercase2.utility.MailContentBuilder;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.FolderSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for executing the sort task.
 */
@Service
public class Executor {
    public static final String EWS_URL = "https://sonne.x-case.local/ews/Exchange.asmx";
    public static final Logger LOGGER = LogManager.getLogger(Executor.class);
    public static final String INBOX_FOLDER = "\\X-CASE\\XING\\Aktuelle_Projektanfragen\\";

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private EMailAdressesRespository eMailAdressesRespository;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private RuntimeVariables runtimeVariables;

    private final HashMap<String, List<String>> folderMap = new HashMap<>();

    private ExchangeService service;

    public void execute()  {
        Integer totalMails = 0;
        Integer noKeywordMatchingMails = 0;
        Integer distributedMails = 0;
        LocalDateTime startTime = LocalDateTime.now();
        String lastRun = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(startTime);
        //List<EmailMessage> undefiniteMails = new ArrayList<>();

        //Set service parameter
        try {
            service = new ExchangeService();
            ExchangeCredentials credentials = new WebCredentials("jgabriel", "Net333run.", "X-CASE");
            service.setCredentials(credentials);
            service.setUrl(new URI(EWS_URL));
            service.setPreAuthenticate(true);
        } catch (Exception e) {
            LOGGER.error("Could not connect to service", e);
            return;
        }

        //Map All Folders to the map
        folderRepository.findAll().forEach(folder -> {
            List<String> folderPathList = makeFolderPathList(folder);
            folderMap.put(folder.getDestinationFolder(), folderPathList);
        });

        Folder inputFolder = findFolder(makeFolderPathList(INBOX_FOLDER));

        FindItemsResults<Item> foundMails = null;

        Map<de.xcase.filtercase2.backend.entities.Folder, List<EmailMessage>> mailMap = new HashMap<>();
        folderRepository.findAll().forEach(folder -> mailMap.put(folder, new ArrayList<>()));

        int processes = 0;

        do {
            if (foundMails == null) {
                try {
                    foundMails = service.findItems(inputFolder.getId(), new ItemView(1000));
                } catch (ServiceLocalException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    foundMails = service.findItems(inputFolder.getId(), new ItemView(1000, foundMails.getNextPageOffset()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for(Item mail: foundMails.getItems()) {
                totalMails++;
                Boolean foundEMails = false;

                //Todo Fehler liegt hier. Er springt nicht in die Schleife.
                try {
                    mail.load();
                    String body = mail.getBody().toString();
                    for (de.xcase.filtercase2.backend.entities.Folder folder : mailMap.keySet()) {
                        //find folders with matching keywords
                        for (Keyword keyword : folder.getKeywords()) {
                            if (body.contains(keyword.getKeyword())) {
                                foundEMails = true;
                                List<EmailMessage> emailMessages = mailMap.get(folder);
                                if (mail.getClass().equals(EmailMessage.class) && !emailMessages.contains(mail)) {
                                    emailMessages.add((EmailMessage) mail);
                                    break;
                                }
                            }
                        }
                        //find folders without matching keywords
                    }
                    if (!foundEMails) {
                        noKeywordMatchingMails++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } while ((foundMails.isMoreAvailable()));


        //TODO uncomment
        for (de.xcase.filtercase2.backend.entities.Folder folder: mailMap.keySet()) {
            Folder destination = findFolder(makeFolderPathList(folder.getDestinationFolder()));
          try {
                for (EmailMessage emailMessage: mailMap.get(folder)) {
                    //emailMessage.copy(destination.getId());
                    //emailMessage.delete(DeleteMode.MoveToDeletedItems);
                    distributedMails++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Send mails
        Map<String, String> mailValues = new HashMap<>();
        mailValues.put("totalNumber", String.valueOf(totalMails));
        mailValues.put("deletedNumber", String.valueOf(noKeywordMatchingMails));
        mailValues.put("filteredNumber", String.valueOf(distributedMails));
        //mailValues.put("ambiguousNumber", "0");
        mailValues.put("lastRun", lastRun);

        eMailAdressesRespository.findAll().forEach(eMailAdress -> {
            MimeMessagePreparator messagePreparator = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom("no-reply-filterCASE@x-case.de");
                messageHelper.setTo(eMailAdress.getEmailAddress());
                messageHelper.setSubject("[filterCASE] Ergebnisbericht der letzten Filterung");
                messageHelper.setText(mailContentBuilder.build(mailValues), true);
            };
            mailClient.prepareAndSend(messagePreparator);
        });

        //Set the runtime variables
        runtimeVariables.setLastRun(startTime);
        runtimeVariables.setTotalMails(runtimeVariables.getTotalMails() + totalMails);
        runtimeVariables.setNoKeywordMatchingMails(runtimeVariables.getNoKeywordMatchingMails() + noKeywordMatchingMails);
        runtimeVariables.setDistributedMails(runtimeVariables.getDistributedMails() + distributedMails);
    }


    private Folder findFolder(de.xcase.filtercase2.backend.entities.Folder folderToSearch) {
        return findFolder(folderMap.get(folderToSearch.getDestinationFolder()));
    }

    private Folder findFolder(List<String> pathList) {
        if (pathList == null) {
            return null;
        }
        try {
            SearchFilter folderSearch = new SearchFilter.IsEqualTo(FolderSchema.DisplayName, pathList.get(0));
            FindFoldersResults publicFolderRoot = service.findFolders(WellKnownFolderName.PublicFoldersRoot, folderSearch, new FolderView(1));
            Folder pubFolder = publicFolderRoot.getFolders().get(0);
            if (pathList.size() > 1) {
                return resolveFolder(pubFolder, pathList.subList(1, pathList.size()));
            } else {
                return pubFolder;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> makeFolderPathList(de.xcase.filtercase2.backend.entities.Folder folder) {
        return makeFolderPathList(folder.getDestinationFolder());
    }

        private Folder resolveFolder(Folder searchFolder, List<String> path) {
        if (path.isEmpty()) {
            return searchFolder;
        }
        SearchFilter folderSearch = new SearchFilter.IsEqualTo(FolderSchema.DisplayName, path.get(0));
        try {
            FindFoldersResults foundFolders = searchFolder.findFolders(folderSearch, new FolderView(1));
            if (path.size() == 1) {
                if(foundFolders.getFolders().isEmpty()) {
                    return searchFolder;
                } else {
                    return foundFolders.getFolders().get(0);
                }
            } else {
                return resolveFolder(foundFolders.getFolders().get(0),  path.subList(1, path.size()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return the previous folder on error
            return searchFolder;
        }

    }

    private List<String> makeFolderPathList(String folderPath) {
        List<String> folderPathList  = new ArrayList<>(Arrays.asList(folderPath.split("\\\\")));
        folderPathList.removeIf(path -> path.trim().equals(""));
        return folderPathList;
    }
}
