package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(9998);

        System.out.println("Servidor simples pronto, ouvindo a porta 9998!");

        while (true) {
            Socket s = null;

            try {
                s = ss.accept();
                System.out.println("Novo Cliente Conectado : " + s);
                DataInputStream dis1 = new DataInputStream(s.getInputStream());
                DataOutputStream dos1 = new DataOutputStream(s.getOutputStream());
                System.out.println("Atribuindo novo thread para este cliente");
                Thread t = new SimpleClientHandler(s, dis1, dos1);
                System.out.println("Thread do cliente " + s.getRemoteSocketAddress() + ": " + t.getName());
                t.start();
            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
}
