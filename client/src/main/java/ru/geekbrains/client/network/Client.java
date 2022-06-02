package ru.geekbrains.client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import ru.geekbrains.client.config.ClientConfig;
import ru.geekbrains.client.model.Message;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class Client {

    private final ClientConfig config = ClientConfig.getInstance();

    public void send(Message message, Consumer<String> callback) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap client = new Bootstrap();
            client.group(workerGroup);
            client.channel(NioSocketChannel.class);
            client.option(ChannelOption.SO_KEEPALIVE, true);
            client.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new JacksonEncoder(),
//                            new MessageEncoder(),
//                            new ObjectEncoder(),
                            new LineBasedFrameDecoder(80),
                            new StringDecoder(StandardCharsets.UTF_8),
                            new ClientHandler(message, callback)
                    );
                }
            });

            ChannelFuture future = client.connect(config.getHost(), config.getPort()).await();
            future.addListener(f -> {
                if (!f.isSuccess()) {
                    callback.accept("CONNECTION REFUSED");
                }
            });
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
