package org.f1x.store;

import org.f1x.util.buffer.Buffer;

public final class NullMessageStore implements MessageStore {

    public static final NullMessageStore INSTANCE = new NullMessageStore();

    private NullMessageStore() {
    }

    @Override
    public void write(int seqNum, long sendingTime, CharSequence msgType, Buffer body, int offset, int length) {
    }

    @Override
    public int read(int seqNum, Visitor visitor) {
        return 0;
    }

    @Override
    public void read(int fromSeqNum, int toSeqNum, Visitor visitor) {
    }

    @Override
    public void clear() {
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
