package com.ermayurmahajan.wordgame;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainActivity3 extends AppCompatActivity implements OnUserEarnedRewardListener{
    private AdView adView;
    ProgressBar progressBar;
    InterstitialAd mInterstitialAd;
    int countResetButton = 0;
    Button btnA,btnB,btnC,btnD,btnE,btnF,btnG, btnH,btnI,btnJ,btnK,btnL,btnM,btnN,btnO, btnP,btnQ,btnR,btnS,btnT,btnU,btnV,btnW, btnX, btnY, btnZ, btnReset;
    TextView txtWordToBeGuessed;
    ImageView selectedTypeImage;
    String wordToBeGuessed, wordDisplayedString;
    String selectedType;
    char[] wordDisplayedCharArray;
    ArrayList<String> mylistOfWords;
    public char edtInput;
    String lettersTried;
    TextView txtTriesLeft;
    String triesLeft;
    final String WINNING_MESSAGE = "You Won!";
    final String LOSING_MESSAGE  = "You Lost!";
    Animation rotateAnimation;
    Animation scaleAnimation;
    Animation scaleAndRotateAnimation;
    void revealLetterInWord(char letter){
        int indexOfLetter = wordToBeGuessed.indexOf(letter);

        //loop if index is positive or 0
        while(indexOfLetter >= 0){
            wordDisplayedCharArray[indexOfLetter] = wordToBeGuessed.charAt(indexOfLetter);
            indexOfLetter = wordToBeGuessed.indexOf(letter, indexOfLetter +1);
        }

        //update the string as well
        wordDisplayedString = String.valueOf(wordDisplayedCharArray);
    }
    void displayWordOnScreen(){
        String formattedString = "";
        for(char character : wordDisplayedCharArray){
            formattedString += character + " ";
        }
        txtWordToBeGuessed.setText(formattedString);
    }
    void initializeGame(){
        Collections.shuffle(mylistOfWords);
        wordToBeGuessed = mylistOfWords.get(0);
        mylistOfWords.remove(0);
        wordDisplayedCharArray = wordToBeGuessed.toCharArray();

        for(int i = 1; i < wordDisplayedCharArray.length - 1; i++){
            wordDisplayedCharArray[i] = '_';
        }
        revealLetterInWord(wordDisplayedCharArray[0]);

        revealLetterInWord(wordDisplayedCharArray[wordDisplayedCharArray.length - 1]);

        wordDisplayedString = String.copyValueOf(wordDisplayedCharArray);

        displayWordOnScreen();
        lettersTried = " ";
        //Tries left
        triesLeft = " X X X X X";
        txtTriesLeft.setText(triesLeft);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);
        btnE = findViewById(R.id.btnE);
        btnF = findViewById(R.id.btnF);
        btnG = findViewById(R.id.btnG);
        btnH = findViewById(R.id.btnH);
        btnI = findViewById(R.id.btnI);
        btnJ = findViewById(R.id.btnJ);
        btnK = findViewById(R.id.btnK);
        btnL = findViewById(R.id.btnL);
        btnM = findViewById(R.id.btnM);
        btnN = findViewById(R.id.btnN);
        btnO = findViewById(R.id.btnO);
        btnP = findViewById(R.id.btnP);
        btnQ = findViewById(R.id.btnQ);
        btnR = findViewById(R.id.btnR);
        btnS = findViewById(R.id.btnS);
        btnT = findViewById(R.id.btnT);
        btnU = findViewById(R.id.btnU);
        btnV = findViewById(R.id.btnV);
        btnW = findViewById(R.id.btnW);
        btnX = findViewById(R.id.btnX);
        btnY = findViewById(R.id.btnY);
        btnZ = findViewById(R.id.btnZ);
        btnReset= findViewById(R.id.btnReset);
        selectedTypeImage = findViewById(R.id.imgBackground);
        mylistOfWords = new ArrayList<String>();
        txtWordToBeGuessed = (TextView) findViewById(R.id.txtWordToGuessed);
        txtTriesLeft = (TextView) findViewById(R.id.txtTriesLeft);
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
        scaleAndRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_and_rotate);
        scaleAndRotateAnimation.setFillAfter(true);
        selectedType = getIntent().getStringExtra("typeChooses");
        progressBar = findViewById(R.id.progressBar);
        InputStream myInputStream = null;
        Scanner in = null;
        String aWord;
        try {
            switch (selectedType) {
                case ("Color"):
                    myInputStream = getAssets().open("color.txt");
                    break;
                case ("Fruit"):
                    selectedTypeImage.setImageResource(R.drawable.fruit_background);
                    myInputStream = getAssets().open("fruit.txt");
                    break;
                case ("City"):
                    selectedTypeImage.setImageResource(R.drawable.city_background);
                    myInputStream = getAssets().open("city.txt");
                    break;
                case ("Pokemon"):
                    selectedTypeImage.setImageResource(R.drawable.pokemon_background);
                    myInputStream = getAssets().open("pokemon.txt");
                    break;
            }
            in = new Scanner(myInputStream);
            while (in.hasNext()) {
                aWord = in.next();
                mylistOfWords.add(aWord);
            }

        } catch (IOException e) {
            Toast.makeText(this, e.getClass().getSimpleName() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }finally {
            if(in != null){
                in.close();
            }
            try {
                if(myInputStream != null){
                    myInputStream.close();
                }
            } catch (IOException e) {
                Toast.makeText(this, e.getClass().getSimpleName() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        initializeGame();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }

    public void inputOfButton(View v){
        switch (v.getId()){
            case (R.id.btnA):
                edtInput ='a';
                btnA.setEnabled(false);
                break;
            case (R.id.btnB):
                edtInput ='b';
                btnB.setEnabled(false);
                break;
            case (R.id.btnC):
                edtInput ='c';
                btnC.setEnabled(false);
                break;
            case (R.id.btnD):
                edtInput ='d';
                btnD.setEnabled(false);
                break;
            case (R.id.btnE):
                edtInput ='e';
                btnE.setEnabled(false);
                break;
            case (R.id.btnF):
                edtInput ='f';
                btnF.setEnabled(false);
                break;
            case (R.id.btnG):
                edtInput ='g';
                btnG.setEnabled(false);
                break;
            case (R.id.btnH):
                edtInput ='h';
                btnH.setEnabled(false);
                break;
            case (R.id.btnI):
                edtInput ='i';
                btnI.setEnabled(false);
                break;
            case (R.id.btnJ):
                edtInput ='j';
                btnJ.setEnabled(false);
                break;
            case (R.id.btnK):
                edtInput ='k';
                btnK.setEnabled(false);
                break;
            case (R.id.btnL):
                edtInput ='l';
                btnL.setEnabled(false);
                break;
            case (R.id.btnM):
                edtInput ='m';
                btnM.setEnabled(false);
                break;
            case (R.id.btnN):
                edtInput ='n';
                btnN.setEnabled(false);
                break;
            case (R.id.btnO):
                edtInput ='o';
                btnO.setEnabled(false);
                break;
            case (R.id.btnP):
                edtInput ='p';
                btnP.setEnabled(false);
                break;
            case (R.id.btnQ):
                edtInput ='q';
                btnQ.setEnabled(false);
                break;
            case (R.id.btnR):
                edtInput ='r';
                btnR.setEnabled(false);
                break;
            case (R.id.btnS):
                edtInput ='s';
                btnS.setEnabled(false);
                break;
            case (R.id.btnT):
                edtInput ='t';
                btnT.setEnabled(false);
                break;
            case (R.id.btnU):
                edtInput ='u';
                btnU.setEnabled(false);
                break;
            case (R.id.btnV):
                edtInput ='v';
                btnV.setEnabled(false);
                break;
            case (R.id.btnW):
                edtInput ='w';
                btnW.setEnabled(false);
                break;
            case (R.id.btnX):
                edtInput ='x';
                btnX.setEnabled(false);
                break;
            case (R.id.btnY):
                edtInput ='y';
                btnY.setEnabled(false);
                break;
            case (R.id.btnZ):
                edtInput ='z';
                btnZ.setEnabled(false);
                break;
        }
        checkIfLetterIsInWord(edtInput);
    }
    void checkIfLetterIsInWord(char letter){
        //if the letter was found inside the word to be guessed
        if(wordToBeGuessed.indexOf(letter) >= 0){
            //if the letter was NOT displayed yet
            if(wordDisplayedString.indexOf(letter) < 0){

                //animate
                txtWordToBeGuessed.startAnimation(scaleAnimation);

                //replace the underscores with the letter
                revealLetterInWord(letter);

                //update the changes on screen
                displayWordOnScreen();

                //check if the game is won
                if(!wordDisplayedString.contains("_")){
                    txtTriesLeft.startAnimation(scaleAndRotateAnimation);

                    // winning sound
                    final MediaPlayer mediaPlayer = MediaPlayer.create(this , R.raw.win);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                        }
                    });
                    setDisableButton();
                    txtTriesLeft.setText(WINNING_MESSAGE);
                }
                else {
                    //enter sound
                    final MediaPlayer mediaPlayer = MediaPlayer.create(this , R.raw.correct_input);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                        }
                    });

                }
            }
        }
        //otherwise , if the letter was NOT found inside the word to be guessed
        else {
            //decrease the number of tries left, and well show it on screen
            decreaseAndDisplayTriesLeft();

            //check if the game is lost
            if(triesLeft.isEmpty()){
                txtTriesLeft.startAnimation(scaleAndRotateAnimation);

                //lose sound
                final MediaPlayer mediaPlayer = MediaPlayer.create(this , R.raw.lose);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                    }
                });
                setDisableButton();
                txtTriesLeft.setText(LOSING_MESSAGE);
                txtWordToBeGuessed.setText(wordToBeGuessed);
            }
        }

        //display the letter that was tried
        if(lettersTried.indexOf(letter) < 0){
            lettersTried += letter + ", ";
        }
        else {
            //enter sound
            final MediaPlayer mediaPlayer = MediaPlayer.create(this , R.raw.wrong_input);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
        }
    }
    void decreaseAndDisplayTriesLeft(){
        //if there are still some tries left
        if(!triesLeft.isEmpty()){
            //wrong input sound
            //enter sound
            final MediaPlayer mediaPlayer = MediaPlayer.create(this , R.raw.wrong_input);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
            //animate
            txtTriesLeft.startAnimation(scaleAnimation);
            //take out the last 2 characters form this  string
            triesLeft = triesLeft.substring(0 , triesLeft.length() - 2);
            txtTriesLeft.setText(triesLeft);
        }
    }

    public void resetGame (View v){
        countResetButton = countResetButton + 1;
        //restart game
        final MediaPlayer mediaPlayer  = MediaPlayer.create(this , R.raw.restart);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
        //start animation
//        trReset.startAnimation(rotateAnimation);
        btnReset.startAnimation(rotateAnimation);

        //clear animation on
        txtTriesLeft.clearAnimation();
        setEnableButton();
        if (countResetButton > 3){
            progressBar.setVisibility(View.VISIBLE);
            interstitialLoadAds();
        }
        // setup a new game
        initializeGame();
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd == null){
            progressBar.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this, getString(R.string.inter_ad_unit2_id), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd){
                    super.onAdLoaded(interstitialAd);
                    mInterstitialAd = interstitialAd;
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                            mInterstitialAd = null;
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            MainActivity3.super.onBackPressed();
                            mInterstitialAd = null;
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(MainActivity3.this);
                                countResetButton = 0;
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    },5);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    progressBar.setVisibility(View.GONE);
                    super.onAdFailedToLoad(loadAdError);
                    MainActivity3.super.onBackPressed();
                    Log.e("Error", loadAdError.toString());
                }
            });
        }
        else {
            MainActivity3.super.onBackPressed();
            progressBar.setVisibility(View.GONE);
        }
    }

    public void setDisableButton(){
        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
        btnE.setEnabled(false);
        btnF.setEnabled(false);
        btnG.setEnabled(false);
        btnH.setEnabled(false);
        btnI.setEnabled(false);
        btnJ.setEnabled(false);
        btnK.setEnabled(false);
        btnL.setEnabled(false);
        btnM.setEnabled(false);
        btnN.setEnabled(false);
        btnO.setEnabled(false);
        btnP.setEnabled(false);
        btnQ.setEnabled(false);
        btnR.setEnabled(false);
        btnS.setEnabled(false);
        btnT.setEnabled(false);
        btnU.setEnabled(false);
        btnV.setEnabled(false);
        btnW.setEnabled(false);
        btnX.setEnabled(false);
        btnY.setEnabled(false);
        btnZ.setEnabled(false);
    }
    public void setEnableButton(){
        btnA.setEnabled(true);
        btnB.setEnabled(true);
        btnC.setEnabled(true);
        btnD.setEnabled(true);
        btnE.setEnabled(true);
        btnF.setEnabled(true);
        btnG.setEnabled(true);
        btnH.setEnabled(true);
        btnI.setEnabled(true);
        btnJ.setEnabled(true);
        btnK.setEnabled(true);
        btnL.setEnabled(true);
        btnM.setEnabled(true);
        btnN.setEnabled(true);
        btnO.setEnabled(true);
        btnP.setEnabled(true);
        btnQ.setEnabled(true);
        btnR.setEnabled(true);
        btnS.setEnabled(true);
        btnT.setEnabled(true);
        btnU.setEnabled(true);
        btnV.setEnabled(true);
        btnW.setEnabled(true);
        btnX.setEnabled(true);
        btnY.setEnabled(true);
        btnZ.setEnabled(true);
    }
    void interstitialLoadAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.inter_ad_unit1_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd){
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        mInterstitialAd = null;
                        progressBar.setVisibility(View.GONE);
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(MainActivity3.this);
                            countResetButton = 0;
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },10);
            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("Error", loadAdError.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    public void rewardLoadAd(View v){
        //clicking sound
        final MediaPlayer mediaPlayer = MediaPlayer.create(this , R.raw.click);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        MobileAds.initialize(MainActivity3.this);
        RewardedInterstitialAd.load(MainActivity3.this, getString(R.string.reward_ad_unit_id), new AdRequest.Builder().build(), new RewardedInterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                super.onAdLoaded(rewardedInterstitialAd);
                rewardedInterstitialAd.show(MainActivity3.this, MainActivity3.this);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
        incressTrieReward();
    }
    void incressTrieReward(){
        txtTriesLeft.startAnimation(scaleAnimation);
        //add the last 2 characters form this  string
        triesLeft = triesLeft + " X X";
        txtTriesLeft.setText(triesLeft);
        Toast.makeText(this, "Two Chance Added", Toast.LENGTH_SHORT).show();
    }
}