package com.github.kpavlov.jreactive8583.server;

import com.github.kpavlov.jreactive8583.IsoMessageListener;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;
import io.netty.channel.ChannelHandlerContext;

/**
 * @program: netty-iso8583
 * @description:
 * @author: xfzhu
 * @create: 2020-02-21 16:48
 **/
public class ServerDemo {
    public static void main(String[] args)throws Exception{
        MessageFactory<IsoMessage> messageFactory = ConfigParser.createDefault();// [1]
        Iso8583Server<IsoMessage> server = new Iso8583Server<>(13055, messageFactory);// [2]

        server.addMessageListener(new IsoMessageListener() {

            @Override
            public boolean applies(IsoMessage isoMessage) {
                return true;
            }

            @Override
            public boolean onMessage(ChannelHandlerContext ctx, IsoMessage isoMessage) {
                //capturedRequest = isoMessage;
                System.out.println("The server get the message!");
                final IsoMessage response = server.getIsoMessageFactory().createResponse(isoMessage);
                response.setField(39, IsoType.ALPHA.value("00", 2));
                response.setField(60, IsoType.LLLVAR.value("XXX", 3));
                ctx.writeAndFlush(response);
                return false;
            }
        });
        //server.getConfiguration().replyOnError(true);// [4]
        server.init();// [5]

        server.start();// [6]
        if (server.isStarted()) { // [7]
            System.out.println("Server started succeed!");
        }
        //server.shutdown();// [8]
    }
}
