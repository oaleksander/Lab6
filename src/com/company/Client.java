package com.company;

import com.company.commands.Command;
import com.company.storables.DragonUtils;
import com.company.ui.ClientClass;
import com.company.ui.CommandReader;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Arrays;
import java.util.Optional;

public class Client {

    private static final String[] dragonCommands = {"insert","update","replace_if_greater_age"};
    private static ByteArrayOutputStream byteArrayOutputStream;
    private static ObjectOutput objectOutput;
   // private static SocketChannel client = null;
   // private static ByteBuffer buffer = null;

    private static final InetSocketAddress address = new InetSocketAddress("localhost", 3333);


    private static DatagramSocket datagramSocket = null;

    public static void main(String[] args)
    {


        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);
            //datagramSocket.bind(address);
            //client = SocketChannel.open(new InetSocketAddress("localhost", 3333));
        } catch (IOException e) {
            System.out.println("Can't connect to server.");
            System.exit(-1);
        }

        System.out.println("Connection with server established.");

        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        CommandReader commandReader = new CommandReader(cin);
        byteArrayOutputStream = new ByteArrayOutputStream();

        while(true) {
            CommandReader.UserCommand userCommand = commandReader.readCommandFromBufferedReader();
            try {
            objectOutput = new ObjectOutputStream(byteArrayOutputStream);
            if(handleCommand(userCommand)) {}
          ///  System.out.print(getResponse());
            //buffer.clear();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println("Something is wrong with server connection. Trying ro reconnect...");
                try {
                    //datagramSocket.bind(new InetSocketAddress("localhost", 3333));System.out.println("Reconnected!");
                    if(handleCommand(userCommand)){}
                   //     System.out.print(getResponse());
                } catch (IOException e2) {
                    System.out.println("Can't connect to server. Please try again later.");
                }
            }
        }


    }
    private static boolean handleCommand(CommandReader.UserCommand userCommand) throws IOException {
        if(userCommand.Command.equals("exit")) {
            System.out.println("Exiting...");
            byteArrayOutputStream.close();
            datagramSocket.close();
            System.exit(0);
        }
        //if(!datagramSocket.isConnected())
        //    throw new IOException();
        if(Arrays.stream(ClientClass.userCommands).parallel().noneMatch(command -> command.getLabel().equals(userCommand.Command)))
        {
            System.out.println("Unknown command \"" + userCommand.Command + "\". try \"help\" for list of commands");
            return false;
        }
        if(!Arrays.stream(ClientClass.userCommands).parallel().filter(command -> command.getLabel().equals(userCommand.Command)).findAny().get().getArgumentLabel().equals("")
                && userCommand.Argument.isBlank()) {
            System.out.println("Please specify command argument.");
             return false;
        }
        Date date = new Date();
        String dragonString;
        long dragonID = -1;
        if(Arrays.stream(dragonCommands).parallel().anyMatch(command -> command.equals(userCommand.Command))) {
            userCommand.Command += "_csv";
            if(userCommand.Command.equals("update_id_csv")) {
                try {
                    dragonString = DragonUtils.inputDragonFromConsole(Long.parseLong(userCommand.Argument),date).toCsvString();
                    userCommand.Argument = dragonString;
                } catch (NumberFormatException e) {
                    System.out.println("Can't parse dragon ID from " + userCommand.Argument + ".");
                }
            }
            else {
                dragonString = DragonUtils.inputDragonFromConsole(-1, date).toCsvString(); //-1 value will be replaced with new ID
                userCommand.Argument = userCommand.Argument + "," + dragonString;
            }

        }

        objectOutput.writeObject(userCommand);
        byte[] data = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.reset();
        datagramSocket.send(new DatagramPacket(data,data.length,address));
        //client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
        return true;
    }

    private static String getResponse() throws IOException {
       // buffer = ByteBuffer.allocate(65536);
       // client.read(buffer);
        byte[] data = new byte[65536];
        DatagramPacket receivePacket = new DatagramPacket(data,data.length);
        datagramSocket.receive(receivePacket);
        return new String(receivePacket.getData());
    }
}

/*
DatagramSocket sock = null;

        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        CommandReader commandReader = new CommandReader(cin);


        try
        {
            sock = new DatagramSocket();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput;
            while(true)
            {
                //Ожидаем ввод сообщения серверу
                System.out.println("Input message to server: ");
                objectOutput = new ObjectOutputStream(byteArrayOutputStream);
                CommandReader.UserCommand userCommand = commandReader.readCommandFromBufferedReader();
                if(Arrays.stream(ClientClass.userCommands).parallel().noneMatch(command -> command.getLabel().equals(userCommand.Command)))
                {
                    System.out.println("Unknown command \"" + userCommand.Command + "\". try \"help\" for list of commands");
                    continue;
                }
                if(!Arrays.stream(ClientClass.userCommands).parallel().filter(command -> command.getLabel().equals(userCommand.Command)).findAny().get().getArgumentLabel().equals("")
                    && userCommand.Argument.isBlank()) {
                    System.out.println("Please specify command argument.");
                    continue;
                }

                if(userCommand.Command.equals("exit")) {
                    System.out.println("Exiting...");
                    sock.close();
                    byteArrayOutputStream.close();
                    return;
                }
                Date date = new Date();
                String dragonString;
                long dragonID = -1;
                if(Arrays.stream(dragonCommands).parallel().anyMatch(command -> command.equals(userCommand.Command))) {
                    userCommand.Command += "_csv";
                    if(userCommand.Command.equals("update_id_csv")) {
                        try {
                            dragonString = DragonUtils.inputDragonFromConsole(Long.parseLong(userCommand.Argument),date).toCsvString();
                            userCommand.Argument = dragonString;
                        } catch (NumberFormatException e) {
                            System.out.println("Can't parse dragon ID from " + userCommand.Argument + ".");
                        }
                    }
                    else {
                        dragonString = DragonUtils.inputDragonFromConsole(-1, date).toCsvString(); //-1 value will be replaced with new ID
                        userCommand.Argument = userCommand.Argument + "," + dragonString;
                    }

                }

                objectOutput.writeObject(userCommand);

                byte[] b = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.reset();

                //Отправляем сообщение
                DatagramPacket dp = new DatagramPacket(b , b.length , InetAddress.getByName("127.0.0.1") , 3333);

                sock.send(dp);

                //буфер для получения входящих данных
                byte[] buffer = new byte[65536];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

                //Получаем данные
                sock.receive(reply);
                byte[] data = reply.getData();
                String s = new String(data, 0, reply.getLength());

                System.out.print("Server response:\n" + s);
            }
        }catch(IOException e)
        {
            System.err.println("IOException " + e);
            System.exit(-1);
        }
 */
