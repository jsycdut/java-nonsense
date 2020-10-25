package com.piperstack.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.util.CharsetUtil;

public class ServerSideChannelHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("server channel active");
  }
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println("great, ServerSideChannelHandler got message " + msg);
    System.out.println("server channel read");
    String s = ((ByteBuf)msg).toString(CharsetUtil.UTF_8);
    System.out.println("reveived message " + s);
    if ("SHUTDOWN".equals(s)) {
      System.out.println("going to shutdown");


      EventLoop el = ctx.channel().eventLoop();
      EventLoopGroup parent = el.parent();

      System.out.println("el " + el);
      System.out.println("parent " + parent);

      parent.shutdownGracefully();
      el.shutdownGracefully();
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    System.out.println("server read complete");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.out.println("server exception caught");
  }
}
