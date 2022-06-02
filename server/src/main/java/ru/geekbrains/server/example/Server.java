package ru.geekbrains.server.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;

public class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new Server(9999).run();
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup);
            server.channel(NioServerSocketChannel.class);
            server.option(ChannelOption.SO_BACKLOG, 128);
            server.childOption(ChannelOption.SO_KEEPALIVE, true);
            server.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(8 * 1024, 32 * 1024));
            server.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(
                            new JacksonDecoder(),
//                            new MessageDecoder(),
//                            new ObjectDecoder(1024 * 1024, ClassResolvers.cacheDisabled(null)),
                            new StringEncoder(StandardCharsets.UTF_8),
                            new ServerHandler()
                    );
                }
            });

            ChannelFuture future = server.bind(port).sync();
            System.out.println("Server started on port " + port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
