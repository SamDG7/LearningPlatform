// Sravya
import javax.swing.*;



public class MakeQuizGUI extends JFrame {
  
  public static void main(String[] args) {
        String quizName;
        String randomQuestion;
        String quizQuestions;

        showWelcomeMessageDialog();
        quizName = showQuizNameInputDialog();
        randomQuestion = showRandomInputDialog();
        quizQuestions = showQuestionInputDialog();
        showPrintingDetailsDialog();

    }

    public static void showWelcomeMessageDialog() {
        JOptionPane.showMessageDialog(null, "Welcome to Quiz Maker Program!",
                "Quiz Maker", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String showQuizNameInputDialog() {
        String quizName; 
        do {
            quizName = JOptionPane.showInputDialog(null, "What is the quiz name?",
                    "Quiz Maker", JOptionPane.QUESTION_MESSAGE);
            if ((quizName == null) || (quizName.isBlank())) {
                JOptionPane.showMessageDialog(null, "Quiz name cannot be empty!", "Quiz Maker",
                        JOptionPane.ERROR_MESSAGE);
    
            } //end if
        
        } while ((quizName == null) || (quizName.isBlank())); 
        
        return quizName;
    }


   public static String showRandomInputDialog() {
    	String randomQuestion; 
        do {
        	randomQuestion = JOptionPane.showInputDialog(null, "Do you want your quiz randomized?",
                    "Quiz Maker", JOptionPane.QUESTION_MESSAGE);
            if ((randomQuestion == null) || (randomQuestion.isBlank())) {
                JOptionPane.showMessageDialog(null, "Input cannot be empty!", "Quiz Maker",
                        JOptionPane.ERROR_MESSAGE);
    
            } //end if
        
        } while ((randomQuestion == null) || (randomQuestion.isBlank())); 
        
        return randomQuestion;
    }
    
  
  
  
}
