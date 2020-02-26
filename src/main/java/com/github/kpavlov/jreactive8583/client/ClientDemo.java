package com.github.kpavlov.jreactive8583.client;

import com.github.kpavlov.jreactive8583.IsoMessageListener;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;
import io.netty.channel.ChannelHandlerContext;

/**
 * @program: netty-iso8583
 * @description:
 * @author: xfzhu
 * @create: 2020-02-21 16:41
 **/
public class ClientDemo {
    public static void main(String[] args)throws Exception{
        //MessageFactory<IsoMessage> messageFactory = ConfigParser.createDefault();// [1]
        MessageFactory messageFactory = new MessageFactory();
        //这里的参数关系到最终生成的报文格式
        messageFactory.setBinaryHeader(false);
        messageFactory.setConfigPath("j8583.xml");
        messageFactory.setBinaryFields(false);
        messageFactory.setUseBinaryBitmap(true);
        messageFactory.setForceStringEncoding(true);
        messageFactory.setUseBinaryMessages(false);
        Iso8583Client<IsoMessage> client = new Iso8583Client<>(messageFactory);// [2]
        String host="127.0.0.1";
        int port=13055;
        //new Iso8583Client<>(socketAddress, configuration, clientMessageFactory());

        client.addMessageListener(new IsoMessageListener() {

            @Override
            public boolean applies(IsoMessage isoMessage) {
                return true;
            }

            @Override
            public boolean onMessage(ChannelHandlerContext ctx, IsoMessage isoMessage) {
               IsoMessage capturedRequest = isoMessage;
               System.out.println("The client get the message!");
/*                final IsoMessage response = server.getIsoMessageFactory().createResponse(isoMessage);
                response.setField(39, IsoType.ALPHA.value("00", 2));
                response.setField(60, IsoType.LLLVAR.value("XXX", 3));
                ctx.writeAndFlush(response);*/
                return false;
            }
        });
        //client.getConfiguration().replyOnError(true);// [4]
        client.init();// [5]

        client.connect("127.0.0.1", 13055);// [6]
        if (client.isConnected()) { // [7]

            System.out.println("The client get the conn!");
            IsoMessage message = messageFactory.newMessage(0x200);
            //client.sendAsync(message);// [8]
            // or
            client.send(message);// [9]
            // or
           // client.send(message, 1, TimeUnit.SECONDS);// [10]
        }
        client.shutdown();// [11]
    }
}
