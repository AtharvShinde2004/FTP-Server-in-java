# Custom FTP Server in Java

This is a simple FTP (File Transfer Protocol) server implemented in Java. The server allows clients to connect and perform basic file operations like listing files in a directory and downloading files from the server.

## Features

- **Server**: The server is capable of handling multiple client connections concurrently.
- **Client**: The client program provides a command-line interface for users to interact with the server.
- **Commands**: Supported commands include `LIST` to list files on the server and `GET <filename>` to download a file from the server.
- **Error Handling**: Proper error handling is implemented to handle various scenarios such as unsupported commands and file not found errors.

## Prerequisites

- Java Development Kit (JDK) installed on your system.
- Basic understanding of Java programming and networking concepts.

## Usage

1. **Clone the Repository**: Clone this repository to your local machine.

```bash
git clone https://github.com/your-username/custom-ftp-server-java.git
```

2. **Compile the Code**: Navigate to the project directory and compile the Java files.

```bash
cd custom-ftp-server-java
javac Server.java Client.java
```

3. **Start the Server**: Run the `Server` class to start the FTP server.

```bash
java Server
```

4. **Connect with the Client**: Run the `Client` class to connect with the server.

```bash
java Client <server-ip> <server-port>
```

Replace `<server-ip>` and `<server-port>` with the IP address and port number where the server is running.

5. **Interact with the Server**: Once connected, you can use the following commands:

- `LIST`: Lists files available on the server.
- `GET <filename>`: Downloads the specified file from the server.
- `exit`: Closes the connection and exits the client program.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- This project was inspired by the need for a simple FTP server implementation for educational purposes.
- Thanks to contributors who have helped improve this project.
