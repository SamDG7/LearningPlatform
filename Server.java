import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    private static Socket socket;
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            int counter = 0;
            System.out.println("Waiting for the client to connect...");
            while (true) {
                counter++;
                socket = serverSocket.accept();
                System.out.println(counter + "Client Connected!");
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

            System.out.println("before tOrS");
            String teacherOrStudent = br.readLine();
            System.out.println(teacherOrStudent);
            System.out.println("after tOrS");
            String logOrSin = br.readLine();
            System.out.println(logOrSin);


            if (logOrSin.equals("new")) {
                String username = br.readLine();
                String password = br.readLine();
                if (teacherOrStudent.equals("Student")) {
                    writeToFile("StudentLogins.txt", true, username + "," + password);
                } else {
                    writeToFile("TeacherLogins.txt", true, username + "," + password);
                }
                pw.write("Success");
                pw.println();
                pw.flush();
                System.out.println("Written success");
            } else if (logOrSin.equals("returning")) {
                System.out.println("inside returning code");
                String username = "";
                String password = "";
                String enterUser = br.readLine();
                String enterPass = br.readLine();
                System.out.println(enterUser + enterPass);
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
                        System.out.println("Written sucess");
                    } else {
                        pw.write("Failed");
                        pw.println();
                        pw.flush();
                        System.out.println("Written failed");
                    }
                    String studentDash = br.readLine();
                    System.out.println("Recieved student dash choice: " + studentDash);
                    if (studentDash.equals("Take a quiz")) {
                        String courseName = br.readLine();
                        System.out.println("Recieved course name from client");
                        Course course = new Course(courseName);
                        ArrayList<String> quizNames = course.getQuizzes();
                        oos.writeObject(quizNames);
                        System.out.println("sent list of quizzes to client");
                        String quizName = br.readLine();
                        System.out.println("recieved quiz name from client");
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
                        oos.writeObject(questions);
                        oos.flush();
                        System.out.println("Sent questions to client");
                        ArrayList<String> questionAnswers = (ArrayList<String>) ois.readObject();
                        try (PrintWriter tpw = new PrintWriter(new BufferedWriter(new FileWriter("GradedQuizzes.txt", true)))) {
                            System.out.println("writing to graded quizzes");
                            tpw.write(courseName + " " + quizName + " " + quizFile + " " + username + " ");
                            for (int i = 0; i < questionAnswers.size(); i++) {
                                if (i != questionAnswers.size() - 1) {
                                    System.out.println("written first answers");
                                    tpw.write(questionAnswers.get(i) + " ");
                                } else {
                                    System.out.println("written alst answer");
                                    tpw.write(questionAnswers.get(i));
                                }
                            }
                            tpw.println();
                            tpw.flush();
                            System.out.println("Message has been written to file");
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
                        System.out.println("Recieved course name from client");
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
                        System.out.println("Written sucess");
                    } else {
                        pw.write("Failed");
                        pw.println();
                        pw.flush();
                        System.out.println("Written failed");
                    }
                    String teacherDash = br.readLine();
                    System.out.println("Recived teacher dash option" + teacherDash);
                    if (teacherDash.equals("Create a course")) {
                        String courseName = br.readLine();
                        System.out.println("Server has received course name " + courseName);
                        writeToFile("Courses.txt", true, courseName);
                        pw.write("Success");
                        pw.println();
                        pw.flush();
                    }
                    if (teacherDash.equals("Edit a course")) {
                        System.out.println("inside edit a course");
                        String courseName = br.readLine();
                        String editAction = br.readLine();
                        System.out.println("recevied course name and edit action");
                        if (editAction.equals("Create a quiz")) {
                            String quizFile = br.readLine();
                            String quizName = br.readLine();
                            String questionNum = br.readLine();
                            String random = br.readLine();
                            System.out.println("Recived all quiz info");
                            if (quizName.contains(" ")) {
                                quizName = quizName.replaceAll(" ", "_");
                                System.out.println("Replaced blanks in quiz name");
                            }
                            writeToFile("Quizzes.txt", true, courseName + " " + quizFile + " " + quizName + " " + questionNum + " " + random);

                            pw.write("W");
                            pw.println();
                            pw.flush();
                            System.out.println("wrote success");
                        }
                        if (editAction.equals("Delete a quiz")) {
                            String quizName = br.readLine();
                            System.out.println("Recieved quiz name from client");
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
                        System.out.println(courseName);
                        System.out.println("Received course name from client");
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
                        System.out.println("wrote success to client");
                    }
                    if (teacherDash.equals("Grade A Quiz")) {
                        ArrayList<String> courses = readFromFile("Courses.txt");
                        oos.writeObject(courses);
                        String courseName = br.readLine();
                        System.out.println("Recieved course name from client");
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
                System.out.println("Error");
            }
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
