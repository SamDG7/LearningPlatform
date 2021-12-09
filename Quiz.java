import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

/**
 * Quiz
 *
 * Determines order of quiz, runs quiz, grades quiz
 * @author Sravya Dandibhatta
 * @author Sam DeLucia-Green
 * @author Roan Finkle
 * @author Pooja Mathi
 * @author Sudhiksha Pudota
 *
 * @version November 15, 2021
 */

/**
 * Outline for quiz file format:
 *
 * Question
 * 1. choice
 * 2. choice
 * C3. choice (C denotes the answer being correct)
 * 4. choice
 */

public class Quiz {
    /**
     * The number of questions in the quiz.
     */
    private final int numQuestions;

    /**
     * The number of answers for each question in the quiz.
     */
    private final int numAnswers = 4;

    /**
     * The name of the file that contains the quiz.
     */
    private final String filename;

    /**
     * The name of the quiz.
     */
    private final String quizName;

    /**
     * The ArrayList that stores answers entered by the student
     */
    public ArrayList<Integer> storedAnswers = new ArrayList<>();

    /**
     * The ArrayList that stores the correct answers.
     */
    public ArrayList<Integer> correctAnswers = new ArrayList<>();

    /**
     * Determined by the order intended by the teacher (randomized or not)
     */
    private boolean random;

    /**
     * Constructs a newly allocated {@code Quiz} object with the file name, the number of questions, quizname and order.
     *
     * @param filename
     * @param numQuestions
     * @param quizName
     * @param random
     */
    public Quiz(String filename, int numQuestions, String quizName, boolean random) {
        this.filename = filename;
        this.numQuestions = numQuestions;
        this.quizName = quizName;
        this.random = random;
    }

    /**
     * Converts the file to an ArrayList
     * @return
     * @throws IOException
     */
    public ArrayList<String> readFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Two dimensional array that helps generate questions in a randomized order.
     * @return
     */
    public String[][] randomizeOrder() {
        try {
            ArrayList<String> enteredOrder = readFile();
            String[][] randomOrder = new String[numQuestions][5];
            for (int i = 0; i < numQuestions; i++) {
                for (int j = 0; j < 5; j++) {
                    int randomNum;
                    do {
                        randomNum = (int) (Math.random() * numQuestions);
                    } while (!(randomNum % 5 == 0) && enteredOrder.get(randomNum) != null);
                    randomOrder[i][j] = enteredOrder.get(randomNum);
                }
            }
            return randomOrder;
        } catch (IOException e) {
            System.out.println("Error reading file!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Answers added to storedAnswers ArrayList
     * @param answer
     */
    public void storeAnswer(int answer) {
        storedAnswers.add(answer);
    }

    /**
     * Correct answers added to correctAnswers ArrayList
     * @param answer
     */
    public void addCorrectAnswer(int answer) {
        correctAnswers.add(answer);
    }

    /**
     * Runs the quiz
     */
    public void runQuiz() {
        Scanner scanner = new Scanner(System.in);
        /**
         * Runs quiz in static order (not randomized)
         */
        if (!random) {
            try {
                JFrame jframe = new JFrame(quizName);
                Container content = jframe.getContentPane();
                content.setLayout(new BorderLayout());
                for (int i = 0; i < readFile().size(); i += 5) {
                    System.out.println(readFile().get(i));
                    for (int j = 1; j < 5; j++) {
                        if (readFile().get(i + j).charAt(0) == 'C') {
                            addCorrectAnswer(Integer.parseInt(String.valueOf(readFile().get(i + j).charAt(1))));
                            String temp = readFile().get(i + j).replaceAll("C", "");
                            System.out.println(temp);
                        } else {
                            System.out.println(readFile().get(i + j));
                        }
                    }


                    System.out.println("Would you like to attach a file to respond to this question?:\n1: Yes \n2: No ");
                    /**
                     * Input value for whether user wants to attach a file
                     */
                    int attachFile = scanner.nextInt();
                    scanner.nextLine();
                    if (attachFile == 1) {
                        // do something to attach the file
                    } else {
                        System.out.println("What is your answer to the question?");
                        /**
                         * Input value for answer
                         */
                        int answer = scanner.nextInt();
                        scanner.nextLine();
                        /**
                         * storeAnswer method invoked to store the answer entered into ArrayList
                         */
                        storeAnswer(answer);
                    }
                }
                System.out.println("Would you like to submit this quiz? \n" + "1: Yes\n" + "2: No");
                /**
                 * Input value for whether user wants to submit the quiz
                 */
                int submit = scanner.nextInt();
                /**
                 * gradeQuiz() method invoked to send answers to be graded
                 */
                if (submit == 1) {
                    gradeQuiz();
                } else {
                    return;
                }
                Date date = new Date();

                /**
                 * Generate time stamp
                 */
                Timestamp ts=new Timestamp(date.getTime());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println(formatter.format(ts));
            } catch (IOException e) {
                System.out.println("Error reading from file");
            }
        }
        /**
         * Runs quiz in randomized order
         */
        else {
            System.out.println(quizName);
            for (int i = 0; i < randomizeOrder().length; i += 5) {
                System.out.println(randomizeOrder()[i]);
                for (int j = 1; j < 5; j++) {
                    if (randomizeOrder()[i][j].charAt(0) == 'C') {
                        addCorrectAnswer(Integer.parseInt(String.valueOf(randomizeOrder()[i][j].charAt(1))));
                        String temp = randomizeOrder()[i][j].replaceAll("C", "");
                        System.out.println(temp);
                    } else {
                        System.out.println(randomizeOrder()[i][j]);
                    }
                }
                System.out.println("Would you like to attach a file to respond to this question?:\n1: Yes \n2: No ");
                /**
                 * Input value for whether user wants to attach file
                 */
                int attachFile = scanner.nextInt();
                scanner.nextLine();
                /**
                 * User can attach file
                 */
                if (attachFile == 1) {
                    System.out.println("What is the name of the file you want to attach?");
                    String attachedFileName = scanner.nextLine();

                }
                /**
                 * Answers stored in ArrayList after storeAnswer method is invoked
                 */
                else {
                    int answer = scanner.nextInt();
                    scanner.nextLine();
                    storeAnswer(answer);
                }
            }
            System.out.println("Would you like to submit this quiz? \n" + "1: Yes + \n" + "2: No");
            /**
             * Input value for submission
             */
            int submit = scanner.nextInt();
            /**
             * Submitted answers send for grading after gradeQuiz method is invoked
             */
            if (submit == 1) {
                gradeQuiz();
            } else {
                return;
            }
            Date date = new Date();
            Timestamp ts=new Timestamp(date.getTime());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(formatter.format(ts));
        }
    }

    /**
     * Grades quiz (for teachers)
     */
    public void gradeQuiz () {
        /**
         * The number of correct answers in the quiz.
         */
        int totalCorrectAnswers = 0;
        /**
         * Checks if answer entered matches with the correct answer.
         * If true, totalCorrectAnswers is incremented
         */
        for (int i = 0; i < storedAnswers.size(); i++) {
            if (Objects.equals(storedAnswers.get(i), correctAnswers.get(i))) {
                totalCorrectAnswers++;
            }
        }
        System.out.println("You got " + totalCorrectAnswers + " correct!");
        System.out.println("That's is a score of: " + ((totalCorrectAnswers / numQuestions)* 100) + "%");
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("GradedQuizzes.txt", true)))) {
            pw.write(quizName + " " +  ((totalCorrectAnswers / numQuestions) * 100));
            pw.println();
            pw.flush();
        } catch (IOException e) {
            System.out.println("Error occurred writing to file!");
            e.printStackTrace();
        }
    }


}
