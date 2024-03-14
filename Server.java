import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    static final int LISTENING_PORT = 7210; // Change this Port as per needs
    private String directoryName = "C:\\Users\\Shind\\OneDrive\\Desktop\\FTP"; // Create a New Folder in the Desktop and Assign its Path here
    private ExecutorService executorService = Executors.newFixedThreadPool(50); // Limit the thread pool size to 50

    public Server() {
        try (ServerSocket listener = new ServerSocket(LISTENING_PORT)) {
            System.out.println("Server is listening on port " + LISTENING_PORT);

            // Continuously accept incoming connections
            while (true) {
                Socket connection = listener.accept();
                // Delegate connection handling to a separate thread
                executorService.execute(new ClientHandler(directoryName, connection));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // } finally {
        // Shut down the executor service when done
        // executorService.shutdown();
        // }
    }

    public static void main(String[] args) {
        // Start the server
        new Server();
    }
}

class ClientHandler implements Runnable {
    private final File directory;
    private final Socket connection;

    public ClientHandler(String directoryName, Socket connection) {
        // Initialize the ClientHandler with the server's directory and the client's
        // connection
        this.directory = new File(directoryName);
        this.connection = connection;
    }

    @Override
    public void run() {
        try (BufferedReader incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                PrintWriter outgoing = new PrintWriter(connection.getOutputStream(), true)) {

            // Send the worker ID to the client (you can customize this part as needed)
            outgoing.println(Thread.currentThread().threadId());

            String command;
            // Process incoming commands from the client
            while ((command = incoming.readLine()) != null) { // Check for null to handle abrupt disconnections
                System.out.println("Received command: " + command);

                // Check the received command and take appropriate actions
                if (command.trim().equalsIgnoreCase("exit")) {
                    break;
                } else if (command.trim().equalsIgnoreCase("list")) {
                    // Send the list of files in the directory to the client
                    sendList(directory, outgoing);
                } else if (command.toLowerCase().startsWith("get")) {
                    // Extract the filename from the 'get' command and send the corresponding file
                    String fileName = command.substring(3).trim();
                    sendFile(fileName, directory, outgoing, incoming, connection);
                } else {
                    // Notify the client about unsupported commands
                    outgoing.println("ERROR unsupported command");
                }

                // Ensure that data is sent immediately to the client
                outgoing.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // } finally {
        // try {
        // Close the connection when the thread is done
        // connection.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
    }

    // Helper method to send the list of files in the directory to the client
    private static void sendList(File directory, PrintWriter outgoing) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                // Send each file name to the client
                outgoing.println(file.getName());
            }
        }
        // Add an empty line to indicate the end of the file list
        outgoing.println();
        // Ensure that data is sent immediately to the client
        outgoing.flush();
        System.out.println("Sent LIST response");
    }

    // Helper method to send a specific file to the client
    private static void sendFile(String fileName, File directory, PrintWriter outgoing, BufferedReader incoming,
            Socket connection) {
        File file = new File(directory, fileName);

        if (file.exists() && file.isFile()) {
            try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
                    BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileInputStream));
                    OutputStream outputStream = connection.getOutputStream();
                    BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(outputStream))) {

                char[] buffer = new char[1024];
                int charsRead;

                // Notify the client that the file transfer is about to start
                outgoing.println("OK");
                // Ensure that data is sent immediately to the client
                outgoing.flush();

                // Read and send the contents of the file to the client
                while ((charsRead = fileReader.read(buffer)) != -1) {
                    clientWriter.write(buffer, 0, charsRead);
                }

                // Signal the end of the file transfer
                clientWriter.write("\n");
                // Ensure that data is sent immediately to the client
                clientWriter.flush();

                System.out.println("Sent OK response for file: " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Notify the client if the requested file is not found
            outgoing.println("ERROR file not found");
            // Ensure that data is sent immediately to the client
            outgoing.flush();
            System.out.println("Sent ERROR file not found response for file: " + fileName);
        }
    }
}
