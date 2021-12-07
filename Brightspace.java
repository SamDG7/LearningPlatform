import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Brightspace {
    public static String username;
    public static String password;
    public static Object[] courses;
    public static Object courseName;
    public static String[] quizzes;
    public static Object quizName;

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
                        JOptionPane.INFORMATION_MESSAGE, null,
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
                } else if (teachOrStu == accountOptions[0]) {
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

            // student dashboard
            if (teachOrStu == accountOptions[0] && login) {
                // prints out list of courses
                try (BufferedReader br = new BufferedReader(new FileReader("Courses.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        courses[Integer.parseInt(line)] = line + ", ";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Object studentAction;
                String[] studentDash = {"Take a quiz", "View quiz grades", "Exit", "Access another course"};
                do {
                    Object courseName;
                    int lastCourse = courses.length + 1;
                    courses[lastCourse] = "Exit";
                    courseName = JOptionPane.showInputDialog(null,
                            "Please select a course.", "Brightspace",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            courses, courses[lastCourse]);
                    Course course = new Course(courseName);
                    do {
                        studentAction = JOptionPane.showInputDialog(null,
                                "Welcome! What would you like to do?", "Brightspace",
                                JOptionPane.INFORMATION_MESSAGE, null,
                                studentDash, studentDash[2]);
                        // prints out list of quizzes of the course
                        if (studentAction == studentDash[0]) {
                            try {
                                course.getQuizzes();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //view graded quizzes
                        } else if (studentAction == studentDash[1]) {
                            course.getGradedQuizzes();
                            // exits program
                        } else if (studentAction == studentDash[2]) {
                            JOptionPane.showConfirmDialog(null,
                                    "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                            return;
                        }
                    } while (studentAction != studentDash[2]);
                } while (studentAction != studentDash[3]);


                // teacher dashboard
            } else if (teachOrStu == accountOptions[1] && login) {
                Object teacherAction;
                String[] teacherDash = {"Create a course", "Edit a course", "Remove a course", "Access a course to view contents", "Exit"};
                do {
                    teacherAction = JOptionPane.showInputDialog(null,
                            "Welcome! What would you like to do?", "Brightspace",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            teacherDash, teacherDash[4]);

                    // create a course
                    if (teacherAction == teacherDash[0]) {
                        courseName = JOptionPane.showInputDialog(null, "Please enter the name of the new course",
                                "Brightspace", JOptionPane.QUESTION_MESSAGE);
                        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("Courses.txt", true)))) {
                            pw.write(courseName);
                            pw.println();
                            pw.flush();
                        } catch (IOException e) {
                            JOptionPane.showConfirmDialog(null,
                                    "Error occurred writing to file!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                            e.printStackTrace();
                        }

                        // edit a course
                    } else if (teacherAction == teacherDash[1]) {
                        Course course = new Course(courseName);
                        course.getCourses();

                        Object editAction;
                        String[] editOptions = {"Create a quiz", "Delete a quiz", "Exit"};
                        editAction = JOptionPane.showInputDialog(null,
                                "What would you like to do to the course?", "Brightspace",
                                JOptionPane.INFORMATION_MESSAGE, null,
                                editOptions, editOptions[2]);

                        // creates a quiz
                        if (editAction == editOptions[0]) {
                            Object quizFile = JOptionPane.showInputDialog(null, "What is the name of the file you would like to upload as a quiz?",
                                    "Brightspace", JOptionPane.QUESTION_MESSAGE);
                            Object quizName = JOptionPane.showInputDialog(null, "What is the name of the quiz?",
                                    "Brightspace", JOptionPane.QUESTION_MESSAGE);
                            Object questionNum = JOptionPane.showInputDialog(null, "How many questions is the quiz?",
                                    "Brightspace", JOptionPane.QUESTION_MESSAGE);
                            String[] randomYN = {"Yes", "No", "Exit"};
                            Object temp = JOptionPane.showInputDialog(null,
                                    "Would you like the question order to be randomized?", "Brightspace",
                                    JOptionPane.INFORMATION_MESSAGE, null,
                                    randomYN, randomYN[2]);
                            boolean random = false;
                            if (temp == randomYN[0]) {
                                random = true;
                            }
                            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("Quizzes.txt", true)))) {
                                if (quizName.contains(" ")) {
                                    quizName = quizName.replaceAll(" ", "_");
                                }
                                pw.write(courseName + " " + quizFile + " " + quizName + " " + questionNum + " " + random);
                                pw.println();
                                pw.flush();
                            } catch (IOException e) {
                                JOptionPane.showConfirmDialog(null,
                                        "Error occurred writing to file!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                                e.printStackTrace();
                            }

                            // deletes a quiz
                        } else if (editAction == editOptions[1]) {
                            ArrayList<String> lines2 = new ArrayList<>();
                            try (BufferedReader br = new BufferedReader(new FileReader("Quizzes.txt"))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    quizzes[Integer.parseInt(line)] = line + ", ";
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            int lastQuiz = quizzes.length + 1;
                            quizzes[lastQuiz] = "Exit";
                            quizName = JOptionPane.showInputDialog(null,
                                    "Which quiz would you like to delete?", "Brightspace",
                                    JOptionPane.INFORMATION_MESSAGE, null,
                                    quizzes, quizzes[lastQuiz]);
                            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("Quizzes.txt", false)))) {
                                for (int i = 0; i < lines2.size(); i++) {
                                    if (!(lines2.get(i).contains(quizName) && lines2.get(i).contains(courseName))) {
                                        pw.write(lines2.get(i));
                                        pw.flush();
                                    }
                                }
                            } catch (IOException e) {
                                JOptionPane.showConfirmDialog(null,
                                        "Error occurred writing to file!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                                e.printStackTrace();
                            }
                        }

                        // removes a course
                    } else if (teacherAction == teacherDash[2]) {
                        try (BufferedReader br = new BufferedReader(new FileReader("Courses.txt"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.contains(courseName)) {
                                    line = line.substring(line.indexOf(' ') + 1);
                                    line = line.substring((line.indexOf(' ') + 1));
                                    line = line.substring(0, line.indexOf(' '));
                                    courses[Integer.parseInt(line)] = line + ", ";
                                }
                            }
                            int lastCourse = courses.length + 1;
                            courses[lastCourse] = "Exit";
                            courseName = JOptionPane.showInputDialog(null,
                                    "Please select a course to remove", "Brightspace",
                                    JOptionPane.INFORMATION_MESSAGE, null,
                                    courses, courses[lastCourse]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ArrayList<String> lines = new ArrayList<>();
                        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("Courses.txt", false)))) {
                            for (int i = 0; i < lines.size(); i++) {
                                if (!(lines.get(i).equals(courseName))) {
                                    pw.write(lines.get(i));
                                    pw.flush();
                                }
                            }
                        } catch (IOException e) {
                            JOptionPane.showConfirmDialog(null,
                                    "Error occurred writing to file!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                            e.printStackTrace();
                        }
                        
                        // access a course
                    } else if (teacherAction == teacherDash[3]) {

                        // exit
                    } else if (teacherAction == teacherDash[4]) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                } while (teacherAction != teacherDash[4]);
            }
        } while (!login);
    }
}


