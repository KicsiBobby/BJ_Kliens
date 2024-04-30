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
    @FXML private Label info;
    @FXML private Label PlayerErtek;
    @FXML private Label SzerverErtek;
    @FXML private StackPane Pane;
    @FXML private HBox PlayerCards;
    @FXML private HBox ServerCards;
    @FXML private TextField csatlakoz;
    @FXML private Button HitGomb;
    @FXML private Button ResetGomb;
    @FXML private Button StandGomb;
    @FXML private Button JoinGomb;
    @FXML private Button BetGomb;
    @FXML private Button ExitGomb;
    @FXML private Button AllinGomb;


    DatagramSocket socket = null;
    private int zseton = 0;
    private int tet = 0;
    private String ip = "10.201.2.4";
    private int port = 678;
    private String lap="";

    private String Kertek="0";
    private String Sertek="0";

    public void initialize() {
        try {
            socket = new DatagramSocket(678);
            zsOsszeg.setText("Összeg: " + zseton);
            HitGomb.setDisable(true);
            StandGomb.setDisable(true);
            ResetGomb.setDisable(true);
            BetGomb.setDisable(true);
            ExitGomb.setDisable(true);
            AllinGomb.setDisable(true);

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
        ip = ServerIp.getText();
        zseton= Integer.parseInt(csatlakoz.getText());
        kuld("join:"+String.valueOf(zseton),ip, port);
    }

    double lapX=500;
    double lapY=450;
    @FXML
    private void OnHitPressed() {
        kuld("hit",ip,port);
    }

    @FXML
    private void OnAllinPressed() {
        tet+=zseton;
        zseton=0;
        zsOsszeg.setText("Összeg: " + zseton);
        Tet.setText("Tét: " + tet);
    }
    @FXML
    private void onBetPressed(){
        if(tet>=5) {
            PlayerCards.getChildren().clear();
            ServerCards.getChildren().clear();
            info.setText("");
            HitGomb.setDisable(false);
            StandGomb.setDisable(false);
            ResetGomb.setDisable(true);
            BetGomb.setDisable(true);
            ExitGomb.setDisable(true);
            AllinGomb.setDisable(true);
            kuld("bet:"+tet,ip,port);
        }

    }
    @FXML
    private void OnStandPressed() {
        kuld("stand",ip,port);

    }
    @FXML
    private void onExitClick() {
        kuld("exit",ip,port);
        SzerverErtek.setText("");
        PlayerErtek.setText("");
    }

    //ImageView leforditott=new ImageView(new Image(getClass().getResourceAsStream("gray_back.png")));


    private void onFogad(String uzenet){
        String[] u = uzenet.split(":");
        System.out.print(uzenet+"\n");
        if(u[0].equals("joined")) {
            info.setText("Csatlakozva\nTétekre vár...");
            zsOsszeg.setText("Összeg: " + zseton);
            ResetGomb.setDisable(false);
            BetGomb.setDisable(false);
            JoinGomb.setDisable(true);
            ExitGomb.setDisable(false);
            AllinGomb.setDisable(false);

        }
        if(u[0].equals("balance")) {
            if(Integer.parseInt(u[1])> zseton) {
                info.setText("Gratulálok, nyertél!");
            } else {
                info.setText("Sajnálom, veszítettél.");
            }
            zseton=Integer.parseInt(u[1]);
            zsOsszeg.setText("Összeg: " + zseton);

            tet=0;
            Tet.setText("Tét: " + 0);

        }
        if(u[0].equals("end")) {
            HitGomb.setDisable(true);
            StandGomb.setDisable(true);
            ResetGomb.setDisable(false);
            BetGomb.setDisable(false);
            AllinGomb.setDisable(false);
            ExitGomb.setDisable(false);
            lapX=500;
            lapY=450;

        }
        if(u[0].equals("paid")) {
            info.setText("Csatlakozásra vár");
            zseton=Integer.parseInt(u[1]);
            zsOsszeg.setText("Összeg: " + 0);
            PlayerCards.getChildren().clear();
            ServerCards.getChildren().clear();
            HitGomb.setDisable(true);
            StandGomb.setDisable(true);
            ResetGomb.setDisable(true);
            BetGomb.setDisable(true);
            JoinGomb.setDisable(false);
            AllinGomb.setDisable(true);
        }

        if(u[0].equals("ertekK")) {
            Kertek=u[1];
            PlayerErtek.setText(Kertek);
        }
        if(u[0].equals("ertekS")) {
            Sertek=u[1];
            SzerverErtek.setText(Sertek);
        }

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
            if (u[0].equals("s")){
                lap=u[1];
                ImageView ujlap=new ImageView(new Image(getClass().getResourceAsStream(lap+".png")));
                ujlap.setFitWidth(200);
                ujlap.setFitHeight(150);
                ujlap.setPreserveRatio(true);
                ServerCards.getChildren().add(ujlap);
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