package Map;
import com.sun.tools.javac.Main;
import javafx.application.Platform;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {
    private static Socket socket;
    private static BufferedReader in;
    public  static ArrayList<String> data;
    public  static ArrayList<String> dataSuns = new ArrayList<>();
    public  static String ip = "192.168.251.211" , message;
    private static PrintWriter out;

    static {
        socket = null;
        try {
            socket = new Socket(ip, 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            data = connectToServer();

            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (line.equals("START")) {
                            System.out.println("Server says START — starting game!");
                            message = line;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can't connect");
        }
    }

    public static void sendMessage(String message) {
        out.println(message);
    }

    private static ArrayList<String> connectToServer() {
        try {
            String response = in.readLine();
            String[] given = response.split("sun");
            String[] zombieData = given[0].split("\\|");
            String[] sunData = given[1].split("\\|");
            data = new ArrayList<>();

            for (int i = 0; i < zombieData.length; i++) {//col,type of each elements
                data.add(zombieData[i]);
            }
            for (int i = 0; i < sunData.length; i++) {
                dataSuns.add(sunData[i]);
            }

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

