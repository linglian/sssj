package tool;

//********************************************

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

//*类名:ByteLoad
//*作者:凌恋      时间:2016-8-8 15:26:08
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public abstract class ByteLoad {
    public static ByteBuffer getByteBuffer(InputStream in, int channelSize)
            throws IOException {
        ReadableByteChannel channel = Channels.newChannel(in);
        ByteBuffer chunkBuffer = ByteBuffer.allocate(channelSize);
        chunkBuffer.order(ByteOrder.LITTLE_ENDIAN);
        channel.read(chunkBuffer);
        chunkBuffer.position(0);
        return chunkBuffer;
    }
}
