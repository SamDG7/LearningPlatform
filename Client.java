import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Client {
    //fields
    public static String username;
    public static String password;
    public static String courseName;
    public static Object quizName;
    public static ArrayList<String> questionAnswers = new ArrayList<>();

    public static void main(String[] args) {
        String hostName;
        int portNum;
        Socket socket = null;
        //general welcome message
        do {
            JOptionPane.showMessageDialog(null, "Welcome!",
                    "Brightspace", JOptionPane.INFORMATION_MESSAGE);
            hostName = JOptionPane.showInputDialog(null,
                    "Please enter host name", "Brightspace", JOptionPane.QUESTION_MESSAGE);
            if (hostName == null) {
                JOptionPane.showConfirmDialog(null,
                        "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                return;
            }
        } while (hostName.equals(""));

        //port number is inputted and dialog persists until 4242 is inputted
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
        //try catch for port number that is not 4242
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
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Object teachOrStu;
            boolean login = false;
            String[] accountOptions = {"Student", "Teacher", "Exit"};
            //JOption that allows users to pick between student and teacher and exiting the program
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
            //picking 0=student
            if (teachOrStu == accountOptions[0]) {
                pw.write("Student");
                pw.println();
                pw.flush();
                System.out.println("written student");
                //picking 1=teacher
            } else if (teachOrStu == accountOptions[1]) {
                pw.write("Teacher");
                pw.println();
                pw.flush();
                System.out.println("written teacher");
            }

            //JOption pane that lets user decide to log into an account or create one
            Object newOrLogin;
            String[] logInOptions = {"Create an Account", "Log In", "Exit"};
            newOrLogin = JOptionPane.showInputDialog(null,
                    "Would you like to create an account? \nOr log in to an existing one?", "Brightspace",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    logInOptions, logInOptions[2]);
            //0=new account
            //entered usernames and passwords are stored
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
                //Account is created if username and password are not null
                if (br.readLine().equals("Success")) {
                    JOptionPane.showConfirmDialog(null,
                            "You have successfully created your account!\nPlease select OK and restart the program.",
                            "Brightspace", JOptionPane.DEFAULT_OPTION);
                    //Program quits if username/password are null and user needs to re-attempt to create account
                } else {
                    JOptionPane.showConfirmDialog(null,
                            "You have failed to create your account.\nPlease select OK to try again.",
                            "Brightspace", JOptionPane.DEFAULT_OPTION);
                    return;
                }
                //1=returning user
            } else if (newOrLogin == logInOptions[1]) {
                pw.write("returning");
                pw.println();
                pw.flush();
                System.out.println("written returning");
                //JOption pane for inputting username and password
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

                    //if inputted username and password is equal to stored username/password login is deemed successful
                    if (br.readLine().equals("Success")) {
                        login = true;
                        JOptionPane.showConfirmDialog(null,
                                "You have successfully logged into your account!\nPlease select OK to continue.",
                                "Brightspace", JOptionPane.DEFAULT_OPTION);
                        //if username or password do not match with stored info, login is deemed unsuccessful
                    } else {
                        JOptionPane.showConfirmDialog(null,
                                "You have entered an incorrect username or password.\nPlease select OK to try again.",
                                "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                } while (!login);
                //option 2=exiting the program
            } else if (newOrLogin == logInOptions[2]) {
                JOptionPane.showConfirmDialog(null,
                        "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                return;
            }
            //if login is successful on the student side, this code executes
            if (teachOrStu == accountOptions[0] && login) {
                Object studentAction;
                String[] studentDash = {"Take a quiz", "View quiz grades", "Exit"};
                //JOption pane for student dashboard allowing students to either take a quiz, view a quiz, or exit
                studentAction = JOptionPane.showInputDialog(null,
                        "Welcome! What would you like to do?", "Brightspace",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        studentDash, studentDash[2]);
                if (studentAction == null) {
                    JOptionPane.showConfirmDialog(null,
                            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                }
                pw.write((String) studentAction);
                pw.println();
                pw.flush();
                //if student picks take a quiz, file reader reads the stored list of courses in the courses file
                if (studentAction == studentDash[0]) {
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
                    }
                    //JOption pane for students to select which course they would like to get into
                    courseName = (String) JOptionPane.showInputDialog(null,
                            "Please select a course to access", "Brightspace",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            courses, courses.length);
                    //no input from the user results in the program ending
                    if (courseName == null) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    pw.write(courseName);
                    pw.println();
                    pw.flush();
                    System.out.println("Written course name to file");
                    ArrayList<String> temp = new ArrayList<>();
                    try {
                        temp = (ArrayList<String>) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    String[] quizNames = new String[temp.size()];
                    for (int i = 0; i < temp.size(); i++) {
                        quizNames[i] = temp.get(i);
                    }
                    //JOption pane for user to select a specific quiz in the entered course
                    quizName = JOptionPane.showInputDialog(null,
                            "Which quiz would you like to take?", "Brightspace",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            quizNames, quizNames.length);
                    //program ends if quizname is null
                    if (quizName == null) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    pw.write((String) quizName);
                    pw.println();
                    pw.flush();
                    System.out.println("sent quizname to server");
                    ArrayList<String> temp2 = new ArrayList<>();
                    try {
                        temp2 = (ArrayList<String>) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    String[] questions = new String[temp2.size()];
                    for (int i = 0; i < temp2.size(); i++) {
                        questions[i] = temp2.get(i);
                    }

                    System.out.println("got questions from server");
                    JFrame frame = new JFrame((String) quizName);
                    Container content = frame.getContentPane();
                    content.setLayout(new BorderLayout());
                    frame.setSize(600, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                    JPanel panel = new JPanel();
                    JPanel lowerPanel = new JPanel();
                    JButton submitButton = new JButton("Submit");
                    submitButton.addActionListener(new ActionListener() {
                        //method to record quiz answers on the student side
                        public void actionPerformed(ActionEvent e) {
                            frame.setVisible(false);
                            JOptionPane.showConfirmDialog(null,
                                    "Your answer have been recorded! Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                            try {
                                oos.writeObject(questionAnswers);
                            } catch (IOException IOE) {
                                IOE.printStackTrace();
                            }
                            System.out.println("Sent answers to server");
                        }
                    });

                    //answers get submitted to server
                    lowerPanel.add(submitButton);
                    content.add(lowerPanel, BorderLayout.SOUTH);

                    System.out.println(questions.length);
                    for (int i = 0; i < questions.length; i++) {
                        JTextField questionAnswer = new JTextField("", 5);
                        JLabel jLabel = new JLabel(questions[i]);
                        JButton saveAnswerButton = new JButton("Save Answer");
                        panel.add(jLabel);
                        panel.add(questionAnswer);
                        panel.add(saveAnswerButton);
                        saveAnswerButton.addActionListener(e -> {
                            String answer = questionAnswer.getText();
                            questionAnswers.add(answer);
                            System.out.println(answer);
                        });
                        content.add(panel);
                    }
                    System.out.println("question loop finished");
                }
                //if student picks view graded quizzes below code executes
                if (studentAction == studentDash[1]) {
                    ArrayList<String> courses = new ArrayList<>();
                    try {
                        courses = (ArrayList<String>) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    String[] coursesActual = new String[courses.size()];
                    for (int i = 0; i < courses.size(); i ++) {
                        coursesActual[i] = courses.get(i);
                    }
                    //JOption pane that allows student to select a course
                    courseName = (String) JOptionPane.showInputDialog(null,
                            "Please select a course to access", "Brightspace",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            coursesActual, coursesActual.length);
                    if (courseName == null) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    pw.write(courseName);
                    pw.println();
                    pw.flush();
                    System.out.println("Written course name to file");
                    ArrayList<String> temp = new ArrayList<>();
                    //try catch for if a course does not exist
                    try {
                        temp = (ArrayList<String>) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    String[] quizNames = new String[temp.size()];
                    for (int i = 0; i < temp.size(); i++) {
                        quizNames[i] = temp.get(i);
                    }
                    //JOption pane that displays to student their graded quizzes and scores that were from the teacher side
                    quizName = JOptionPane.showInputDialog(null,
                            "Below are your graded quizzes and scores", "Brightspace",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            quizNames, quizNames.length);
                    if (quizName == null) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    pw.write((String) quizName);
                    pw.println();
                    pw.flush();
                    System.out.println("sent quizname to server");
                    JOptionPane.showConfirmDialog(null,
                            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                    return;

                    //if student selects 2, program exits
                }
                if (studentAction == studentDash[2]) {
                    JOptionPane.showConfirmDialog(null,
                            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                }
                //if user is a teacher and login is successful dashboard appears
            }  else if (teachOrStu == accountOptions[1] && login) {
                Object teacherAction;
                String[] teacherDash = {"Create a course", "Edit a course", "Remove a course", "Grade A Quiz", "Exit"};
                //JOption pane for teachers to decide to create a course, edit a course, remove a course, grade a quiz, or exit
                teacherAction = JOptionPane.showInputDialog(null,
                        "Welcome! What would you like to do?", "Brightspace",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        teacherDash, teacherDash[3]);
                if (teacherAction == null) {
                    JOptionPane.showConfirmDialog(null,
                            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                    return;
                }
                pw.write((String) teacherAction);
                pw.println();
                pw.flush();
                // create a course
                if (teacherAction == teacherDash[0]) {
                    courseName = JOptionPane.showInputDialog(null, "Please enter the name of the new course",
                            "Brightspace", JOptionPane.QUESTION_MESSAGE);
                    if (courseName == null) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
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
                    if (editAction == null) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    pw.write(courseName);
                    pw.println();
                    pw.write((String) editAction);
                    pw.println();
                    pw.flush();
                    System.out.println("written course name and edit action");
                    if (editAction == editOptions[0]) {
                        String quizFile = JOptionPane.showInputDialog(null, "What is the name of the file you would like to upload as a quiz?",
                                "Brightspace", JOptionPane.QUESTION_MESSAGE);
                        if (quizFile == null) {
                            JOptionPane.showConfirmDialog(null,
                                    "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                            return;
                        }
                        String quizName = JOptionPane.showInputDialog(null, "What is the name of the quiz?",
                                "Brightspace", JOptionPane.QUESTION_MESSAGE);
                        if (quizName == null) {
                            JOptionPane.showConfirmDialog(null,
                                    "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                            return;
                        }
                        String questionNum = JOptionPane.showInputDialog(null, "How many questions is the quiz?",
                                "Brightspace", JOptionPane.QUESTION_MESSAGE);
                        if (questionNum == null) {
                            JOptionPane.showConfirmDialog(null,
                                    "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                            return;
                        }
                        String[] randomYN = {"Yes", "No", "Exit"};
                        Object temp = JOptionPane.showInputDialog(null,
                                "Would you like the question order to be randomized?", "Brightspace",
                                JOptionPane.INFORMATION_MESSAGE, null,
                                randomYN, randomYN[2]);
                        if (temp == null) {
                            JOptionPane.showConfirmDialog(null,
                                    "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                            return;
                        }

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
                            return;
                        }
                    }
                    if (editAction == editOptions[1]) {
                        ArrayList<String> quizzes = new ArrayList<>();
                        try (BufferedReader tbr = new BufferedReader(new FileReader("Quizzes.txt"))) {
                            String line;
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
                        if (quizName == null) {
                            JOptionPane.showConfirmDialog(null,
                                    "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                            return;
                        }
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
                            return;
                        }
                    }
                    if (editAction == editOptions[2]) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }

                }
                //remove course
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
                //grade quizzes from student side
                if (teacherAction == teacherDash[3]) {
                    ArrayList<String> courses = new ArrayList<>();
                    try {
                        courses = (ArrayList<String>) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    String[] coursesActual = new String[courses.size()];
                    for (int i = 0; i < courses.size(); i ++) {
                        coursesActual[i] = courses.get(i);
                    }
                    courseName = (String) JOptionPane.showInputDialog(null,
                            "Please select a course to access", "Brightspace",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            coursesActual, coursesActual.length);
                    if (courseName == null) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    pw.write(courseName);
                    pw.println();
                    pw.flush();
                    System.out.println("Written course name to file");
                    ArrayList<String> quizzesToGrade = new ArrayList<>();
                    try {
                        quizzesToGrade = (ArrayList<String>) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    String[] actualQuizzes = new String[quizzesToGrade.size()];
                    for (int i = 0; i < quizzesToGrade.size(); i ++) {
                        actualQuizzes[i] = quizzesToGrade.get(i);
                    }
                    quizName = JOptionPane.showInputDialog(null,
                            "Which quiz would you like to grade?", "Brightspace",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            actualQuizzes, actualQuizzes.length);
                    if (quizName == null) {
                        JOptionPane.showConfirmDialog(null,
                                "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    pw.write((String) quizName);
                    pw.println();
                    pw.flush();
                    ArrayList<String> quizQuestions = new ArrayList<>();
                    try {
                        quizQuestions = (ArrayList<String>) ois.readObject();
                    } catch (ClassNotFoundException cnfe) {
                        cnfe.printStackTrace();
                    }

                    ArrayList<String> studentAnswers = new ArrayList<>();
                    try {
                        studentAnswers = (ArrayList<String>) ois.readObject();
                    } catch (ClassNotFoundException cnfe) {
                        cnfe.printStackTrace();
                    }

                    JFrame frame = new JFrame((String) quizName);
                    Container content = frame.getContentPane();
                    content.setLayout(new BorderLayout());
                    frame.setSize(600, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setVisible(true);
                    JPanel panel = new JPanel();
                    JPanel lowerPanel = new JPanel();
                    JButton submitButton = new JButton("Submit");
                    JTextField numCorrect = new JTextField("", 5);
                    submitButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            frame.setVisible(false);
                            String correct = numCorrect.getText();
                            pw.write(correct);
                            pw.println();
                            pw.flush();
                        }
                    });


                    System.out.println(quizQuestions.size());
                    for (int i = 0; i < quizQuestions.size(); i++) {
                        JLabel jLabel = new JLabel(quizQuestions.get(i));
                        panel.add(jLabel);
                        JLabel jLabel2 = new JLabel(" ");
                        panel.add(jLabel2);
                        JLabel jLabel1 = new JLabel("The Student Answered: " + studentAnswers.get(i));
                        panel.add(jLabel1);
                        content.add(panel);
                    }
                    JLabel jLabel = new JLabel("Enter number correct: ");
                    lowerPanel.add(jLabel);
                    lowerPanel.add(numCorrect);
                    lowerPanel.add(submitButton);
                    content.add(lowerPanel, BorderLayout.SOUTH);
                    content.add(panel);
                    System.out.println("question loop finished");
                }
                //exit option
                if (teacherAction == teacherDash[4]) {
                    JOptionPane.showConfirmDialog(null,
                            "Thank you for using Brightspace!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                }
            }
            //error if connection is not established with server
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server side connection error");
        }
    }
}





