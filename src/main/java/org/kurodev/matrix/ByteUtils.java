package org.kurodev.matrix;

import java.nio.ByteBuffer;

public class ByteUtils {
    public static byte[] toByteArray(double d) {
        return ByteBuffer.allocate(Double.BYTES).putDouble(d).array();
    }

    public static byte[] toByteArray(int d) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(d).array();
    }


    public static double toDouble(byte[] d) {
        return ByteBuffer.wrap(d).getDouble();
    }
    public static int toInt(byte[] d) {
        return ByteBuffer.wrap(d).getInt();
    }
}
