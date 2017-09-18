package org.efix.util.buffer;

import java.nio.ByteBuffer;


public interface Buffer {

    long addressOffset();

    int capacity();

    byte[] byteArray();

    ByteBuffer byteBuffer();

    long getLong(int index);

    int getInt(int index);

    short getShort(int index);

    byte getByte(int index);

    void getBytes(int index, byte[] dst);

    void getBytes(int index, byte[] dst, int dstOffset, int length);

    void getBytes(int index, MutableBuffer dstBuffer, int dstIndex, int length);

    void getBytes(int index, ByteBuffer dstBuffer, int length);

    void boundsCheck(int offset, int length);

}
