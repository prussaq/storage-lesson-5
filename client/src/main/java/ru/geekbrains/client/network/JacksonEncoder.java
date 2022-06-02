package ru.geekbrains.client.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.geekbrains.client.model.Message;

public class JacksonEncoder extends MessageToByteEncoder<Message> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        out.writeBytes(mapper.writeValueAsBytes(msg));
        System.out.println(new String(mapper.writeValueAsBytes(msg)));
    }
}
