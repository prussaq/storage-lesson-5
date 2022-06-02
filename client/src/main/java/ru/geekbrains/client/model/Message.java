package ru.geekbrains.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String type;
    private String name;
    private int length;
    private byte[] data;
}