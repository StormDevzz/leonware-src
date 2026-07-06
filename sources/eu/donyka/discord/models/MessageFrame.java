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

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/models/MessageFrame.class */
public class MessageFrame {
    byte[] headerBuffer;
    byte[] messageBuffer;
    private OpCode opCode;
    private int length;
    private String message;

    @Generated
    public byte[] getHeaderBuffer() {
        return this.headerBuffer;
    }

    @Generated
    public byte[] getMessageBuffer() {
        return this.messageBuffer;
    }

    @Generated
    public void setOpCode(OpCode opCode) {
        this.opCode = opCode;
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

    public MessageFrame() {
        this.headerBuffer = new byte[8];
        this.messageBuffer = new byte[65535 - this.headerBuffer.length];
    }

    public MessageFrame(OpCode code, String message) {
        this();
        this.opCode = code;
        this.message = message;
    }

    public boolean parseHeader() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(this.headerBuffer);
            Throwable th = null;
            try {
                this.opCode = OpCode.values()[readInt(inputStream)];
                this.length = readInt(inputStream);
                if (inputStream != null) {
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        inputStream.close();
                    }
                }
                return true;
            } finally {
            }
        } catch (IOException e) {
            return false;
        }
    }

    private int readInt(InputStream stream) throws IOException {
        int ch1 = stream.read();
        int ch2 = stream.read();
        int ch3 = stream.read();
        int ch4 = stream.read();
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
        byte[] d = this.message.getBytes(StandardCharsets.UTF_8);
        ByteBuffer writeStream = ByteBuffer.allocate(d.length + 8);
        writeStream.putInt(Integer.reverseBytes(this.opCode.ordinal()));
        writeStream.putInt(Integer.reverseBytes(d.length));
        writeStream.put(d);
        writeStream.rewind();
        return writeStream;
    }
}
