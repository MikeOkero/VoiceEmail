package com.example.voiceemailassistant;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class ReceiveMail  {
    public static void receiveEmail(String pop3Host, String mailStrProt,
                                    String user, String password) {
        try {
            //1) get the session object
//            Properties properties = new Properties();
//            properties.put("mail.pop3.host", pop3Host);
//            properties.put("mail.pop3.port", "995");
//            properties.put("mail.pop3.starttls.enable", "true");
//            Session emailSession = Session.getDefaultInstance(properties);
            //IMAPS protocol
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
//Set host address
            props.setProperty("mail.imaps.host", "imaps.gmail.com");
//Set specified port
            props.setProperty("mail.imaps.port", "993");
//Using SSL
            props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.imaps.socketFactory.fallback", "false");
            Session imapSession = Session.getInstance(props);

            //2) create the POP3 store object and connect with the pop server
            //Store emailStore = emailSession.getStore("pop3s");
            //emailStore.connect(pop3Host, user, password);
            Store store = imapSession.getStore("imaps");
            store.connect("imap.gmail.com", user, password);

            //3) create the folder object and open it
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);
            //Folder emailFolder = emailStore.getFolder("INBOX");
            //emailFolder.open(Folder.READ_ONLY);

            //4) retrieve the messages from the folder in an array and print it
            //Message[] messages = emailFolder.getMessages();
            Message[] messages = inbox.getMessages();
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Message: " + message.getContent().toString());
            }

            //5) close the store and folder objects
            //emailFolder.close(false);
            //emailStore.close();
            inbox.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String pop3host = "pop.gmail.com";//change accordingly
        String mailStrProt = "pop3";
        String username= "myk3test123@gmail.com";
        String password= "m1k3t3st";//change accordingly

        receiveEmail(pop3host, mailStrProt, username, password);

    }
//    public static void main(String[] args) {
//        //Set mail properties and configure accordingly
//        String hostval = "pop.gmail.com";
//        String mailStrProt = "pop3";
//        String uname = "myk3test123@gmail.com";
//        String pwd = "m1k3t3st";
//        // Calling checkMail method to check received emails
//        checkMail(hostval, mailStrProt, uname, pwd);
//    }
//    public static void checkMail(String hostval, String mailStrProt, String uname,String pwd)
//    {
//        try {
//            //Set property values
//            Properties propvals = new Properties();
//            propvals.put("mail.pop3.host", hostval);
//            propvals.put("mail.pop3.port", "995");
//            propvals.put("mail.pop3.starttls.enable", "true");
//            Session emailSessionObj = Session.getDefaultInstance(propvals);

//            //Create POP3 store object and connect with the server
//            Store storeObj = emailSessionObj.getStore("pop3s");
//            storeObj.connect(hostval, uname, pwd);

//            //Create folder object and open it in read-only mode
//            Folder emailFolderObj = storeObj.getFolder("INBOX");
//            emailFolderObj.open(Folder.READ_ONLY);

//            //Fetch messages from the folder and print in a loop
//            Message[] messageobjs = emailFolderObj.getMessages();
//
//            for (int i = 0, n = messageobjs.length; i < n; i++) {
//                Message indvidualmsg = messageobjs[i];
//                System.out.println("Printing individual messages");
//                System.out.println("No# " + (i + 1));
//                System.out.println("Email Subject: " + indvidualmsg.getSubject());
//                System.out.println("Sender: " + indvidualmsg.getFrom()[0]);
//                System.out.println("Content: " + indvidualmsg.getContent());
//                //toString()
//
//            }
//            //Now close all the objects
//            emailFolderObj.close(false);
//            storeObj.close();
//        } catch (NoSuchProviderException exp) {
//            exp.printStackTrace();
//        } catch (MessagingException exp) {
//            exp.printStackTrace();
//        } catch (Exception exp) {
//            exp.printStackTrace();
//        }
//    }
}
