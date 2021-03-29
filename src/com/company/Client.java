package com.company;

import com.company.storables.DragonUtils;
import com.company.ui.CommandExecutor;
import com.company.ui.CommandReader;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Date;

public class Client {

    private static final String[] dragonCommands = {"insert", "update", "replace_if_greater_age"};
    private static final InetSocketAddress address = new InetSocketAddress("localhost", 3333);
    private static ByteArrayOutputStream byteArrayOutputStream;
    private static ObjectOutput objectOutput;
    private static DatagramSocket datagramSocket = null;

    /**
     * Main client function
     *
     * @param args filename
     * @see CommandExecutor
     * @see Server
     */
    public static void main(String[] args) {

        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);
            datagramSocket.setSoTimeout(1500);
        } catch (IOException e) {
            System.out.println("Can't connect to server.");
            System.exit(-1);
        }

        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        CommandReader commandReader = new CommandReader(cin);
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.out.println("Welcome to interactive Dragon Hashtable server. To get help, enter \"help\".");
        //noinspection InfiniteLoopStatement
        while (true) {
            CommandReader.Command command = commandReader.readCommandFromBufferedReader();
            try {
                byteArrayOutputStream.reset();
                objectOutput = new ObjectOutputStream(byteArrayOutputStream);
                if (sendCommand(command))
                    System.out.print(getResponse());
            } catch (IOException e) {
                System.err.print(e.getMessage());
                System.err.println(". Trying again...");
                try {
                    if (sendCommand(command))
                        System.out.print(getResponse());
                } catch (IOException e2) {
                    System.err.println("Failed. Please try again later.");
                }
            }
        }


    }

    /**
     * Gets a command, verifies it and sends it to server (or exits the program if that is the case)
     * @param userCommand Command input
     * @return true if Command was verified and sent
     * @throws IOException If problem occurred while sending to server
     */
    private static boolean sendCommand(CommandReader.Command userCommand) throws IOException {
        if (userCommand.CommandString.equals("exit")) {
            System.out.println("Exiting...");
            byteArrayOutputStream.close();
            datagramSocket.close();
            System.exit(0);
        }
        if (Arrays.stream(CommandExecutor.userCommands).parallel().noneMatch(command -> command.getLabel().equals(userCommand.CommandString))) {
            System.err.println("Unknown command \"" + userCommand.CommandString + "\". try \"help\" for list of commands");
            return false;
        }
        if (!Arrays.stream(CommandExecutor.userCommands).parallel().filter(command -> command.getLabel().equals(userCommand.CommandString)).findAny().get().getArgumentLabel().equals("")
                && userCommand.ArgumentString.isBlank()) {
            System.err.println("Please specify command argument.");
            return false;
        }
        Date date = new Date();
        String dragonString;
        if (Arrays.stream(dragonCommands).parallel().anyMatch(command -> command.equals(userCommand.CommandString))) {
            userCommand.CommandString += "_csv";
            if (userCommand.CommandString.equals("update_id_csv")) {
                try {
                    dragonString = DragonUtils.inputDragonFromConsole(Long.parseLong(userCommand.ArgumentString), date).toCsvString();
                    userCommand.ArgumentString = dragonString;
                } catch (NumberFormatException e) {
                    System.err.println("Can't parse dragon ID from " + userCommand.ArgumentString + ".");
                }
            } else {
                dragonString = DragonUtils.inputDragonFromConsole(-1, date).toCsvString(); //-1 value will be replaced with new ID
                userCommand.ArgumentString = userCommand.ArgumentString + "," + dragonString;
            }

        }
        objectOutput.writeObject(userCommand);
        byte[] data = byteArrayOutputStream.toByteArray();
        datagramSocket.send(new DatagramPacket(data, data.length, address));
        return true;
    }

    /**
     * Gets a message from a server
     * @return message
     * @throws IOException If problem occurred while receiving from server
     */
    private static String getResponse() throws IOException {
        byte[] data = new byte[65536];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        datagramSocket.receive(receivePacket);
        return new String(receivePacket.getData());
    }
}
