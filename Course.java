import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Course
 *
 * Determines dashboard and activity on course dashboard
 * @author Sravya Dandibhatta
 * @author Sam DeLucia-Green
 * @author Roan Finkle
 * @author Pooja Mathi
 * @author Sudhiksha Pudota
 *
 * @version November 15, 2021
 */

public class Course {
    public static String[] quizzes;
    public static Object quizName;
    public static String[] courses;
    public static Object courseName;


    public void getCourses() throws FileNotFoundException, IOException {
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
                    "Please select a course to access.", "Brightspace",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    courses, courses[lastCourse]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getQuizzes() throws FileNotFoundException, IOException {
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
                "Which quiz would you like to take?", "Brightspace",
                JOptionPane.INFORMATION_MESSAGE, null,
                quizzes, quizzes[lastQuiz]);

        String line = "";
        for (int i = 0; i < lines2.size(); i++) {
            if (lines2.get(i).contains(quizName) && lines2.get(i).contains(courseName)) {
                line = lines2.get(i);
            }
        }
        if (quizName != quizzes[lastQuiz]) {
            line = line.substring(line.indexOf(' ') + 1);
            String quizFile = line.substring(0, line.indexOf(' '));
            line = line.substring(line.indexOf(' ') + 1);
            line = line.substring(line.indexOf(' ') + 1);
            int questionNum = Integer.parseInt(line.substring(0, line.indexOf(' ')));
            line = line.substring(line.indexOf(' ') + 1);
            boolean bool = Boolean.parseBoolean(line);
            Quiz quiz = new Quiz(quizFile, questionNum, quizName, bool);
            quiz.runQuiz();
        } else if (quizName == quizzes[lastQuiz]) {
            JOptionPane.showConfirmDialog(null,
            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
        }
    }
}
