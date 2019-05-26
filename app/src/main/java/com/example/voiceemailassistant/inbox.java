package com.example.voiceemailassistant;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class inbox extends AppCompatActivity {
    private TextView from;
    private TextView esubject;
    private TextView emessage;
    private TextToSpeech tts1;
    private boolean IsInitialVoiceFinshed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        IsInitialVoiceFinshed = false ;

        tts1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts1.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    speak("This is the inbox");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IsInitialVoiceFinshed=true;
                        }
                    }, 6000);
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        from= (TextView) findViewById(R.id.emailfrom);
        esubject= (TextView) findViewById(R.id.emailsubject);
        emessage= (TextView) findViewById(R.id.emailmessage);


    }
    private void speak(String text) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts1.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }


    }


    public static void receiveEmail(String pop3Host, String mailStrProt,
                                    String user, String password) {
        try {
            //1) get the session object
            Properties properties = new Properties();
            properties.put("mail.pop3.host", pop3Host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //2) create the POP3 store object and connect with the pop server
            Store emailStore = emailSession.getStore("pop3s");
            emailStore.connect(pop3Host, user, password);

            //3) create the folder object and open it
            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            //4) retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Message: " + message.getContent().toString());
            }
//            for (int x = 0; x<messages.length; x++){
//                Message message = messages[x];
//                from.setText(message.getFrom()[0]);
//            }

            //5) close the store and folder objects
            emailFolder.close(false);
            emailStore.close();

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

//    private void receiveEmail()
//    {
//        for (int i = 0, n = messageobjs.length; i < n; i++) {
//            Message indvidualmsg = messageobjs[i];
//            System.out.println("Printing individual messages");
//            System.out.println("No# " + (i + 1));
//            System.out.println("Email Subject: " + indvidualmsg.getSubject());
//            System.out.println("Sender: " + indvidualmsg.getFrom()[0]);
//            System.out.println("Content: " + indvidualmsg.getContent().toString());
//
//        }
//    }


//    private void receiveEmail()
//    {
//        //Getting content for email
//        String email =  from.setText().toString().trim();
//        String subject = esubject.setText().toString().trim();
//        String message = emessage.setText().toString().trim();
//
//        //Creating SendMail object
//        ReceiveMail rm = new ReceiveMail(this, From, Subject, Content);
//
//        //Executing sendmail to send email
//        rm.execute();
//    }
}
