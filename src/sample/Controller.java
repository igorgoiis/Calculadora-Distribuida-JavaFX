package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    Socket s1, s2;
    DataInputStream dis1, dis2;
    DataOutputStream dos1, dos2;

    @FXML
    public Label display;
    @FXML
    public BorderPane borderPane;

    public String valor1 = "", valor2 = "";
    public String operador, aux = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTimeout(() -> {
            Stage stage = (Stage) borderPane.getScene().getWindow();
            stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        }, 1000);

        try {
            InetAddress ip = InetAddress.getByName("localhost");

            s1 = new Socket(ip, 9998);
            s2 = new Socket(ip, 9999);

            dis1 = new DataInputStream(s1.getInputStream());
            dos1 = new DataOutputStream(s1.getOutputStream());
            dis2 = new DataInputStream(s2.getInputStream());
            dos2 = new DataOutputStream(s2.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    public void numeros(MouseEvent event) {
        display.setText("");
        String id = ((Control) event.getSource()).getId();
        String valor = "";

        switch (id) {
            case "um":
                valor = "1";
                break;
            case "dois":
                valor = "2";
                break;
            case "tres":
                valor = "3";
                break;
            case "quatro":
                valor = "4";
                break;
            case "cinco":
                valor = "5";
                break;
            case "seis":
                valor = "6";
                break;
            case "sete":
                valor = "7";
                break;
            case "oito":
                valor = "8";
                break;
            case "nove":
                valor = "9";
                break;
            case "zero":
                valor = "0";
                break;
            case "ponto":
                valor = ".";
                break;
        }

        if (operador == null) {
            valor1 += valor;
            display.setText(valor1);
        } else {
            valor2 += valor;
            display.setText(valor2);
            aux = valor1 + " " + operador + " " + valor2; //Protocolo de Envio
        }

    }

    public void operacao(MouseEvent event) {
        String id = ((Control) event.getSource()).getId();

        if (valor1 != null) {

            switch (id) {
                case "somar":
                    operador = "+";
                    display.setText(operador);
                    break;
                case "subtrair":
                    operador = "-";
                    display.setText(operador);
                    break;
                case "mult":
                    operador = "*";
                    display.setText(operador);
                    break;
                case "dividir":
                    operador = "/";
                    display.setText(operador);
                    break;
                case "pot":
                    operador = "^";
                    display.setText(operador);
                    break;
                case "porcent":
                    operador = "%";
                    display.setText(operador);
                    break;
                case "raiz":
                    operador = "√";
                    display.setText(operador);
                    aux = valor1 + " " + operador + " " + valor2;
                    break;
            }
        }
    }

    public void igual() throws IOException {

        if (operador.equals("+") || operador.equals("-") || operador.equals("*") || operador.equals("/")) {
            String op = operador;
            dos1.writeUTF(op);
            if (dis1.readUTF().trim().equals("Ok")) {
                dos1.writeUTF(aux);
                String result = dis1.readUTF();
                display.setText(result);
            }
        }

        if (operador.equals("^") || operador.equals("%") || operador.equals("√")) {
            if (operador.equals("^")) {
                display.setText("");
                String op = operador;
                dos2.writeUTF(op);
                if (dis2.readUTF().trim().equals("Ok")) {
                    dos2.writeUTF(aux);
                    aux = dis2.readUTF();
                    display.setText(aux);
                }
            }
            if (operador.equals("√")) {
                display.setText("");
                String op = operador;
                dos2.writeUTF(op);
                if (dis2.readUTF().trim().equals("Ok")) {
                    dos2.writeUTF(aux);
                    aux = dis2.readUTF();
                    display.setText(aux);
                }
            }
            if (operador.equals("%")) {
                display.setText("");
                String op = operador;
                dos2.writeUTF(op);
                if (dis2.readUTF().trim().equals("Ok")) {
                    dos2.writeUTF(aux);
                    aux = dis2.readUTF();
                    display.setText(aux);
                }
            }

        }
    }

    public void limpar(MouseEvent event) {
        String id = ((Control) event.getSource()).getId();
        if (id.equals("c")) {
            display.setText("0");
            valor1 = "";
            valor2 = "";
            operador = null;
        }
    }

    private void closeWindowEvent(WindowEvent event) {
        System.out.println("Fechando Calculadora");
        try {
            if (dis1 != null && dis2 != null) {
                dos1.writeUTF("sair");
                dos2.writeUTF("sair");
                dis1.close();
                dis2.close();
                dos1.close();
                dos2.close();
                s1.close();
                s2.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
