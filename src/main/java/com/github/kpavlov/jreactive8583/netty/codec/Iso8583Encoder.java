package com.github.kpavlov.jreactive8583.netty.codec;

import com.solab.iso8583.IsoMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

@ChannelHandler.Sharable
public class Iso8583Encoder extends MessageToByteEncoder<IsoMessage> {

    private final int lengthHeaderLength;

    public Iso8583Encoder(int lengthHeaderLength) {
        this.lengthHeaderLength = lengthHeaderLength;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, IsoMessage isoMessage, ByteBuf out) {
        if (lengthHeaderLength == 0) {
            byte[] bytes = isoMessage.writeData();
            out.writeBytes(bytes);
        } else {
            try {
                OutputStream fout = new FileOutputStream("E:\\00myProj\\0greatDemos\\iso8583\\req.out");
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                isoMessage.write(baos,lengthHeaderLength);
                byte req[]=baos.toByteArray();
                fout.write(req);
                out.writeBytes(req,0,req.length);
            }catch (Exception e){

            }

        }
    }
}
