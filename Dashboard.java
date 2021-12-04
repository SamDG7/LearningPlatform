import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Dashboard {
    public static String username;
    public static String password;
    public static Object[] courses;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Object teachOrStu;
        boolean login = false;
        String[] accountOptions = {"Student", "Teacher", "Exit"};
        do {

            // LogIn.java code

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

                    /** if (teacherAction == teacherDash[0]) {
                        } else if (teacherAction == teacherDash[1]) {
                        } else if (teacherAction == teacherDash[2]) {
                        } else if (teacherAction == teacherDash[3]) {
                        } else **/

                    if (teacherAction == teacherDash[4]) {
                        JOptionPane.showConfirmDialog(null,
                        "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                } while (teacherAction != teacherDash[4]);
            }
        } while (!login);
    }
}
