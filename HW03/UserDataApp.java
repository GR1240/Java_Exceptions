import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InvalidDataFormatException extends Exception {
    public InvalidDataFormatException(String message) {
        super(message);
    }
}

class UserData {
    private String lastName;
    private String firstName;
    private String middleName;
    private String birthDate;
    private String phoneNumber;
    private char gender;

    public UserData(String lastName, String firstName, String middleName, String birthDate, String phoneNumber, char gender) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public String toString() {
        return lastName + " " + firstName + " " + middleName + " " + birthDate + " " + phoneNumber + " " + gender;
    }
}

public class UserDataApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные в формате: Фамилия Имя Отчество Дата_рождения Номер_телефона Пол");
        String input = scanner.nextLine();

        String[] data = input.split(" ");
        try {
            UserData userData = parseUserData(data);
            saveUserDataToFile(userData);
            System.out.println("Данные успешно сохранены в файл.");
        } catch (InvalidDataFormatException | IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static UserData parseUserData(String[] data) throws InvalidDataFormatException {
        if (data.length != 6) {
            throw new InvalidDataFormatException("Неверное количество данных");
        }

        String lastName = data[0];
        String firstName = data[1];
        String middleName = data[2];
        String birthDate = data[3];
        String phoneNumber = data[4];
        char gender = data[5].charAt(0);

        // Проверка формата даты рождения
        if (!isValidDateFormat(birthDate)) {
            throw new InvalidDataFormatException("Неверный формат даты рождения");
        }

        // Проверка формата номера телефона
        if (!isValidPhoneNumberFormat(phoneNumber)) {
            throw new InvalidDataFormatException("Неверный формат номера телефона");
        }

        // Проверка формата пола
        if (gender != 'm' && gender != 'f') {
            throw new InvalidDataFormatException("Неверный формат пола");
        }

        return new UserData(lastName, firstName, middleName, birthDate, phoneNumber, gender);
    }

    private static boolean isValidDateFormat(String birthDate) {
        String regex = "^\\d{2}\\.\\d{2}\\.\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(birthDate);
        return matcher.matches();
    }

    private static boolean isValidPhoneNumberFormat(String phoneNumber) {
        String regex = "^\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private static void saveUserDataToFile(UserData userData) throws IOException {
        String fileName = userData.getLastName() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(userData.toString());
        }
    }
}
