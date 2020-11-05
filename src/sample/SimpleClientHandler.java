package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class SimpleClientHandler extends Thread {
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    public SimpleClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String str, resultado = "", operador;
        String[] valores;

        while (true) {

            try {

                operador = dis.readUTF();
                System.out.println("Operador: " + operador);
                if (operador.equals("sair")) {
                    System.out.println("Cliente " + this.s + " enviou sair...");
                    System.out.println("Fechando esta conexão");
                    this.s.close();
                    System.out.println("Conexão Fechada");
                    break;
                }
                if (operador != null) {
                    if (operador.equals("+") || operador.equals("-") || operador.equals("*") || operador.equals("/")) {
                        dos.writeUTF("Ok");
                    }

                    str = dis.readUTF();
                    System.out.println("Operação: " + str);
                    valores = str.trim().split(" ");
                    double v1, v2;
                    switch (operador) {
                        case "+":
                            v1 = Double.parseDouble(valores[0]);
                            v2 = Double.parseDouble(valores[2]);
                            resultado = String.valueOf(v1 + v2);
                            dos.writeUTF(resultado);
                            break;

                        case "-":
                            v1 = Double.parseDouble(valores[0]);
                            v2 = Double.parseDouble(valores[2]);
                            resultado = String.valueOf(v1 - v2);
                            dos.writeUTF(resultado);
                            break;

                        case "*":
                            v1 = Double.parseDouble(valores[0]);
                            v2 = Double.parseDouble(valores[2]);
                            resultado = String.valueOf(v1 * v2);
                            dos.writeUTF(resultado);
                            break;

                        case "/":
                            v1 = Double.parseDouble(valores[0]);
                            v2 = Double.parseDouble(valores[2]);
                            ;
                            if (v2 != 0) {
                                resultado = String.valueOf(v1 / v2);

                            } else {
                                resultado = "ERROR: X/0";
                            }
                            dos.writeUTF(resultado);
                            break;
                    }
                } else {
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.dis.close();
            this.dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
