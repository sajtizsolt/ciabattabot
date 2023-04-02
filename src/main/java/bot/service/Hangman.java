package bot.service;

import bot.actualcommands.textcommands.HangmanCommand;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

public class Hangman {

    public static int errorCountLimit = 11;

    private final String wordsFile = "data/words_hu.txt";

    private int status = 0;

    int randomInt = 3;
    int serialNumber = 0;  //because of the upper limit
    String line = null;
    String word = null;
    String backgroundWord;
    char currentGuess;
    MessageChannel channel;

    int errorCount = 0;


    String guessedLetters = "";

    public Hangman(MessageChannel _channel) {
        HangmanCommand.gameInProgress = true;
        channel = _channel;
    }

    public void setCurrentGuess(char currentGuess) {
        if (Character.isAlphabetic(currentGuess)) {
            this.currentGuess = currentGuess;
            guess();
        } else {
            channel.sendMessage("Gonna need a letter!").queue();
        }
    }

    void guess() {
        boolean talalat = false;
        for (int i = 0; i < word.length(); i++) {
            if (currentGuess == word.charAt(i)) {
                char[] hatterszoChar = backgroundWord.toCharArray();
                hatterszoChar[i] = currentGuess;
                backgroundWord = String.valueOf(hatterszoChar);
                talalat = true;
            }
            if (i == word.length() - 1 && !talalat) {
                errorCount++;
            }
        }

        if (!(backgroundWord).equals(word)) {
            guessedLetters += currentGuess;
            StringBuilder letters = new StringBuilder();
            for (var c : guessedLetters.toCharArray())
                letters.append(c).append(", ");

            if (errorCount < errorCountLimit) {
                channel.sendMessage("Error count: " + errorCount + "\n" + backgroundWord + "\nGuess another letter!\n" + "Already guessed letters: " + letters.toString()).queue();
            } else {
                channel.sendMessage(backgroundWord + "\nYou lost!!\nThe word was " + word + "!").queue();
                HangmanCommand.gameInProgress = false;
                guessedLetters = "";
            }

        } else {
            channel.sendMessage(backgroundWord + "\nYou won!").queue();
            HangmanCommand.gameInProgress = false;
            guessedLetters = "";
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void statuszCheck() throws Exception {
        switch (status) {

            case 0:
                start();
                break;
            case 1:
                stop();
                break;
            case 2:
                restart();
                break;
        }
    }

    public void start() throws Exception {
        beolvas();
        errorCount = 0;
        String wordToSend = backgroundWord + "\n Guess a letter!";
        channel.sendMessage(wordToSend).queue();

    }

    public void restart() throws Exception {
        HangmanCommand.gameInProgress = true;
        errorCount = 0;
        channel.sendMessage("Restart:\n");
        setStatus(0);
        statuszCheck();
    }

    public void stop() {
        System.exit(1);
    }


    public void beolvas() throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(wordsFile, StandardCharsets.UTF_8));
            serialNumber = getSerialNumber();  //upper limit
            randomInt = randomnumber(serialNumber);
            try {
                int olvasottSor = 1;
                while ((line = br.readLine()) != null) {
                    if (olvasottSor == randomInt) {
                        word = line.toLowerCase();
                    }
                    olvasottSor++;
                }
            } catch (java.io.IOException f) {
                channel.sendMessage("Some kind of error happened while reading the file!").queue();
            } finally {
                if (line != null)
                    br.close();
            }

        } catch (FileNotFoundException e) {
            channel.sendMessage("File not found!").queue();
            e.printStackTrace();
        }

        backgroundWord = "";
        for (int i = 0; i < word.length(); i++) {
            backgroundWord = backgroundWord + "-";
        }
    }


    public int getSerialNumber() throws IOException {
        int sorokszama = 0;
        BufferedReader br = new BufferedReader(new FileReader(wordsFile));
        while (br.readLine() != null) sorokszama++;
        br.close();
        return sorokszama;
    }


    public int randomnumber(int felsokorlat) {

        int randomNum = ThreadLocalRandom.current().nextInt(1, felsokorlat + 1);
        return randomNum;

    }

    //segitseg(keresettSzo)   shows a letter for 33% of the error points

    public String segitseg(String kapottSzo, String hatterSzo) {
        boolean vanHianyzoBetu = false;
        int randomNumber = randomnumber(kapottSzo.length() - 1);
        for (int i = 0; i < kapottSzo.length(); i++) {
            if (hatterSzo.charAt(i) == '-') {
                vanHianyzoBetu = true;
                break;
            }
        }
        if (vanHianyzoBetu) {
            while (hatterSzo.charAt(randomNumber) != '-') {
                randomNumber = randomnumber(kapottSzo.length() - 1);
            }

        } else {
            return hatterSzo;
        }
        for (int i = 0; i < kapottSzo.length(); i++) {
            if (kapottSzo.charAt(randomNumber) == kapottSzo.charAt(i)) {
                char[] hatterszoChar = hatterSzo.toCharArray();
                hatterszoChar[i] = kapottSzo.charAt(i);
                hatterSzo = String.valueOf(hatterszoChar);
            }
        }
        return hatterSzo.toLowerCase();
    }

    //for adding a letter
    public void szavatHozzaad(String szotHozzaad) throws Exception {

        String szoHozzaadas = szotHozzaad;
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordsFile, true));
        writer.newLine();
        writer.write(szoHozzaadas);
        writer.close();

    }
}
