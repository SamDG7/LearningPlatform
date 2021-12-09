import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
    /**
     * The name of the course.
     */
    public String courseName;

    /**
     * The ArrayList that stores the names of the existing courses.
     */
    public ArrayList<String> courses;

    /**
     * The number of courses.
     */
    public int courseNumber;

    /**
     * Constructs a newly allocated object with the name of the course.
     * @param courseName
     */
    public Course(String courseName) {
        this.courseName = courseName;
        courses = new ArrayList<String>();
    }

    /**
     * Method only for teachers to create a new course
     */
    public void createCourse() {
        /**
         * Scanner
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the name of the new course:");
        /**
         * Input value for the name of the course
         */
        courseName = scanner.nextLine();

        /**
         * The name of the new course is added to the ArrayList of courses
         */
        courses.add(courseName);
        System.out.println("Course successfully added!");
    }

    /**
     * Method only for teachers
     * Edits existing course
     */
    public void editCourse() {
        Scanner scanner = new Scanner(System.in);
        /**
         * Accesses list of courses
         */
        getCourses();
        System.out.println("Select the number of the course to edit:");
        /**
         * Input value for the number of the course to be edited
         */
        courseNumber = scanner.nextInt();
        System.out.println("Please enter the new name for the course:");
        /**
         * Input value for the new name of the course
         */
        courseName = scanner.nextLine();
        /**
         * Changes name in the ArrayList
         */
        courses.set(courseNumber - 1, courseName);
        System.out.println("Course successfully edited!");
    }

    /**
     * Method only for teachers
     * Deletes course
     */
    public void deleteCourse() {
        Scanner scanner = new Scanner(System.in);
        /**
         * Accesses the list of the courses
         */
        getCourses();
        System.out.println("Please select the number of the course to remove:");
        /**
         * Input value for the number of the course to be deleted
         */
        courseNumber = scanner.nextInt();
        /**
         * Accesses the index number of the course and deleted entry from the ArrayList of courses
         */
        courses.remove(courseNumber - 1);
        System.out.println("Course successfully removed!");
    }

    /**
     * Creates an ArrayList of courses
     * @return
     */
    public ArrayList<String> courses() {
        for (int i = 0; i < courseName.length(); i++) {
            courses.add(courseName);
        }
        return courses;
    }

    /**
     * Used to access the list of courses
     * Displays the list of courses with the number before it for each line
     */
    public void getCourses() {
        for (int i = 1; i < courses.size(); i++) {
            int courseIndex = i++;
            System.out.println(courseIndex + ". " + courses.get(i) + "\n");
        }
    }

    /**
     * Similar to getCourses, it reqtrieves the quizzes for the student
     * @throws FileNotFoundException
     * @throws IOException
     */
    public ArrayList<String> getQuizzes() throws FileNotFoundException, IOException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> lines2 = new ArrayList<>();
        ArrayList<String> quizNames = new ArrayList<>();
        /**
         * Converts the file to ArrayList for the quiz to be accessed
         */
        try (BufferedReader br = new BufferedReader(new FileReader("Quizzes.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(courseName)) {
                    lines2.add(line);
                    line = line.substring(line.indexOf(' ') + 1);
                    line = line.substring((line.indexOf(' ') + 1));
                    line = line.substring(0, line.indexOf(' '));
                    quizNames.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quizNames;

    }

    /**
     * Retrieves the graded quizzes
     */
    public ArrayList<String> getGradedQuizzes() {
        ArrayList<String> lines2 = new ArrayList<>();
        ArrayList<String> quizNames = new ArrayList<>();
        /**
         * Converts the file to ArrayList for the quiz to be accessed
         */
        try (BufferedReader br = new BufferedReader(new FileReader("GradedQuizzes.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(courseName)) {
                    lines2.add(line);
                    line = line.substring(line.indexOf(' ') + 1);
                    String quizName = line.substring(0, line.indexOf(' '));
                    line = line.substring(line.indexOf(' ') + 1);
                    String quizFile = line.substring(0, line.indexOf(' '));
                    line = line.substring(line.indexOf(' ') + 1);
                    String username = line.substring(0, line.indexOf(' '));
                    line = line.substring(line.indexOf(' ') + 1);
                    String answers = line;
                    String finalString = quizName + " " + username + " " + quizFile +" " + answers;
                    quizNames.add(finalString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quizNames;
    }
}
