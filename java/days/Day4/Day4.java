package days.Day4;

import days.Day;

import java.util.*;
import java.util.stream.Collectors;

enum PassportDatum {
    BirthYear("byr"),
    IssueYear("iyr"),
    ExpirationYear("eyr"),
    Height("hgt"),
    HairColor("hcl"),
    EyeColor("ecl"),
    PassportID("pid"),
    CountryID("cid"),
    ;

    private final String shortCode;

    PassportDatum(String code) {
        this.shortCode = code;
    }

    public static PassportDatum fromString(String code) {
        for (PassportDatum d : PassportDatum.values()) {
            if (d.shortCode.equalsIgnoreCase(code)) {
                return d;
            }
        }
        throw new IllegalArgumentException(String.format("Key %s is not a valid PassportDatum.", code));
    }
}

class Passport {
    public EnumMap<PassportDatum, String> data = new EnumMap<>(PassportDatum.class);

    static final int MIN_HEIGHT_INCHES = 59;
    static final int MAX_HEIGHT_INCHES = 76;
    static final int MIN_HEIGHT_CM = 150;
    static final int MAX_HEIGHT_CM = 195;

    public static final List<PassportDatum> RequiredFields = new ArrayList<>(
            Arrays.asList(
                    PassportDatum.PassportID,
                    PassportDatum.BirthYear,
                    PassportDatum.ExpirationYear,
                    PassportDatum.IssueYear,
                    PassportDatum.EyeColor,
                    PassportDatum.HairColor,
                    PassportDatum.Height
            )
    );

    public static final List<String> VALID_COLORS = new ArrayList<>(
            Arrays.asList(
                    "amb",
                    "blu",
                    "brn",
                    "gry",
                    "grn",
                    "hzl",
                    "oth"
            )
    );

    public Passport(String passportData) {
        this.parseData(passportData);
    }

    private void parseData(String passportData) {
        String cleanedData = passportData.replaceAll("\n", " ").replaceAll("\\s{2,}", " ");
        String[] dataPairs = cleanedData.split(" ");

        for (String pair : dataPairs) {
            String[] keyVal = pair.split(":");
            try {
                this.data.put(PassportDatum.fromString(keyVal[0].trim()), keyVal[1].trim());
            } catch (IllegalArgumentException e) {
                System.out.printf("Invalid key: %s\n", keyVal[0]);
            }
        }
    }

    @Override
    public String toString() {
        return "Passport{" +
                "data=" + data +
                '}';
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isValidYear(String input, int min, int max) {
        try {
            int length = input.length();
            int value = Integer.parseInt(input);
            return length == 4 && value >= min && value <= max;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidHeight(String input) {
        try {
            String unit = input.substring(input.length() - 2);
            int height = Integer.parseInt(input.substring(0, input.length() - 2));

            if (unit.equals("in")) {
                return MIN_HEIGHT_INCHES <= height && height <= MAX_HEIGHT_INCHES;
            } else if (unit.equals("cm")) {
                return MIN_HEIGHT_CM <= height && height <= MAX_HEIGHT_CM;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    private boolean isValidHairColor(String input) {
        try {
            Long.parseLong(input.substring(1), 16);
        } catch (Exception e) {
            return false;
        }
        return input.startsWith("#") && input.length() - 1 == 6;
    }

    private boolean isValidEyeColor(String input) {
        return VALID_COLORS.contains(input);
    }

    private boolean isValidPassportId(String input) {
        try {
            Integer.parseInt(input);
            return (input.length() == 9);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean containsRequiredFields() {
        return data.keySet().containsAll(RequiredFields);
    }

    public boolean isValid() {
        try {
            for (Map.Entry<PassportDatum, String> entry : data.entrySet()) {
                String value = entry.getValue();
                switch (entry.getKey()) {
                    case Height:
                        if (!isValidHeight(value)) {
                            throw new Exception("Invalid height: " + value);
                        }
                        break;
                    case BirthYear:
                        if (!isValidYear(value, 1920, 2002)) {
                            throw new Exception("Invalid birth year: " + value);
                        }
                        break;
                    case IssueYear:
                        if (!isValidYear(value, 2010, 2020)) {
                            throw new Exception("Invalid issue year: " + value);
                        }
                        break;
                    case ExpirationYear:
                        if (!isValidYear(value, 2020, 2030)) {
                            throw new Exception("Invalid expiration year: " + value);
                        }
                        break;
                    case HairColor:
                        if (!isValidHairColor(value)) {
                            throw new Exception("Invalid hair color: " + value);
                        }
                        break;
                    case EyeColor:
                        if (!isValidEyeColor(value)) {
                            throw new Exception("Invalid eye color: " + value);
                        }
                        break;
                    case PassportID:
                        if (!isValidPassportId(value)) {
                            throw new Exception("Invalid passport ID: " + value);
                        }
                        break;
                }
            }

            if (!containsRequiredFields()) {
                throw new Exception("Does not have all required fields.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}

public class Day4 extends Day {
    public Day4() {
        inputFile = "days/Day4/input1.txt";
    }

    private List<Passport> getPassports(String input) {
        String[] passportsData = input.split("\n\n");
        return Arrays.stream(passportsData).map(Passport::new).collect(Collectors.toList());
    }

    public void part1() {
        String input = this.getInput();
        int validPassports = 0;
        int invalidPassports = 0;
        List<Passport> passports = getPassports(input);
        for (Passport passport : passports) {
            if (passport.containsRequiredFields()) {
                validPassports++;
            } else {
                invalidPassports++;
            }
        }

        System.out.printf("\nValid passports: %s\nInvalid passports: %s\n", validPassports, invalidPassports);
    }

    public void part2() {
        String input = this.getInput();
        int validPassports = 0;
        int invalidPassports = 0;
        List<Passport> passports = getPassports(input);
        for (Passport passport : passports) {
            if (passport.isValid()) {
                validPassports++;
            } else {
                invalidPassports++;
            }
        }

        System.out.printf("\nValid passports: %s\nInvalid passports: %s\n", validPassports, invalidPassports);
    }
}
