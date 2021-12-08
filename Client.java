import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    public static String username;
    public static String password;
    public static String courseName;
    public static Object quizName;

    public static void main(String[] args) {
        String hostName;
        int portNum;
        Socket socket = null;
        do {
            JOptionPane.showMessageDialog(null, "Welcome!",
                    "Brightspace", JOptionPane.INFORMATION_MESSAGE);
            hostName = JOptionPane.showInputDialog(null,
                    "Please enter host name", "Brightppace", JOptionPane.QUESTION_MESSAGE);
            if (hostName == null) {
                JOptionPane.showConfirmDialog(null,
                        "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                return;
            }
        } while (hostName.equals(""));


        do {
            try {
                portNum = Integer.parseInt(JOptionPane.showInputDialog(null,
                        "Please enter port number", "Brightspace", JOptionPane.QUESTION_MESSAGE));
            } catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null,
                        "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                return;
            }
        } while (portNum == -1);

        try {
            try {
                socket = new Socket(hostName, portNum);
                JOptionPane.showMessageDialog(null, "Connection Esatblished!",
                        "Brightspace", JOptionPane.INFORMATION_MESSAGE);

            } catch (ConnectException e) {
                JOptionPane.showMessageDialog(null, "Invalid hostname or port number",
                        "Brightspace", JOptionPane.ERROR_MESSAGE);
            }
            assert socket != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            Object teachOrStu;
            boolean login = false;
            String[] accountOptions = {"Student", "Teacher", "Exit"};
            do {
                teachOrStu = JOptionPane.showInputDialog(null,
                        "Are you a student or teacher?", "Brightspace",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        accountOptions, accountOptions[2]);
                if (teachOrStu == accountOptions[2]) {
                    JOptionPane.showConfirmDialog(null,
                            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                    return;
                }
            } while (teachOrStu == accountOptions[2]);
            if (teachOrStu == accountOptions[0]) {
                pw.write("Student");
                pw.println();
                pw.flush();
                System.out.println("written student");
            } else if (teachOrStu == accountOptions[1]) {
                pw.write("Teacher");
                pw.println();
                pw.flush();
                System.out.println("written teacher");
            }

            Object newOrLogin;
            String[] logInOptions = {"Create an Account", "Log In", "Exit"};
            newOrLogin = JOptionPane.showInputDialog(null,
                    "Would you like to create an account? \nOr log in to an existing one?", "Brightspace",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    logInOptions, logInOptions[2]);
            if (newOrLogin == logInOptions[0]) {
                pw.write("new");
                pw.println();
                pw.flush();
                System.out.println("written new");
                username = JOptionPane.showInputDialog(null, "Please enter a username.",
                        "Brightspace", JOptionPane.QUESTION_MESSAGE);
                password = JOptionPane.showInputDialog(null, "Please enter a password.",
                        "Brightspace", JOptionPane.QUESTION_MESSAGE);
                pw.write(username);
                pw.println();
                pw.write(password);
                pw.println();
                pw.flush();

                if (br.readLine().equals("Success")) {
                    JOptionPane.showConfirmDialog(null,
                            "You have successfully created your account!\nPlease select OK and restart the program.",
                            "Brightspace", JOptionPane.DEFAULT_OPTION);
                } else {
                    JOptionPane.showConfirmDialog(null,
                            "You have failed to create your account.\nPlease select OK to try again.",
                            "Brightspace", JOptionPane.DEFAULT_OPTION);
                }

            } else if (newOrLogin == logInOptions[1]) {
                pw.write("returning");
                pw.println();
                pw.flush();
                System.out.println("written returning");
                do {
                    String enterUser = JOptionPane.showInputDialog(null, "Please enter your username.",
                            "Brightspace", JOptionPane.QUESTION_MESSAGE);
                    String enterPass = JOptionPane.showInputDialog(null, "Please enter your password.",
                            "Brightspace", JOptionPane.QUESTION_MESSAGE);
                    pw.write(enterUser);
                    pw.println();
                    pw.write(enterPass);
                    pw.println();
                    pw.flush();
                    System.out.println("Written username and password");

                    if (br.readLine().equals("Success")) {
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
            } else if (newOrLogin == logInOptions[2]) {
                JOptionPane.showConfirmDialog(null,
                        "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                return;
            }

            if (teachOrStu == accountOptions[0] && login) {
                Object studentAction;
                String[] studentDash = {"Take a quiz", "View quiz grades", "Exit"};
                studentAction = JOptionPane.showInputDialog(null,
                        "Welcome! What would you like to do?", "Brightspace",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        studentDash, studentDash[2]);
                pw.write((String) studentAction);
                pw.println();
                pw.flush();
                if (studentAction == studentDash[0]) {

                }
                if (studentAction == studentDash[1]) {

                }
                if (studentAction == studentDash[2]) {
                    JOptionPane.showConfirmDialog(null,
                            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                }
            }  else if (teachOrStu == accountOptions[1] && login) {
                Object teacherAction;
                String[] teacherDash = {"Create a course", "Edit a course", "Remove a course", "Exit"};
                teacherAction = JOptionPane.showInputDialog(null,
                        "Welcome! What would you like to do?", "Brightspace",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        teacherDash, teacherDash[3]);
                pw.write((String) teacherAction);
                pw.println();
                pw.flush();
                // create a course
                if (teacherAction == teacherDash[0]) {
                    courseName = JOptionPane.showInputDialog(null, "Please enter the name of the new course",
                            "Brightspace", JOptionPane.QUESTION_MESSAGE);
                    courseName = courseName.replaceAll(" ", "_");
                    pw.write(courseName);
                    pw.println();
                    pw.flush();
                    if (br.readLine().equals("Success")) {
                        JOptionPane.showConfirmDialog(null,
                                "You have successfully created a new course!\nPlease select OK and restart the program.",
                                "Brightspace", JOptionPane.DEFAULT_OPTION);
                    } else {
                        JOptionPane.showConfirmDialog(null,
                                "An error occured while creating the course.\nPlease select OK to try again.",
                                "Brightspace", JOptionPane.DEFAULT_OPTION);
                    }
                }
                // edit a course
                if (teacherAction == teacherDash[1]) {
                    String courseName = JOptionPane.showInputDialog(null, "Please enter the name of the course you would like to edit.",
                            "Brightspace", JOptionPane.QUESTION_MESSAGE);
                    Object editAction;
                    String[] editOptions = {"Create a quiz", "Delete a quiz", "Exit"};
                    editAction = JOptionPane.showInputDialog(null,
                            "What would you like to do to the course?", "Brightspace",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            editOptions, editOptions[2]);
                    pw.write(courseName);
                    pw.println();
                    pw.write((String) editAction);
                    pw.println();
                    pw.flush();
                    System.out.println("written course name and edit action");
                    if (editAction == editOptions[0]) {
                        String quizFile = JOptionPane.showInputDialog(null, "What is the name of the file you would like to upload as a quiz?",
                                "Brightspace", JOptionPane.QUESTION_MESSAGE);
                        String quizName = JOptionPane.showInputDialog(null, "What is the name of the quiz?",
                                "Brightspace", JOptionPane.QUESTION_MESSAGE);
                        String questionNum = JOptionPane.showInputDialog(null, "How many questions is the quiz?",
                                "Brightspace", JOptionPane.QUESTION_MESSAGE);
                        String[] randomYN = {"Yes", "No", "Exit"};
                        Object temp = JOptionPane.showInputDialog(null,
                                "Would you like the question order to be randomized?", "Brightspace",
                                JOptionPane.INFORMATION_MESSAGE, null,
                                randomYN, randomYN[2]);
                        boolean random = temp == randomYN[0];
                        pw.write(quizFile);
                        pw.println();
                        pw.write(quizName);
                        pw.println();
                        pw.write(questionNum);
                        pw.println();
                        pw.write(String.valueOf(random));
                        pw.println();
                        pw.flush();
                        System.out.println("written all quiz info");
                        if (br.readLine().equals("W")) {
                            System.out.println("inside success for making quiz");
                            JOptionPane.showConfirmDialog(null,
                                    "You have successfully created a quiz!\nPlease select OK to continue.",
                                    "Brightspace", JOptionPane.DEFAULT_OPTION);
                        } else {
                            JOptionPane.showConfirmDialog(null,
                                    "An Error has occured while creating the quiz.\nPlease select OK to try again.",
                                    "Brightspace", JOptionPane.DEFAULT_OPTION);
                        }
                    }
                    if (editAction == editOptions[1]) {
                        ArrayList<String> quizzes = new ArrayList<>();
                        try (BufferedReader tbr = new BufferedReader(new FileReader("Quizzes.txt"))) {
                            String line;
                            System.out.println("Quizzes: ");
                            while ((line = tbr.readLine()) != null) {
                                line = line.substring(line.indexOf(' ') + 1);
                                line = line.substring((line.indexOf(' ') + 1));
                                line = line.substring(0, line.indexOf(' '));
                                quizzes.add(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String[] actualQuizzes = new String[quizzes.size()];
                        for (int i = 0; i < quizzes.size(); i++) {
                            actualQuizzes[i] = quizzes.get(i);
                        }
                        quizName = JOptionPane.showInputDialog(null,
                                "Which quiz would you like to delete?", "Brightspace",
                                JOptionPane.INFORMATION_MESSAGE, null,
                                actualQuizzes, actualQuizzes.length);
                        pw.write((String) quizName);
                        pw.println();
                        pw.flush();
                        System.out.println("sent quiz name to server");
                        if (br.readLine().equals("Success")) {
                            JOptionPane.showConfirmDialog(null,
                                    "You have successfully deleted a quiz!\nPlease select OK to continue.",
                                    "Brightspace", JOptionPane.DEFAULT_OPTION);
                        } else {
                            JOptionPane.showConfirmDialog(null,
                                    "An error occurred while deleting your quiz.\nPlease select OK to try again.",
                                    "Brightspace", JOptionPane.DEFAULT_OPTION);
                        }
                    }
                    if (editAction == editOptions[2]) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }

                }
                if (teacherAction == teacherDash[2]) {
                    ArrayList<String> lines = new ArrayList<>();
                    try (BufferedReader tbr = new BufferedReader(new FileReader("Courses.txt"))) {
                        String line;
                        while ((line = tbr.readLine()) != null) {
                            lines.add(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error");
                    }
                    String[] courses = new String[lines.size()];
                    for (int i = 0; i < lines.size(); i ++) {
                        courses[i] = lines.get(i);
                        courseName = (String) JOptionPane.showInputDialog(null,
                                "Please select a course to remove", "Brightspace",
                                JOptionPane.INFORMATION_MESSAGE, null,
                                courses, courses.length);
                        pw.write(courseName);
                        pw.println();
                        pw.flush();
                        System.out.println("Written course name to file");
                        if (br.readLine().equals("Success")) {
                            JOptionPane.showConfirmDialog(null,
                                    "You have successfully deleted a course!\nPlease select OK to continue.",
                                    "Brightspace", JOptionPane.DEFAULT_OPTION);
                        } else {
                            JOptionPane.showConfirmDialog(null,
                                    "There was an error deleting a course.\nPlease select OK to try again.",
                                    "Brightspace", JOptionPane.DEFAULT_OPTION);
                        }
                    }
                }
                if (teacherAction == teacherDash[3]) {
                    JOptionPane.showConfirmDialog(null,
                            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server side connection error");
        }
    }
}
