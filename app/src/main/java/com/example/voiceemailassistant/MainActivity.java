package com.example.voiceemailassistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecognitionListener {
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private static final int REQUEST_RECORD_PERMISSION = 100;
    private TextView returnedText;
    private TextView returnedText1;
    private TextView returnedText2;
    private TextView returnedText3;
    private ProgressBar progressBar;
    private TextToSpeech tts;
    // private static final int REQ_CODE_SPEECH_INPUT = 100;
    private boolean IsInitialVoiceFinshed;
    private int numberOftaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IsInitialVoiceFinshed = false ;
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    speak("Welcome to your voice email assistant.Tap once to begin and tap again to go to the next field. Tell me the mail address to whom you want to send mail?");
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

        returnedText = (TextView) findViewById(R.id.voiceInput);
        returnedText1 = (TextView) findViewById(R.id.voiceInput1);
        returnedText2 = (TextView) findViewById(R.id.voiceInput2);
        returnedText3 = (TextView) findViewById(R.id.error);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        numberOftaps = 0;

    }

    private void speak(String text){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void startVoiceInput() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
               "en-KE");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);//amount of results at a go
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast
                            .LENGTH_SHORT).show();
                }
        }
    }
    public void layoutTaped(View view)
    {
        if(IsInitialVoiceFinshed) {
            numberOftaps++;
            startVoiceInput();
        }
    }


    @Override
    protected void onStop () {
        super.onStop();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }



    @Override
    public void onBeginningOfSpeech () {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived ( byte[] buffer){
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech () {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);

    }

    @Override
    public void onError ( int errorCode){
        // String errorMessage = getErrorText(errorCode);
        //Log.d(LOG_TAG, "FAILED " + errorMessage);
        //returnedText3.setText(errorMessage);

    }

    @Override
    public void onEvent ( int arg0, Bundle arg1){

        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults (Bundle results){
        Log.i(LOG_TAG, "onPartialResults");

    }

    @Override
    public void onReadyForSpeech (Bundle arg0){

        Log.i(LOG_TAG, "onReadyForSpeech");
    }


    private void exitFromApp()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.finishAffinity();
        }
    }



    @Override
    //RETURN SPEECH INPUT
    public void onResults (Bundle results){
        Log.i(LOG_TAG, "onResults");
        IsInitialVoiceFinshed = false;


        ArrayList<String> result = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(result.get(0).equals("inbox"))
        {
            Intent myIntent = new Intent(MainActivity.this, inbox.class);
            MainActivity.this.startActivity(myIntent);
//            speak("Cancelled!");
//            exitFromApp();
        }
        else {

            switch (numberOftaps) {

                case 1:
                    //result.get(0).equals("next");
                    String to;
                    to= result.get(0).replaceAll("at","@");
                    to= to.replaceAll("\\s+","");
                    //to= to + "@" + to;
                    returnedText.setText(to);
                    speak("What is the subject?");
//                    speak("Mail To:" + returnedText.getText().toString()+ "\n Is this correct?");
//                    if(result.get(0).equals("no"))
//                    {
//
//                    }
                    //initializeTextToSpeech3();

                    break;
                case 2:
                    returnedText1.setText(result.get(0));
                    //initializeTextToSpeech2();
                    speak("Tell me your message and then tap to confirm if everything is okay");
                    break;

                case 3:
                    returnedText2.setText(result.get(0));
                    //initializeTextToSpeech1();
                    speak("Tap and say 'confirm' to confirm the email");
                    break;
                case 4:
                    speak("Please Confirm the mail\n To : " + returnedText.getText().toString() + "\nSubject : " + returnedText1.getText().toString() + "\nMessage : " + returnedText2.getText().toString() + "\nSpeak Yes to confirm");
                    break;


                default:
                    if(result.get(0).equals("yes"))
                    {
                        speak("Sending the mail");
                        sendEmail();
                    }else
                    {

                        speak("Refreshing to compose a new email");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //exitFromApp();
                                Intent i = new Intent(MainActivity.this, MainActivity.class);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(i);
                                overridePendingTransition(0, 0);
                            }
                        }, 4000);
                    }
            }

            IsInitialVoiceFinshed = true;

        }


    }



    //PROGRESS BAR IS USED TO SHOW THE SPEECH INPUT FOR DEMONSTRATION PURPOSES
    @Override
    public void onRmsChanged ( float rmsdB){
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }


    //ERROR MESSAGES THAT APPEAR DURING INPUT

//    public String getErrorText(int errorCode) {
//        String message;
//        switch (errorCode) {
//
//            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
//                initializeTextToSpeech1();
//                message = "No speech input";
//                startVoiceInput();
//                break;
//            default:
//                initializeTextToSpeech3();
//                startVoiceInput();
//                message = "Didn't catch that, please try again.";
//                break;
//        }
//        return message;
//    }




    private void sendEmail() {
        //Getting content for email
        String email =  returnedText.getText().toString().trim();
        String subject = returnedText1.getText().toString().trim();
        String message = returnedText2.getText().toString().trim();

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

}
