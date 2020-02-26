package com.github.kpavlov.jreactive8583.netty.codec;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

public class Iso8583Decoder extends ByteToMessageDecoder {

    private final MessageFactory messageFactory;

    public Iso8583Decoder(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    /**
     * Decodes ISO8583 message from {@link ByteBuf}.
     * <p>
     * Message body starts immediately, no length header,
     * see <code>"lengthFieldFameDecoder"</code> in
     * {@link com.github.kpavlov.jreactive8583.netty.pipeline.Iso8583ChannelInitializer#initChannel(Channel)}
     * </p>
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List out) throws Exception {
        System.out.println("Server get message");
        if (!byteBuf.isReadable()) {
            return;
        }
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        OutputStream fout = new FileOutputStream("E:\\00myProj\\0greatDemos\\iso8583\\server.out");
        fout.write(bytes, 0, bytes.length);


        final IsoMessage isoMessage = messageFactory.parseMessage(bytes, 0);
        if (isoMessage != null) {
            //noinspection unchecked
            out.add(isoMessage);
        } else {
            throw new ParseException("Can't parse ISO8583 message", 0);
        }
    }
}
