/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package eu.donyka.discord.models;

import eu.donyka.discord.enums.OpCode;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import lombok.Generated;

public class MessageFrame {
    byte[] headerBuffer = new byte[8];
    byte[] messageBuffer = new byte[65535 - this.headerBuffer.length];
    private OpCode opCode;
    private int length;
    private String message;

    public MessageFrame() {
    }

    public MessageFrame(OpCode code, String message) {
        this();
        this.opCode = code;
        this.message = message;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean parseHeader() {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(this.headerBuffer);){
            this.opCode = OpCode.values()[this.readInt(inputStream)];
            this.length = this.readInt(inputStream);
            boolean bl = true;
            return bl;
        }
        catch (IOException ex) {
            return false;
        }
    }

    private int readInt(InputStream stream) throws IOException {
        int ch4;
        int ch3;
        int ch2;
        int ch1 = stream.read();
        if ((ch1 | (ch2 = stream.read()) | (ch3 = stream.read()) | (ch4 = stream.read())) < 0) {
            throw new EOFException();
        }
        return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + ch1;
    }

    public boolean parseMessage() {
        this.message = new String(Arrays.copyOfRange(this.messageBuffer, 0, this.length), StandardCharsets.UTF_8);
        return true;
    }

    public ByteBuffer write() {
        byte[] d = this.message.getBytes(StandardCharsets.UTF_8);
        ByteBuffer writeStream = ByteBuffer.allocate(d.length + 8);
        writeStream.putInt(Integer.reverseBytes(this.opCode.ordinal()));
        writeStream.putInt(Integer.reverseBytes(d.length));
        writeStream.put(d);
        writeStream.rewind();
        return writeStream;
    }

    @Generated
    public byte[] getHeaderBuffer() {
        return this.headerBuffer;
    }

    @Generated
    public byte[] getMessageBuffer() {
        return this.messageBuffer;
    }

    @Generated
    public OpCode getOpCode() {
        return this.opCode;
    }

    @Generated
    public int getLength() {
        return this.length;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public void setOpCode(OpCode opCode) {
        this.opCode = opCode;
    }
}

