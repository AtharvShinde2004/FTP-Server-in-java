import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        // Check if the correct number of command-line arguments is provided
        if (args.length != 2) {
            System.out.println("Error; usage -> IPAddress Port");
            return;
        }

        String IPAddress = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket cs = new Socket(IPAddress, port);
                DataOutputStream out = new DataOutputStream(cs.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()))) {

            System.out.println("Connecting to server " + IPAddress + ":" + port);
            System.out.println("---Connection Successful---");

            // Read and display the worker ID sent by the server
            String workerId = in.readLine();
            System.out.println("Received Worker ID: " + workerId);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Prompt the user to enter a command
                System.out.print("Enter command 'LIST' or 'GET <filename>' or 'exit' to close connection: ");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("exit")) {
                    out.writeBytes(command + '\n');
                    break;
                }

                // Validate the entered command
                if (!isValidCommand(command)) {
                    System.out.println("Invalid command. Please enter either 'LIST' or 'GET <filename>'.");
                    continue;
                }

                System.out.println("Sending \"" + command + "\" to server");
                out.writeBytes(command + '\n');

                String response = in.readLine();

                if (response != null && !response.isEmpty()) {
                    handleServerResponse(command, response, in, scanner);
                } else {
                    System.out.println("Error reading server response.");
                    break;
                }
            }

            System.out.println("---Closing connection---");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Validate if the entered command is in the correct format
    private static boolean isValidCommand(String command) {
        return command.matches("(?i)(LIST|GET\\s+\\S+)");
    }

    // Handle different server responses based on the command
    private static void handleServerResponse(String command, String response, BufferedReader in, Scanner scanner)
            throws IOException {
        if (command.contains("LIST")) {
            System.out.println("Server response:");
            System.out.println(response);

            // Display the list of files received from the server
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) {
                    break; // Break the loop when an empty line is encountered
                }
                System.out.println(line);
            }
        } else if (command.contains("GET")) {
            handleGetResponse(command, response, in, scanner);
        } else {
            System.out.println("Server response:\n" + response);
        }
    }

    // Handle the response for the 'GET' command
    private static void handleGetResponse(String command, String response, BufferedReader in, Scanner scanner)
            throws IOException {
        if (response.equals("OK")) {
            String fileName = command.substring(4).trim();
            File file = new File(fileName);

            if (file.exists()) {
                // Ask the user if they want to overwrite the existing file
                System.out.print("File \"" + fileName + "\" already exists. Do you want to overwrite it? (yes/no): ");
                String overwriteChoice = scanner.nextLine().toLowerCase();

                if (!overwriteChoice.equals("yes")) {
                    System.out.println("Download canceled.");
                    return;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                int data;
                // Read data from the server and write it to the local file
                while ((data = in.read()) != -1) {
                    writer.write(data);
                }
                System.out.println("File \"" + fileName + "\" downloaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } else {
            System.out.println("Server response:\n" + response);
        }
    }
}
