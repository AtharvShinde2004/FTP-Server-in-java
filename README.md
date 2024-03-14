# Custom FTP Server in Java

This is a simple FTP (File Transfer Protocol) server implemented in Java. The server allows clients to connect and perform basic file operations like listing files in a directory and downloading files from the server.

## Features

- **Server**: The server is capable of handling multiple client connections concurrently.
- **Client**: The client program provides a command-line interface for users to interact with the server.
- **Commands**: Supported commands include `LIST` to list files on the server and `GET <filename>` to download a file from the server.
- **Error Handling**: Proper error handling is implemented to handle various scenarios such as unsupported commands and file not found errors.

## Prerequisites

- Java Runtime Environment (JRE) installed on your system.

## Usage

1. **Download JAR Files**: Download the `server.jar` and `client.jar` files from the [Releases](https://github.com/AtharvShinde2004/FTP-Server-in-java/releases) section of this repository.

2. **Start the Server**: Run the server JAR file to start the FTP server.

```bash
java -jar server.jar
```

3. **Connect with the Client**: Run the client JAR file to connect with the server.

```bash
java -jar client.jar <server-ip> <server-port>
```

Replace `<server-ip>` and `<server-port>` with the IP address and port number where the server is running.

4. **Interact with the Server**: Once connected, you can use the following commands:

- `LIST`: Lists files available on the server.
- `GET <filename>`: Downloads the specified file from the server.
- `exit`: Closes the connection and exits the client program.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- This project was inspired by the need for a simple FTP server implementation for educational purposes.
- Thanks to contributors who have helped improve this project.
