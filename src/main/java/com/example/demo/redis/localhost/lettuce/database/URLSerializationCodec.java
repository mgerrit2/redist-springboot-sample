package com.example.demo.redis.localhost.lettuce.database;

import io.lettuce.core.codec.RedisCodec;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class URLSerializationCodec implements RedisCodec<String, URL> {

    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    public String decodeKey(ByteBuffer byteBuffer) {
        return charset.decode(byteBuffer).toString();
    }

    @Override
    public URL decodeValue(ByteBuffer byteBuffer) {

        try {
            byte[] array = new byte[byteBuffer.remaining()];
            byteBuffer.get(array);
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(array));
            return (URL) is.readObject();

        } catch (Exception e){
            return null;
        }

    }

    @Override
    public ByteBuffer encodeKey(String s) {
        return charset.encode(s);
    }

    @Override
    public ByteBuffer encodeValue(URL url) {

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bytes);
            os.writeObject(url.getUrl());

            return ByteBuffer.wrap(bytes.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

}
