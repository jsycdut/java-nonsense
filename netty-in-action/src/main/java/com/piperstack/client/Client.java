package com.piperstack.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class Client {
  public static void main(String... args) throws Exception {
    Bootstrap bootstrap = new Bootstrap();
    EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    bootstrap.group(eventLoopGroup)
      .channel(NioSocketChannel.class)
      .handler(new ChannelInitializer<Channel>() {
        @Override
        protected void initChannel(Channel ch) throws Exception {
          ChannelPipeline pipeline = ch.pipeline();
          pipeline.addLast(new ClientSideChannelHandler());
        }
        
      });

    ChannelFuture cf = bootstrap.connect("127.0.0.1", 8190);
    cf.addListener(new ChannelFutureListener(){
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
          System.out.println("operation complete with future success, notify by " + cf.channel().eventLoop());
        } else {
          System.out.println("operation complete with future failed, notify by " + cf.channel().eventLoop());
          future.cause().printStackTrace();
        }
      }
    });

    Channel ch = cf.channel();

    System.out.println("sending raw message");
    ch.writeAndFlush("raw message");

    TimeUnit.SECONDS.sleep(3);
    System.out.println("sending buffer message");
    ch.writeAndFlush(Unpooled.copiedBuffer("Hello server side", CharsetUtil.UTF_8));

    TimeUnit.SECONDS.sleep(3);
    System.out.println("sending shutdown message");
    ch.writeAndFlush(Unpooled.copiedBuffer("SHUTDOWN", CharsetUtil.UTF_8));

    TimeUnit.SECONDS.sleep(10);
    System.out.println("shutdown client");
    eventLoopGroup.shutdownGracefully();
  }
}
