package com.company;

import com.company.commands.Read;
import com.company.ui.CommandExecutor;
import com.company.ui.CommandReader;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Main client class
 */
public class Server {

    private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private static final PrintStream serverResponseStream = new PrintStream(outputStream);
    private static final CommandExecutor localExecutor = new CommandExecutor(CommandExecutor.allCommands, System.out);
    private static final CommandExecutor userExecutor = new CommandExecutor(CommandExecutor.userCommands, serverResponseStream);

    /**
     * Main server function
     *
     * @param args filename
     * @see CommandExecutor
     * @see Client
     */
    public static void main(String[] args) {
        System.out.println("Starting Server...");
        System.out.println("Reading collection from file...");
        readCollectionFromFile(args);
        BufferedReader localInput = new BufferedReader(new InputStreamReader(System.in));
        Selector selector = null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
        try {
            selector = Selector.open();
            selector.wakeup();
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.bind(new InetSocketAddress("localhost", 3333));
            datagramChannel.configureBlocking(false);
            datagramChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.err.println("Failed to start server.");
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Server is active.");
        while (true) {
            try {
                String line;
                if (localInput.ready())
                    if ((line = localInput.readLine()) != null)
                        localExecutor.execute(CommandReader.readCommandFromString(line));
                selector.select(1500);
                for (SelectionKey key : selector.selectedKeys()) {
                    if (!key.isValid()) continue;
                    if (key.isReadable()) answerCommand(byteBuffer, key);
                }
            } catch (IOException e) {
                System.err.println("IOException " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    /**
     * Answers a command from client
     * @param buffer Byte buffer to put data to
     * @param key Selection key
     * @throws IOException If problem occurred while sending data to client
     */
    private static void answerCommand(ByteBuffer buffer, SelectionKey key)
            throws IOException {
        DatagramChannel client = (DatagramChannel) key.channel();
        SocketAddress address = client.receive(buffer);
        if (address == null) return;
        byte[] data = buffer.array();
        ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(data));
        byte[] response;
        try {
            CommandReader.Command command = (CommandReader.Command) iStream.readObject();
            System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "]" + address.toString() + ": " + command.toString() + ".");
            //Отправляем данные клиенту
            try {
                userExecutor.execute(command);
                response = outputStream.toByteArray();
                outputStream.reset();
            } catch (IllegalArgumentException e) {
                response = e.getMessage().getBytes();
            }
        } catch (StreamCorruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            response = "Server received invalid packet. Please try again.".getBytes();
        }

        client.send(ByteBuffer.wrap(response), address);
        buffer.clear();
    }

    /**
     * Reads a collection from file
     * @see Read
     * @param args Filename
     */
    private static void readCollectionFromFile(String[] args) {
        if (args.length == 0)
            System.out.println("Input filename not specified by command line argument. Skipping...");
        else {
            try {
                CommandExecutor.setFile(args[0]);
                try {
                    System.out.println(new Read().execute());
                } catch (Exception e) {
                    System.out.println(e.getMessage() + " Skipping...");
                }
            } catch (NullPointerException e) {
                System.out.println("Input filename is empty. Skipping...");
            }
        }
    }
}
