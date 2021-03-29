 package com.company;

import com.company.commands.Read;
import com.company.ui.CommandReader;
import com.company.ui.ClientClass;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

 /**
 * Main client class
 */
public class Server {

     private static final String POISON_PILL = "POISON_PILL";
     private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     private static final PrintStream serverResponseStream = new PrintStream(outputStream);
     private static final ClientClass userClass = new ClientClass(ClientClass.allCommands, serverResponseStream, System.in);
     private static DatagramChannel datagramChannel;

    /**
     * Main server function
     *
     * @param args filename
     * @see com.company.commands.Save
     * @see Read
     * @see ClientClass
     */
    public static void main(String[] args) {
        System.out.println("Welcome to interactive Dragon Hashtable server. To get help, enter \"help\".");
        if (args.length == 0)
            System.out.println("Input filename not specified by command line argument. Skipping...");
        else {
            try {
                ClientClass.setFile(args[0]);
                try {
                    System.out.println(new Read().execute());
                } catch (Exception e) {
                    System.out.println(e.getMessage() + " Skipping...");
                }
            } catch (NullPointerException e) {
                System.out.println("Input filename is empty. Skipping...");
            }
        }
        try {
            Selector selector = Selector.open();
            selector.wakeup();
            datagramChannel = DatagramChannel.open();
            //DatagramSocket datagramSocket = datagramChannel.socket();
            datagramChannel.bind(new InetSocketAddress("localhost", 3333));
            datagramChannel.configureBlocking(false);
            datagramChannel.register(selector, SelectionKey.OP_READ);
            ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
            System.out.println("Server is active.");
            //noinspection InfiniteLoopStatement
            while (true) {

                    selector.select();
                   // Set<SelectionKey> selectedKeys = selector.selectedKeys();
                   Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    //SocketAddress address = datagramChannel.receive(byteBuffer);
                    //if(address!=null) {
                    //    System.out.println("got something for ya!");
                    //    answerCommand(byteBuffer,address);
                   // }
                    while(keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if(key.isReadable()) answerCommand(byteBuffer,key);

                    }
                   /* while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        System.out.println(key.toString());
                        try {
                        if(!key.isValid())
                            continue;
                        if (key.isAcceptable()) {
                            register(selector, datagramChannel);
                        }

                        if (key.isReadable()) {
                            answerCommand(byteBuffer, key);
                        }
                        iter.remove();
                        }catch (IOException e){
                            System.out.println(e.getMessage());
                        }
                    }*/

            }
/*

            DatagramSocket datagramSocket = new DatagramSocket(3333);
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            System.out.println("Ожидаем данные...");

            //noinspection InfiniteLoopStatement
            while(true)
            {
                //Получаем данные
                datagramSocket.receive(incoming);
                byte[] data = incoming.getData();
                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(data));
                byte[] response;
                try {
                    CommandReader.UserCommand userCommand = (CommandReader.UserCommand) iStream.readObject();
                    System.out.println("Сервер получил: " + userCommand.toString());

                    //Отправляем данные клиенту
                    try {
                        userClass.execute(userCommand);
                        response = outputStream.toByteArray();
                    } catch (IllegalArgumentException e) {
                        response = e.getMessage().getBytes();
                    }
                } catch (StreamCorruptedException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    response = "Server received invalid packet. Please try again.".getBytes();
                }
                DatagramPacket dp = new DatagramPacket(response , response.length , incoming.getAddress() , incoming.getPort());
                outputStream.reset();
                datagramSocket.send(dp);
            } */
        } catch (IOException e)
        {
            System.err.println("Exception " + e);
            e.printStackTrace();
        }

        ClientClass serverClass = new ClientClass(ClientClass.allCommands, System.out, System.in);

        //noinspection InfiniteLoopStatement
        for(;;)
        serverClass.execute(serverClass.getCommandReader().readCommandFromBufferedReader());
    }

     private static void answerCommand(ByteBuffer buffer, SelectionKey key/*, SocketAddress address*/)
             throws IOException {

         DatagramChannel client = (DatagramChannel) key.channel();
         SocketAddress address = client.receive(buffer);
         try {
             client.connect(address);
         } catch (AlreadyConnectedException ignored) {}
         byte[] data = buffer.array();
         ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(data));
         byte[] response;
         try {
             CommandReader.UserCommand userCommand = (CommandReader.UserCommand) iStream.readObject();
             System.out.println(/*address.toString()+*/": " + userCommand.toString()+".");

             //Отправляем данные клиенту
             try {
                 userClass.execute(userCommand);
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

         client.write(ByteBuffer.wrap(response));
         buffer.clear();
     }

      private static void register(Selector selector, DatagramChannel serverSocket)
             throws IOException {
         serverSocket.configureBlocking(false);
         serverSocket.register(selector, SelectionKey.OP_READ);
     }

     public static Process start() throws IOException, InterruptedException {
         String javaHome = System.getProperty("java.home");
         String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
         String classpath = System.getProperty("java.class.path");
         String className = Server.class.getCanonicalName();

         ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);

         return builder.start();
     }
}
