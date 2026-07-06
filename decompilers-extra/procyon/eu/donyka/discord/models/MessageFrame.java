// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.models;

import lombok.Generated;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import eu.donyka.discord.enums.OpCode;

public class MessageFrame
{
    byte[] headerBuffer;
    byte[] messageBuffer;
    private OpCode opCode;
    private int length;
    private String message;
    
    public MessageFrame() {
        this.headerBuffer = new byte[8];
        this.messageBuffer = new byte[65535 - this.headerBuffer.length];
    }
    
    public MessageFrame(final OpCode code, final String message) {
        this();
        this.opCode = code;
        this.message = message;
    }
    
    public boolean parseHeader() {
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(this.headerBuffer)) {
            this.opCode = OpCode.values()[this.readInt(inputStream)];
            this.length = this.readInt(inputStream);
            return true;
        }
        catch (final IOException ex) {
            return false;
        }
    }
    
    private int readInt(final InputStream stream) throws IOException {
        final int ch1 = stream.read();
        final int ch2 = stream.read();
        final int ch3 = stream.read();
        final int ch4 = stream.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + ch1;
    }
    
    public boolean parseMessage() {
        this.message = new String(Arrays.copyOfRange(this.messageBuffer, 0, this.length), StandardCharsets.UTF_8);
        return true;
    }
    
    public ByteBuffer write() {
        final byte[] d = this.message.getBytes(StandardCharsets.UTF_8);
        final ByteBuffer writeStream = ByteBuffer.allocate(d.length + 8);
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
    public void setOpCode(final OpCode opCode) {
        this.opCode = opCode;
    }
}
