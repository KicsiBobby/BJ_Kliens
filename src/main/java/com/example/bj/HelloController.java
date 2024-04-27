package com.example.bj;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class HelloController {

    @FXML private TextField ServerIp;
    @FXML private Label zsOsszeg;
    @FXML private Label Tet;
    @FXML private StackPane Pane;
    @FXML private ImageView Lap1;
    @FXML private ImageView Lap2;
    @FXML private HBox PlayerCards;
    @FXML private Button Hit;
    @FXML private TextField csatlakoz;

    DatagramSocket socket = null;

    private String zse = csatlakoz.getText();
    private int zseton = 500;
    private int tet = 0;



    private String ip = "25.49.193.131";
    private int port = 678;

    ArrayList<String> lapok = new ArrayList<>();

    private String lapbetu[]={"C","D","H","S"};
    private String lapszam[]={"2","3","4","5","6","7","8","9","A","K","J","Q"};

    private String lap="";


    public void initialize() {
        try {
            socket = new DatagramSocket(678);
            zsOsszeg.setText("Összeg: " + zseton);
            System.out.printf("%s",zse);


        } catch (SocketException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                fogad();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void OnClick100() {
        if (zseton >= 100) {
            tet += 100;
            zseton -= 100;
            Tet.setText("Tét: " + tet);
            zsOsszeg.setText("Összeg: " + zseton);
        }

    }

    @FXML
    private void OnClick50() {
        if (zseton >= 50) {
            tet += 50;
            zseton -= 50;
            Tet.setText("Tét: " + tet);
            zsOsszeg.setText("Összeg: " + zseton);
        }

    }

    @FXML
    private void OnClick25() {
        if (zseton >= 25) {
            tet += 25;
            zseton -= 25;
            Tet.setText("Tét: " + tet);
            zsOsszeg.setText("Összeg: " + zseton);
        }

    }

    @FXML
    private void OnClick5() {
        if (zseton >= 5) {
            tet += 5;
            zseton -= 5;
            Tet.setText("Tét: " + tet);
            zsOsszeg.setText("Összeg: " + zseton);
        }

    }

    @FXML
    private void OnResetPressed() {
        if(tet>0)
        zseton+=tet;
        tet=0;
        Tet.setText("Tét: " + tet);
        zsOsszeg.setText("Összeg: " + zseton);
    }

    @FXML
    private void Join() {
        kuld("join:"+String.valueOf(tet), ServerIp.getText(), 678);


    }

    //private void RandomLap() {
    //    String randomszam = lapszam[(int)(Math.random()*lapszam.length)];
    //    String randombetu = lapbetu[(int)(Math.random()*lapbetu.length)];
    //    lap=randomszam+randombetu;
    //    lapok.add(randomszam+randombetu);
    //
    //
    //}

    double lapX=360;
    double lapY=400;
    @FXML
    private void OnHitPressed() {
        kuld("hit",ip,port);
    }

    private void onHitPressed(){
        kuld("bet"+tet,ip,port);
    }

    private void onFogad(String uzenet){
        String[] u = uzenet.split(":");
        System.out.printf(uzenet);
        if (u.length>1){
            if (u[0].equals("k")){
                lap=u[1];
                ImageView ujlap=new ImageView(new Image(getClass().getResourceAsStream(lap+".png")));
                ujlap.setFitWidth(200);
                ujlap.setFitHeight(150);

                ujlap.setTranslateX(lapX);
                ujlap.setTranslateY(lapY);
                lapX=ujlap.getTranslateX()-70;
                lapY=ujlap.getTranslateY()-30;
                ujlap.setPreserveRatio(true);
                PlayerCards.getChildren().add(ujlap);

            }
        }
    }

    private void kuld(String uzenet, String ip, int port) {
        try {
            byte[] adat = uzenet.getBytes("utf-8");
            InetAddress ipv4 = Inet4Address.getByName(ip);
            DatagramPacket packet = new DatagramPacket(adat, adat.length, ipv4, port);
            socket.send(packet);
            System.out.printf("%s:%d -> %s\n", ip, port, uzenet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fogad() {
        byte[] data = new byte[256];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        while (true) {
            try {
                socket.receive(packet);
                String uzenet = new String(packet.getData(), 0, packet.getLength(), "utf-8");
                Platform.runLater(() -> onFogad(uzenet));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}