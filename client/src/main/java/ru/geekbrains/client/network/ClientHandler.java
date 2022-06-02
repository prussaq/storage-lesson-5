package ru.geekbrains.client.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.geekbrains.client.model.Message;

import java.util.function.Consumer;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final Message message;
    private final Consumer<String> callback;

    public ClientHandler(Message message, Consumer<String> callback) {
        this.message = message;
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String response = (String) msg;
        callback.accept(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
