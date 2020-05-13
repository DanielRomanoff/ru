package ru.skillbench.tasks.text.regex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurriculumVitaeImpl implements CurriculumVitae{
    private String text;
    private String name;
    private String surname;
    private String patronymic;
    private HashMap<String, String > hiddenPieces = new HashMap<>();
    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        if (text == null)
            throw new IllegalStateException();
        return text;
    }

    @Override
    public List<Phone> getPhones() {
        List<Phone> phoneList = new LinkedList<>();
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(getText());
        while (matcher.find()){
            int firstDigit;
            int secondDigit;
            if(matcher.group(2) != null)
                firstDigit = Integer.parseInt(matcher.group(2));
            else
                firstDigit = -1;

            if(matcher.group(matcher.groupCount()) != null)
                secondDigit = Integer.parseInt(matcher.group(matcher.groupCount()));
            else
                secondDigit = -1;

            phoneList.add(new Phone(matcher.group(), firstDigit, secondDigit));
        }
        return phoneList;
    }

    @Override
    public String getFullName() {
        Pattern FNPattern = Pattern.compile("(?=^.{0,40}$)^[a-zA-Z-]+\\s[a-zA-Z-]+$");
        Matcher matcher = FNPattern.matcher(getText());
        if (matcher.find()){
            name = matcher.group(1);
            surname = matcher.group(3);
            patronymic = matcher.group(4);
            return matcher.group();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public String getFirstName() {
        if(name == null || surname == null || patronymic == null) {
            getFullName();
        }
        return name;
    }

    @Override
    public String getMiddleName() {
        if(name == null || surname == null || patronymic == null) {
            getFullName();
        }
        return surname;
    }

    @Override
    public String getLastName() {
        if(name == null || surname == null || patronymic == null) {
            getFullName();
        }
        return patronymic;
    }


    @Override
    public void updateLastName(String newLastName) {
        text = getText().replace(getLastName(), newLastName);
        patronymic = newLastName;
    }

    @Override
    public void updatePhone(Phone oldPhone, Phone newPhone) {
        if (getText().contains(oldPhone.getNumber())) {
            text = getText().replace(oldPhone.getNumber(), newPhone.getNumber());
        } else {
            throw new IllegalArgumentException();
        }
    }
    @Override
    public void hide(String piece) {
        if(getText().contains(piece)) {
            String replacement = piece.replaceAll("[^ .@]", "X");
            text = getText().replace(piece, replacement);
            hiddenPieces.put(replacement, piece);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void hidePhone(String phone) {
        if(getText().contains(phone)) {
            String replacement = phone.replaceAll("[\\d]", "X");
            text = getText().replace(phone, replacement);
            hiddenPieces.put(replacement, phone);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int unhideAll() {
        if(text == null) {
            throw new IllegalStateException();
        }
        int replaced = hiddenPieces.keySet().size();
        for(String key : hiddenPieces.keySet()) {
            text = getText().replace(key, hiddenPieces.get(key));
        }
        hiddenPieces.clear();
        return replaced;
    }
}
