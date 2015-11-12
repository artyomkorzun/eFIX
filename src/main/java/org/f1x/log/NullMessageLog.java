package org.f1x.log;

import org.f1x.util.buffer.Buffer;

public final class NullMessageLog implements MessageLog {

    public static final NullMessageLog INSTANCE = new NullMessageLog();

    private NullMessageLog() {
    }

    @Override
    public void log(boolean inbound, Buffer buffer, int offset, int length) {

    }

    @Override
    public void open() {
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() {
    }

}
