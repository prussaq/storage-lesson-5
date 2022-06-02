package ru.geekbrains.client;

import ru.geekbrains.client.model.Message;
import ru.geekbrains.client.network.Client;

import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) throws Exception {
//        Consumer<String> saveToFile = response -> {
//            try {
//                Files.writeString(Path.of("client/response.txt"), response);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        };

        Message message = new Message("type", null, 5, new byte[]{1, 2, 3, 4, 5});
        new Thread(() -> new Client().send(message, s -> System.out.println("s = " + s))).start();

        char[] chars = {'|', '/', '-', '\\'};
        for (int i = 0 ; i < 100; i++) {
            System.out.print(chars[i % 4]);
            Thread.sleep(500);
            System.out.print('\b');
        }
    }
}
