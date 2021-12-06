import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class LogIn {
    public static String username;
    public static String password;

    public static void main(String[] args) {
        Object teachOrStu;
        boolean login = false;
        String[] accountOptions = {"Student", "Teacher", "Exit"};
        do {
            do {
                teachOrStu = JOptionPane.showInputDialog(null,
                        "Are ypu a student or teacher?", "Brightspace",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        accountOptions, accountOptions[2]);
                if (teachOrStu == accountOptions[2]) {
                    JOptionPane.showConfirmDialog(null,
                            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                return;
                }
            } while (teachOrStu == accountOptions[2]);
            Object newOrLogin;
            String[] logInOptions = {"Create an Account", "Log In", "Exit"};
            do {
                newOrLogin = JOptionPane.showInputDialog(null,
                        "Would you like to create an account? \nOr log in to an existing one?", "Brightspace",
                        JOptionPane.INFORMATION_MESSAGE,null,
                        logInOptions, logInOptions[2]);
                if (newOrLogin == logInOptions[2]) {
                    JOptionPane.showConfirmDialog(null,
                            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                    return;
                }
            } while (newOrLogin == logInOptions[2]);
            if (newOrLogin == logInOptions[0]) {
                username = JOptionPane.showInputDialog(null, "Please enter a username.",
                        "Brightspace", JOptionPane.QUESTION_MESSAGE);
                password = JOptionPane.showInputDialog(null, "Please enter a password.",
                        "Brightspace", JOptionPane.QUESTION_MESSAGE);

                if (teachOrStu == accountOptions[1]) {
                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("TeacherLogins.txt", true)))) {
                        pw.write(username + ",");
                        pw.write(password);
                        pw.println();
                        pw.flush();
                    } catch (IOException e) {
                        JOptionPane.showConfirmDialog(null,
                                "Error occurred writing to file!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        e.printStackTrace();
                    }
                } else if (teachOrStu == accountOptions[0]){
                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("StudentLogins.txt", true)))) {
                        pw.write(username + ",");
                        pw.write(password);
                        pw.println();
                        pw.flush();
                    } catch (IOException e) {
                        JOptionPane.showConfirmDialog(null,
                                "Error occurred writing to file!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        e.printStackTrace();
                    }
                }

            } else {
                do {
                    Object enterUser = JOptionPane.showInputDialog(null, "Please enter your username.",
                            "Brightspace", JOptionPane.QUESTION_MESSAGE);
                    Object enterPass = JOptionPane.showInputDialog(null, "Please enter your password.",
                            "Brightspace", JOptionPane.QUESTION_MESSAGE);
                    if (teachOrStu == accountOptions[0]) {
                        ArrayList<String> lines = new ArrayList<>();
                        try (BufferedReader br = new BufferedReader(new FileReader("StudentLogins.txt"))) {
                            String line = br.readLine();
                            while (line != null) {
                                lines.add(line);
                                line = br.readLine();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < lines.size(); i++) {
                            if (lines.get(i).equals(enterUser + "," + enterPass)) {
                                username = lines.get(i).substring(0, lines.get(i).indexOf(","));
                                password = lines.get(i).substring(lines.get(i).indexOf(",") + 1, lines.get(i).length());
                            }
                        }

                    } else {
                        ArrayList<String> lines = new ArrayList<>();
                        try (BufferedReader br = new BufferedReader(new FileReader("TeacherLogins.txt"))) {
                            String line = br.readLine();
                            while (line != null) {
                                lines.add(line);
                                line = br.readLine();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < lines.size(); i++) {
                            if (lines.get(i).equals(enterUser + "," + enterPass)) {
                                username = lines.get(i).substring(0, lines.get(i).indexOf(","));
                                password = lines.get(i).substring(lines.get(i).indexOf(",") + 1, lines.get(i).length());
                            }
                        }
                    }

                    if (enterUser.equals(username) && (enterPass.equals(password))) {
                        login = true;
                        JOptionPane.showConfirmDialog(null,
                                "You have successfully logged into your account!\nPlease select OK to continue.",
                                "Brightspace", JOptionPane.DEFAULT_OPTION);
                    } else {
                        JOptionPane.showConfirmDialog(null,
                                "You have entered an incorrect username or password.\nPlease select OK to try again.",
                                "Brightspace", JOptionPane.DEFAULT_OPTION);
                    }
                } while (!login);
            }

            // Dashboard.java code

        } while (!login);
    }
}
