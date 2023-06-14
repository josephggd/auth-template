package com.kobe2.escrituraauth.security;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.ws.rs.NotAuthorizedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class CachedServletInputStream extends ServletInputStream {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private InputStream cachedInputStream;

    public CachedServletInputStream(byte[] cachedBody) {
        this.cachedInputStream = new ByteArrayInputStream(cachedBody);
    }

    @Override
    public boolean isFinished() {
        logger.info("isFinished");
        try {
            return cachedInputStream.available() == 0;
        } catch (IOException e) {
            logger.warning(e.getMessage());
            throw new NotAuthorizedException("BAD AUTH");
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {

    }

    @Override
    public int read() throws IOException {
        return cachedInputStream.read();
    }
}
