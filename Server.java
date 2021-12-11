import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class Server extends Thread {
    private static Socket socket;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            int counter = 0;
            while (true) {
                counter++;
                socket = serverSocket.accept();
                Server server = new Server();
                Thread clientThread = new Thread(server);
                clientThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String teacherOrStudent = br.readLine();
            boolean newAccount = false;
            do {
                String logOrSin = br.readLine();


                if (logOrSin.equals("new")) {
                    System.out.println("create an account attempt");
                    String username = br.readLine();
                    String password = br.readLine();
                    newAccount = Boolean.parseBoolean(br.readLine());
                    System.out.println("recieved " + newAccount);
                    if (teacherOrStudent.equals("Student")) {
                        writeToFile("StudentLogins.txt", true, username + "," + password);
                    } else {
                        writeToFile("TeacherLogins.txt", true, username + "," + password);
                    }
                    pw.write("Success");
                    pw.println();
                    pw.flush();
                    System.out.println("account created");
                } else if (logOrSin.equals("returning")) {
                    System.out.println("inside returning code");
                    String username = "";
                    String password = "";
                    String enterUser = br.readLine();
                    String enterPass = br.readLine();
                    newAccount = Boolean.parseBoolean(br.readLine());
                    System.out.println("recieved " + newAccount);
                    if (teacherOrStudent.equals("Student")) {
                        ArrayList<String> lines = readFromFile("StudentLogins.txt");
                        for (int i = 0; i < lines.size(); i++) {
                            if (lines.get(i).contains(enterUser) && lines.get(i).contains(enterPass)) {
                                username = enterUser;
                                password = enterPass;
                                break;
                            }
                        }
                        if (username.equals(enterUser) && password.equals(enterPass)) {
                            pw.write("Success");
                            pw.println();
                            pw.flush();
                        } else {
                            pw.write("Failed");
                            pw.println();
                            pw.flush();
                        }
                        String studentDash = br.readLine();
                        if (studentDash.equals("Take a quiz")) {
                            String courseName = br.readLine();
                            Course course = new Course(courseName);
                            ArrayList<String> quizNames = course.getQuizzes();
                            oos.writeObject(quizNames);
                            String quizName = br.readLine();
                            ArrayList<String> quizzes = readFromFile("Quizzes.txt");
                            String line = "";
                            for (int i = 0; i < quizzes.size(); i++) {
                                if (quizzes.get(i).contains(quizName) && quizzes.get(i).contains(courseName)) {
                                    line = quizzes.get(i);
                                }
                            }
                            line = line.substring(line.indexOf(' ') + 1);
                            String quizFile = line.substring(0, line.indexOf(' '));
                            line = line.substring(line.indexOf(' ') + 1);
                            line = line.substring(line.indexOf(' ') + 1);
                            int questionNum = Integer.parseInt(line.substring(0, line.indexOf(' ')));
                            line = line.substring(line.indexOf(' ') + 1);
                            boolean bool = Boolean.parseBoolean(line);
                            Quiz quiz = new Quiz(quizFile, questionNum, quizName, bool);
                            ArrayList<String> questions = quiz.readFile();
                            if (bool) {
                                Collections.shuffle(questions);
                            }
                            oos.writeObject(questions);
                            oos.flush();
                            ArrayList<String> questionAnswers = (ArrayList<String>) ois.readObject();
                            try (PrintWriter tpw = new PrintWriter(new BufferedWriter(new FileWriter("GradedQuizzes.txt", true)))) {
                                tpw.write(courseName + " " + quizName + " " + quizFile + " " + username + " ");
                                for (int i = 0; i < questionAnswers.size(); i++) {
                                    if (i != questionAnswers.size() - 1) {
                                        tpw.write(questionAnswers.get(i) + " ");
                                    } else {
                                        tpw.write(questionAnswers.get(i));
                                    }
                                }
                                tpw.println();
                                tpw.flush();
                            } catch (IOException e) {
                                JOptionPane.showConfirmDialog(null,
                                        "Error occurred writing to file!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                                e.printStackTrace();
                            }


                        }
                        if (studentDash.equals("View quiz grades")) {
                            ArrayList<String> courses = readFromFile("Courses.txt");
                            oos.writeObject(courses);
                            String courseName = br.readLine();
                            Course course = new Course(courseName);
                            ArrayList<String> gradedQuizzes = readFromFile("GradedByTeacher.txt");
                            oos.writeObject(gradedQuizzes);
                            oos.flush();
                        }
                    } else if (teacherOrStudent.equals("Teacher")) {
                        ArrayList<String> lines = readFromFile("TeacherLogins.txt");
                        for (int i = 0; i < lines.size(); i++) {
                            if (lines.get(i).contains(enterUser) && lines.get(i).contains(enterPass)) {
                                username = enterUser;
                                password = enterPass;
                                break;
                            }
                        }
                        if (username.equals(enterUser) && password.equals(enterPass)) {
                            pw.write("Success");
                            pw.println();
                            pw.flush();
                        } else {
                            pw.write("Failed");
                            pw.println();
                            pw.flush();
                        }
                        String teacherDash = br.readLine();
                        if (teacherDash.equals("Create a course")) {
                            String courseName = br.readLine();
                            writeToFile("Courses.txt", true, courseName);
                            pw.write("Success");
                            pw.println();
                            pw.flush();
                        }
                        if (teacherDash.equals("Edit a course")) {
                            String courseName = br.readLine();
                            String editAction = br.readLine();
                            if (editAction.equals("Create a quiz")) {
                                String quizFile = br.readLine();
                                String quizName = br.readLine();
                                String questionNum = br.readLine();
                                String random = br.readLine();
                                if (quizName.contains(" ")) {
                                    quizName = quizName.replaceAll(" ", "_");
                                }
                                writeToFile("Quizzes.txt", true, courseName + " " + quizFile + " " + quizName + " " + questionNum + " " + random);

                                pw.write("W");
                                pw.println();
                                pw.flush();
                            }
                            if (editAction.equals("Delete a quiz")) {
                                String quizName = br.readLine();
                                ArrayList<String> lines2 = readFromFile("Quizzes.txt");
                                try (PrintWriter tpw = new PrintWriter(new BufferedWriter(new FileWriter("Quizzes.txt", false)))) {
                                    for (int i = 0; i < lines2.size(); i++) {
                                        if (!(lines2.get(i).contains(quizName) && lines2.get(i).contains(courseName))) {
                                            tpw.write(lines2.get(i));
                                            tpw.flush();
                                        }
                                    }
                                } catch (IOException e) {
                                    JOptionPane.showConfirmDialog(null,
                                            "Error occurred writing to file!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                                    e.printStackTrace();
                                }
                                pw.write("Success");
                                pw.println();
                                pw.flush();
                            }
                        }
                        if (teacherDash.equals("Remove a course")) {
                            String courseName = br.readLine();
                            ArrayList<String> lines2 = readFromFile("Courses.txt");
                            try (PrintWriter tpw = new PrintWriter(new BufferedWriter(new FileWriter("Courses.txt", false)))) {
                                for (int i = 0; i < lines2.size(); i++) {
                                    if (!(lines2.get(i).contains(courseName))) {
                                        tpw.write(lines2.get(i));
                                        tpw.flush();
                                    }
                                }
                            } catch (IOException e) {
                                JOptionPane.showConfirmDialog(null,
                                        "Error occurred writing to file!", "Brightspace", JOptionPane.DEFAULT_OPTION);
                                e.printStackTrace();
                            }
                            pw.write("Success");
                            pw.println();
                            pw.flush();
                        }
                        if (teacherDash.equals("Grade A Quiz")) {
                            ArrayList<String> courses = readFromFile("Courses.txt");
                            oos.writeObject(courses);
                            String courseName = br.readLine();
                            Course course = new Course(courseName);
                            ArrayList<String> gradedQuizzes = course.getGradedQuizzes();
                            ArrayList<String> quizNames = new ArrayList<>();
                            String tempUser;
                            String quizName;
                            for (int i = 0; i < gradedQuizzes.size(); i++) {
                                quizName = gradedQuizzes.get(i).substring(0, gradedQuizzes.get(i).indexOf(' '));
                                String line = gradedQuizzes.get(i).substring(gradedQuizzes.get(i).indexOf(' ') + 1);
                                System.out.println(line);
                                tempUser = line.substring(0, line.indexOf(' '));
                                System.out.println(tempUser);
                                quizNames.add(tempUser + " - " + quizName);
                            }
                            oos.writeObject(quizNames);
                            String quizNameAndUser = br.readLine();
                            String line = quizNameAndUser.substring(quizNameAndUser.indexOf(' ') + 1);
                            line = line.substring(line.indexOf(' ') + 1);
                            String quizN = line;
                            ArrayList<String> quizzes = readFromFile("GradedQuizzes.txt");
                            String fileName = "";
                            ArrayList<String> studentAnswers = new ArrayList<>();
                            for (int i = 0; i < quizzes.size(); i++) {
                                String temp;
                                if (quizzes.get(i).contains(quizN) && quizzes.get(i).contains(courseName)) {
                                    temp = quizzes.get(i).substring(quizzes.get(i).indexOf(' ') + 1);
                                    temp = temp.substring(temp.indexOf(' ') + 1);
                                    fileName = temp.substring(0, temp.indexOf(' '));
                                    temp = temp.substring(temp.indexOf(' ') + 1);
                                    temp = temp.substring(temp.indexOf(' ') + 1);
                                    int numSpaces = 0;
                                    for (int j = 0; j < temp.length(); j++) {
                                        if (temp.charAt(i) == ' ') {
                                            numSpaces++;
                                        }
                                    }
                                    numSpaces += 2;
                                    System.out.println(numSpaces);
                                    System.out.println(temp);
                                    for (int j = 0; j < numSpaces; j++) {
                                        studentAnswers.add(String.valueOf(temp.charAt(i)));
                                        System.out.println(String.valueOf(temp.charAt(i)));
                                        temp = temp.substring(temp.indexOf(' ') + 1);
                                    }
                                    break;
                                }
                            }
                            ArrayList<String> quizQuestions = readFromFile(fileName);
                            oos.writeObject(quizQuestions);
                            oos.writeObject(studentAnswers);
                            String numCorrect = br.readLine();
                            writeToFile("GradedByTeacher.txt", true, quizNameAndUser + " " + numCorrect + "/" + quizQuestions.size());
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error",
                            "Brightspace", JOptionPane.ERROR_MESSAGE);
                }
            } while (newAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeToFile(String filename, boolean append, String message) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename, append)))) {
            pw.write(message);
            pw.println();
            pw.flush();
            System.out.println("Message has been written to file");
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null,
                    "Error occurred writing to file!", "Brightspace", JOptionPane.DEFAULT_OPTION);
            e.printStackTrace();
        }
    }
    public static ArrayList<String> readFromFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
