package com.example.demo.redis.localhost.lettuce.database.codec;

import io.lettuce.core.codec.RedisCodec;

import java.net.URL;
import java.nio.ByteBuffer;

public class URLSerializationCodec implements RedisCodec<String, URL> {


    @Override
    public String decodeKey(ByteBuffer byteBuffer) {
        return "";
    }

    @Override
    public URL decodeValue(ByteBuffer byteBuffer) {
        return null;
    }

    @Override
    public ByteBuffer encodeKey(String s) {
        return null;
    }

    @Override
    public ByteBuffer encodeValue(URL url) {
        return null;
    }
}
