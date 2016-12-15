package org.efix.connector.channel;

import org.efix.connector.ConnectionException;
import org.efix.util.SocketUtil;
import org.efix.util.buffer.Buffer;
import org.efix.util.buffer.MutableBuffer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Works only with buffers that are wrapped over ByteBuffer with offset 0.
 */
public class NioSocketChannel implements Channel {

    protected final SocketAddress localAddress;
    protected final SocketAddress remoteAddress;
    protected final SocketChannel channel;

    public NioSocketChannel(SocketChannel channel) {
        this.localAddress = SocketUtil.getLocalAddress(channel);
        this.remoteAddress = SocketUtil.getRemoteAddress(channel);
        this.channel = channel;
    }

    @Override
    public int read(MutableBuffer buffer, int offset, int length) {
        ByteBuffer byteBuffer = byteBuffer(buffer, offset, length);
        try {
            int bytesRead = channel.read(byteBuffer);
            if (bytesRead == -1)
                throw new ConnectionException("An existing connection was forcibly closed by the remote host", localAddress, remoteAddress);

            return bytesRead;
        } catch (IOException e) {
            throw new ConnectionException(localAddress, remoteAddress, e);
        }
    }

    @Override
    public int write(Buffer buffer, int offset, int length) {
        ByteBuffer byteBuffer = byteBuffer(buffer, offset, length);
        try {
            return channel.write(byteBuffer);
        } catch (IOException e) {
            throw new ConnectionException(localAddress, remoteAddress, e);
        }
    }

    protected ByteBuffer byteBuffer(Buffer buffer, int offset, int length) {
        ByteBuffer byteBuffer = buffer.byteBuffer();
        byteBuffer.limit(offset + length).position(offset);
        return byteBuffer;
    }

}
