/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prallelproject2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

class ServerClientThread extends Thread {

    public static ArrayList<String> trans2 = new ArrayList<>();
    Socket serverClient;
    public static String message = "";
    public static int pDay1;
    public static int pDay2;
    public static int pDay3;
    public static String amount = "";

    ServerClientThread(Socket inSocket) {
        serverClient = inSocket;

    }

    public void run() {
        try {
            DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
            DataInputStream inStream2 = new DataInputStream(serverClient.getInputStream());
            DataInputStream inStream3 = new DataInputStream(serverClient.getInputStream());
            DataInputStream inStream4 = new DataInputStream(serverClient.getInputStream());
            DataInputStream inStream5 = new DataInputStream(serverClient.getInputStream());
            DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
            String balance = "", op = "", client = "", toclient = "";

            while (!amount.equals("bye")) {

                amount = inStream.readUTF();
                client = inStream2.readUTF();
                op = inStream3.readUTF();
                balance = inStream4.readUTF();
                for (int i = 0; i < trans2.size(); i += 2) {
                    if (client.equals(trans2.get(i))) {
                        balance = Integer.toString(Integer.parseInt(balance) + Integer.parseInt(trans2.get(i + 1)));
                        trans2.remove(i + 1);
                        trans2.remove(i);
                    }

                }
                System.out.println("From " + client + ": Number is :" + amount);
                if ("deposite".equals(op)) {
                    balance = Integer.toString(Integer.parseInt(amount) + Integer.parseInt(balance));
                    message += client + " Performed " + op + " by amount = " + amount + "\n";
                } else if ("withdraw".equals(op)) {
                    if (Integer.parseInt(amount) < Integer.parseInt(balance) && Integer.parseInt(amount) < 7000 && "Client_A".equals(client) && pDay1 < 7000) {
                        balance = Integer.toString(Integer.parseInt(balance) - Integer.parseInt(amount));
                        pDay1 += Integer.parseInt(amount);
                        message += client + " Performed " + op + " by amount = " + amount + "\n";
                    } else if (Integer.parseInt(amount) < Integer.parseInt(balance) && Integer.parseInt(amount) < 7000 && "Client_B".equals(client) && pDay2 < 7000) {
                        balance = Integer.toString(Integer.parseInt(balance) - Integer.parseInt(amount));
                        pDay2 += Integer.parseInt(amount);
                        message += client + " Performed " + op + " by amount = " + amount + "\n";
                    } else if (Integer.parseInt(amount) < Integer.parseInt(balance) && Integer.parseInt(amount) < 7000 && "Client_C".equals(client) && pDay3 < 7000) {
                        balance = Integer.toString(Integer.parseInt(balance) - Integer.parseInt(amount));
                        pDay3 += Integer.parseInt(amount);
                        message += client + " Performed " + op + " by amount = " + amount + "\n";
                    } else if (Integer.parseInt(amount) > Integer.parseInt(balance)) {
                        JOptionPane.showMessageDialog(null, "You don't have enough money to withdraw");
                    } else if (Integer.parseInt(amount) > 7000 || pDay1 > 7000 && "Client_A".equals(client)) {
                        JOptionPane.showMessageDialog(null, client + " Can't Withdraw More Than 7000 Per Day");
                    } else if (Integer.parseInt(amount) > 7000 || pDay2 > 7000 && "Client_B".equals(client)) {
                        JOptionPane.showMessageDialog(null, client + " Can't Withdraw More Than 7000 Per Day");
                    } else if (Integer.parseInt(amount) > 7000 || pDay3 > 7000 && "Client_C".equals(client)) {
                        JOptionPane.showMessageDialog(null, client + " Can't Withdraw More Than 7000 Per Day");
                    }
                } else if ("transaction".equals(op)) {
                    if (Integer.parseInt(amount) < Integer.parseInt(balance)) {
                        toclient = inStream5.readUTF();
                        balance = Integer.toString(Integer.parseInt(balance) - Integer.parseInt(amount));
                        trans2.add(toclient);
                        trans2.add(amount);
                        message += client + " Performed " + op + " by amount = " + amount + " To " + toclient + "\n";

                    } else {
                        JOptionPane.showMessageDialog(null, "You don't have enough money to Transfer");
                    }
                }

                outStream.writeUTF(balance);
                outStream.flush();
            }

            inStream.close();

            inStream2.close();

            inStream3.close();

            inStream4.close();

            outStream.close();

            serverClient.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            System.out.println("Client -" + " exit!! ");
        }
    }
}
