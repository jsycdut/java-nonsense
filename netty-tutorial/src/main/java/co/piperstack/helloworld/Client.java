package co.piperstack.helloworld;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class Client {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup clientEventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(clientEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ClientEventHandler());
                        }
                    });
            System.out.println("client is ready");
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9098).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            clientEventLoopGroup.shutdownGracefully();
        }
    }

    private static class ClientEventHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(Unpooled.copiedBuffer("go go go, this is not gopher", CharsetUtil.UTF_8));
            System.out.println("client: message sent");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //super.channelRead(ctx, msg);
            ByteBuf incoming = (ByteBuf)msg;
            System.out.println("client: received message from server " + ctx.channel().remoteAddress() + " " + incoming.toString(CharsetUtil.UTF_8));
        }
    }
}
