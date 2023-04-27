/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.ipc;

import com.google.gson.JsonObject;
import net.sascha123789.djava.ipc.enums.IPCType;

import java.nio.ByteBuffer;

public class IPCPackage {
    private JsonObject data;
    private IPCType op;

    public IPCPackage(IPCType op, JsonObject data) {
        this.op = op;
        this.data = data;
    }

    public JsonObject getData() {
        return data;
    }

    public IPCType getOp() {
        return op;
    }

    public byte[] toByteArr() {
        byte[] arr = data.toString().getBytes();
        ByteBuffer buff = ByteBuffer.allocate(arr.length + 2*Integer.BYTES);
        buff.putInt(Integer.reverseBytes(op.ordinal()));
        buff.putInt(Integer.reverseBytes(arr.length));
        buff.put(arr);
        return buff.array();
    }
}
