package prog5121;

public class login {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    private static String storedUsername;
    private static String storedPassword;
    private static String storedFirstName;
    private static String storedLastName;

    public login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public boolean checkUserName() {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))      hasUpper = true;
            if (Character.isDigit(c))          hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasUpper && hasDigit && hasSpecial;
    }

    public boolean checkCellPhoneNumber() {
        if (cellPhoneNumber == null) return false;
        return cellPhoneNumber.matches("^\\+[0-9]{9,12}$");
    }

    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code; please correct the number and try again.";
        }
        storedUsername = this.username;
        storedPassword = this.password;
        storedFirstName = this.firstName;
        storedLastName = this.lastName;
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return storedUsername != null && storedPassword != null
                && storedUsername.equals(enteredUsername)
                && storedPassword.equals(enteredPassword);
    }

    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + storedFirstName + " " + storedLastName + ", it is great to see you.";
        }
        return "Username or password incorrect, please try again.";
    }

    public static void resetStoredUser() {
        storedUsername = null;
        storedPassword = null;
        storedFirstName = null;
        storedLastName = null;
    }
}