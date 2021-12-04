import javax.swing.*;

public class TakeQuizGUI extends JFrame {
    public static void main(String[] args){
        String quizName;
        String quizAnswers;
        
        showWelcomeMessageDialog();
        quizName=showQuizDialog();
        quizAnswers=showQuizAnswerDialog();
        showPrintingDetailsDialog();

    }
    public static void showWelcomeMessageDialog(){
        JOptionPane.showMessageDialog(null,"Welcome to the Quiz Taker program",
                "QuizTaker",JOptionPane.INFORMATION_MESSAGE);
    }
    public static String showQuizDialog(){
        String quizName;
        do{
            quizName=JOptionPane.showInputDialog(null, "Which quiz would you like to take?",
                    "Quiz Taker", JOptionPane.QUESTION_MESSAGE);
            if(quizName==null||quizName.isBlank()){
                JOptionPane.showMessageDialog(null, "Pick a valid quiz","Quiz Taker"
                        ,JOptionPane.ERROR_MESSAGE);
            }else{
                //method used in proj 4
            }
        }while(quizName==null||quizName.isBlank());
        return quizName;
    }
    public static String showQuizAnswerDialog(){
        String quizAnswers;
        do{
            quizAnswers=JOptionPane.showInputDialog(null,"Attach your answer file: "
            ,"Quiz Taker",JOptionPane.QUESTION_MESSAGE);
            if(quizAnswers==null){
                JOptionPane.showMessageDialog(null,"Answer file cannot be empty!"
                ,"Quiz Taker",JOptionPane.ERROR_MESSAGE);
            }else{
                //method used in proj 4
            }
        }while(quizAnswers==null);
        return quizAnswers;
    }
    public static void showPrintingDetailsDialog(){
        JOptionPane.showMessageDialog(null,"Saving your Details...","Quiz Taker"
        ,JOptionPane.INFORMATION_MESSAGE);
    }

}
