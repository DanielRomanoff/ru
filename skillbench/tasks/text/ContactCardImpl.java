package ru.skillbench.tasks.text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.*;

public class ContactCardImpl implements ContactCard {
    private String fullName;
    private String department;
    private boolean isWoman;
    private Calendar birthday;
    private HashMap<String, String> phone = new HashMap<>();

    @Override
    public ContactCard getInstance(Scanner scanner) {
        ContactCardImpl contactCard = new ContactCardImpl();
        String s = scanner.nextLine();
        if (!s.equals("BEGIN:VCARD"))
            throw new NoSuchElementException();
        while (scanner.hasNextLine()) {
            s = scanner.nextLine();
            if (!s.contains(":"))
                throw new InputMismatchException();
            if (s.contains("FN")) {
                contactCard.fullName = s.substring(3);
            } else if (s.contains("ORG")) {
                contactCard.department = s.substring(4);
            } else if (s.contains("GENDER")) {
                if (s.substring(7).equals("M"))
                    contactCard.isWoman = false;
                else if (s.substring(7).equals("F"))
                    contactCard.isWoman = true;
                else throw new NoSuchElementException();
            } else if (s.contains("BDAY")) {
                try {
                    contactCard.birthday = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    simpleDateFormat.setLenient(false);
                    contactCard.birthday.setTime(simpleDateFormat.parse(s.substring(5)));
                } catch (ParseException e) {
                    InputMismatchException mismatchException = new InputMismatchException();
                    mismatchException.initCause(e);
                    throw mismatchException;
                }
            }

                if (s.contains("TEL")) {
                    String number = s.substring(s.indexOf(":") + 1);
                    if (!number.matches("\\d{10}")) {
                        throw new InputMismatchException();
                    }
                    contactCard.phone.put(s.substring(s.indexOf("=") + 1, s.indexOf(":")), number);
                }
                }
        if (!s.equals("END:VCARD") || contactCard.fullName == null || contactCard.department == null) {
            throw new NoSuchElementException();
        }
        return contactCard;
    }
    @Override
    public ContactCard getInstance(String data) {
        return getInstance(new Scanner(data));
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getOrganization() {
        return department;
    }

    @Override
    public boolean isWoman() {
        return isWoman;
    }

    @Override
    public Calendar getBirthday() {
        if(birthday == null) {
            throw new NoSuchElementException();
        }
        return birthday;
    }

    @Override
    public Period getAge() {
        if(birthday == null) {
            throw new NoSuchElementException();
        }
        Calendar result = Calendar.getInstance();
        result.add(Calendar.DAY_OF_MONTH, -birthday.get(Calendar.DAY_OF_MONTH));
        result.add(Calendar.MONTH, -birthday.get(Calendar.MONTH));
        result.add(Calendar.YEAR, -birthday.get(Calendar.YEAR));
        return Period.of(result.get(Calendar.YEAR), result.get(Calendar.MONTH), result.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public int getAgeYears() {
        if(birthday == null) {
            throw new NoSuchElementException();
        }
        return getAge().getYears();
    }

    @Override
    public String getPhone(String type) {
        if(!phone.containsKey(type)) {
            throw new NoSuchElementException();
        }
        StringBuilder stringBuilder = new StringBuilder(phone.get(type));
        stringBuilder.insert(0, "(");
        stringBuilder.insert(4, ")");
        stringBuilder.insert(5, " ");
        stringBuilder.insert(9, "-");
        return stringBuilder.toString();
    }
}