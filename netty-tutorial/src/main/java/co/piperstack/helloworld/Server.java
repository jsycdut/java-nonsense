package co.piperstack.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ServerHelloWorldHandler());
                        }
                    });
            System.out.println("server side is ready, come on honey");
            ChannelFuture channelFuture = bootstrap.bind(9098).sync();
            Channel channel = channelFuture.channel();
            new Thread(() -> {
                try {
                    TimeUnit.MINUTES.sleep(1);
                    channel.close();
                    System.out.println("channel.close() called, this app should stop now");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "killer").start();
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static class ServerHelloWorldHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //super.channelRead(ctx, msg);
            ByteBuf incoming = (ByteBuf)msg;
            System.out.println("serverside received: " + ctx.channel().remoteAddress() + " : " + incoming.toString(CharsetUtil.UTF_8));
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            //super.channelReadComplete(ctx);
            ctx.writeAndFlush(Unpooled.copiedBuffer("man, I got you.", CharsetUtil.UTF_8)).addListener(ChannelFutureListener.CLOSE);
            //System.out.println("yeah, server read complete and finished its work");
            //System.out.println("yes, i'm trying to close the channel by ctx.close()");
            //ctx.close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            ctx.close();
        }
    }
}
