import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TakeQuizGUI extends JFrame {
    public static Object courseName;
    public static Object quizName;
    public static String quizAnswers;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        showCourseDialog();
        showQuizDialog();
        quizAnswers=showQuizAnswerDialog();
        showPrintingDetailsDialog();

    }

    public static void showCourseDialog() throws FileNotFoundException, IOException {
        Course course = new Course(courseName);
        course.getCourses();
    }

    public static void showQuizDialog() throws FileNotFoundException, IOException {
        Course course = new Course(quizName);
        course.getQuizzes();


        // not sure if necessary
        String quizName;
        do {
            quizName=JOptionPane.showInputDialog(null, "Which quiz would you like to take?",
                    "Quiz Taker", JOptionPane.QUESTION_MESSAGE);
            if(quizName==null||quizName.isBlank()){
                JOptionPane.showMessageDialog(null, "Pick a valid quiz","Quiz Taker"
                        ,JOptionPane.ERROR_MESSAGE);
            }else{
                //method used in proj 4
            }
        } while(quizName == null||quizName.isBlank());
        // *****

    }
    public static String showQuizAnswerDialog() {
        String quizAnswers;
        do{
            quizAnswers=JOptionPane.showInputDialog(null,"Attach your answer file: "
                    ,"Quiz Taker",JOptionPane.QUESTION_MESSAGE);
            if(quizAnswers==null){
                JOptionPane.showMessageDialog(null,"Answer file cannot be empty!"
                        ,"Quiz Taker",JOptionPane.ERROR_MESSAGE);
            } else {
                //method used in proj 4
            }
        } while(quizAnswers == null);
        return quizAnswers;
    }
    public static void showPrintingDetailsDialog() {
        JOptionPane.showMessageDialog(null,"Saving your Details...","Quiz Taker"
                ,JOptionPane.INFORMATION_MESSAGE);
    }

}
