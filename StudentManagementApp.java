import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Student {
    private String name;
    private int rollNumber;
    private String grade;
    private String address;

    public Student(String name, int rollNumber, String grade, String address) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
        this.address = address;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getRollNumber() { return rollNumber; }
    public void setRollNumber(int rollNumber) { this.rollNumber = rollNumber; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "Student{" +
                "Name='" + name + '\'' +
                ", Roll Number=" + rollNumber +
                ", Grade='" + grade + '\'' +
                ", Address='" + address + '\'' +
                '}';
    }
}

class StudentManagementSystem {
    private List<Student> students;

    public StudentManagementSystem() {
        students = new ArrayList<>();
    }

    // Method to add a student
    public void addStudent(Student student) {
        students.add(student);
        System.out.println("Student added successfully.");
    }

    // Method to remove a student by roll number
    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
        System.out.println("Student removed successfully.");
    }

    // Method to search for a student by roll number
    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    // Method to display all students
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students to display.");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }

    public List<Student> getAllStudents() {
        return students;
    }
}

class FileManager {
    private static final String FILE_NAME = "students.txt";

    public static void saveStudents(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student student : students) {
                writer.write(student.getName() + "," + student.getRollNumber() + "," + student.getGrade() + "," + student.getAddress());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadStudents(StudentManagementSystem sms) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Student student = new Student(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]);
                    sms.addStudent(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class StudentManagementApp {
    private static StudentManagementSystem sms = new StudentManagementSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        FileManager.loadStudents(sms);  // Load students from file at startup

        boolean running = true;

        while (running) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addNewStudent();
                    break;
                case 2:
                    removeStudent();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    sms.displayAllStudents();
                    break;
                case 5:
                    FileManager.saveStudents(sms.getAllStudents());  // Save students to file before exiting
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nStudent Management System");
        System.out.println("1. Add New Student");
        System.out.println("2. Remove Student");
        System.out.println("3. Search for a Student");
        System.out.println("4. Display All Students");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addNewStudent() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        System.out.print("Enter Roll Number: ");
        int rollNumber;
        try {
            rollNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid roll number!");
            return;
        }

        System.out.print("Enter Grade: ");
        String grade = scanner.nextLine();
        if (grade.isEmpty()) {
            System.out.println("Grade cannot be empty!");
            return;
        }

        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        Student student = new Student(name, rollNumber, grade, address);
        sms.addStudent(student);
    }

    private static void removeStudent() {
        System.out.print("Enter Roll Number of the student to remove: ");
        int rollNumber = Integer.parseInt(scanner.nextLine());
        sms.removeStudent(rollNumber);
    }

    private static void searchStudent() {
        System.out.print("Enter Roll Number of the student to search: ");
        int rollNumber = Integer.parseInt(scanner.nextLine());
        Student student = sms.searchStudent(rollNumber);
        if (student != null) {
            System.out.println(student);
        } else {
            System.out.println("Student not found.");
        }
    }
}
