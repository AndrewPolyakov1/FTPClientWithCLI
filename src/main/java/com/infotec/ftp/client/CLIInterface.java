package com.infotec.ftp.client;

import com.infotec.ftp.client.remote.IFTPClient;
import com.infotec.ftp.client.remote.SimpleFTPClient;
import com.infotec.ftp.client.utils.EMenuOptions;
import com.infotec.ftp.client.utils.StudentList;
import com.infotec.ftp.client.utils.Utils;
import com.infotec.ftp.client.utils.errors.RemoteException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class CLIInterface {

    private static final String localTempPath = "src\\main\\resources\\studentsTemp.json";
    private final IFTPClient ftpClient;
    BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
    private StudentList studentList = null;
    private boolean updated = false;


    public CLIInterface(String login, String password, String host, int port, String remotePath) throws MalformedURLException {
        this.ftpClient = new SimpleFTPClient(login, password, host, port, remotePath);
    }

    private void printMenu() {
        System.out.println(">----------------------");
        System.out.println("1. List");
        System.out.println("2. Get by ID");
        System.out.println("3. Add");
        System.out.println("4. Delete by ID");
        System.out.println("5. Exit");
        System.out.println("6. Help");
        System.out.println(">----------------------");
    }

    private void checkListIntegrity() {
        if (studentList == null) {
            try {
                studentList = StudentList.fromJsonString(ftpClient.downloadFileAsString());
                updated = false;
            } catch (ParseException e) {
                displayError("Parse error: " + e.getMessage());
                System.exit(1);
            } catch (RemoteException e) {
                displayError("Remote error: " + e.getMessage() + "\n" + "Exiting...");
                System.exit(1);
            }
        }
    }

    private String getStudentById(int id) {
        checkListIntegrity();
        String student = studentList.getById(id);
        if (student == null) {
            return "> Student not found";
        }
        return student;
    }

    private void addStudent(String student) {
        checkListIntegrity();
        studentList.addStudent(student);
        updated = true;
    }

    private void displayStudentList() {
        checkListIntegrity();
        for (Map.Entry<Integer, String> entry : studentList.getStudentsListWithId()) {
            System.out.println("- " + entry.getValue() + " <" + entry.getKey() + ">");
        }
    }

    private void deleteStudentById(int id) {
        checkListIntegrity();
        studentList.deleteById(id);
        updated = true;
    }

    private void displayError(String message) {
        System.err.println(message);
    }

    private void updateRemoteStudentList() throws IOException {
        if (!updated) {
            return;
        }
        String json = studentList.toJson();
        Utils.checkFileOrCreate(localTempPath);

        // Stores local copy after changes
        try (PrintWriter writer = new PrintWriter(localTempPath)) {
            writer.write(json);
        } catch (IOException e) {
            displayError("Error while checking the dile file: " + e.getMessage());
        }

        try {
            ftpClient.uploadFile(localTempPath);
        } catch (IOException e) {
            displayError("Error while uploading file: " + e.getMessage());
        } catch (RemoteException e) {
            displayError("Remote error: " + e.getMessage());
        }

        updated = false;
    }


    public void run() {
        boolean flag = true;
        printMenu();

        while (flag) {
            System.out.print("Enter option: ");
            EMenuOptions option;
            try {
                option = EMenuOptions.valueOf(Integer.parseInt(reader.readLine()) - 1);
            } catch (NumberFormatException e) {
                displayError("Invalid option");
                continue;
            } catch (IOException ignored) {
                continue;
            }

            if (option == null) {
                displayError("Invalid option");
                continue;
            }

            switch (option) {
                case LIST:
                    displayStudentList();
                    break;
                case GET_BY_ID:
                    System.out.print("Enter ID: ");
                    try {
                        System.out.println(getStudentById(Integer.parseInt(reader.readLine())));
                    } catch (IOException e) {
                        displayError("Error occurred while getting student by ID: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        displayError("Invalid ID");
                    }
                    break;
                case ADD:
                    System.out.print("Enter student name: ");
                    try {
                        addStudent(reader.readLine());
                    } catch (IOException e) {
                        displayError("Error occurred while adding student: " + e.getMessage());
                    }
                    break;
                case DELETE_BY_ID:
                    System.out.println("Delete by ID");
                    System.out.print("Enter ID: ");
                    try {
                        deleteStudentById(Integer.parseInt(reader.readLine()));
                    } catch (IOException e) {
                        displayError("Error occurred while deleting student by ID: " + e.getMessage());
                    }
                    break;
                case EXIT:
                    System.out.println("Exit");
                    flag = false;
                    break;
                case HELP:
                    printMenu();
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
            try {
                updateRemoteStudentList();
            } catch (IOException e) {
                displayError("Error occurred while updating remote list: " + e.getMessage());
            }
        }
    }
}
