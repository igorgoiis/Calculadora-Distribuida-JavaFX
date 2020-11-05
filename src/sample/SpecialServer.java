package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class SpecialServer extends Thread {
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(9999);

        System.out.println("Servidor especial pronto, ouvindo a porta 9999!");

        while (true) {
            Socket s = null;
            try {
                s = ss.accept();
                System.out.println("Novo Cliente Conectado : " + s);
                DataInputStream dis2 = new DataInputStream(s.getInputStream());
                DataOutputStream dos2 = new DataOutputStream(s.getOutputStream());
                System.out.println("Atribuindo novo tópico para este cliente");
                Thread t = new SpecialClientHandler(s, dis2, dos2);
                System.out.println("Thread do cliente " + s.getRemoteSocketAddress() + ": " + t.getName());
                t.start();
            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
}
