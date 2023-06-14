package com.kobe2.escrituraauth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobe2.escrituraauth.records.UserRecord;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestWrapper extends HttpServletRequestWrapper {
    private byte[] cachedBody;
    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedServletInputStream(this.cachedBody);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return super.getReader();
    }

    public UserRecord toUserRecord() throws IOException {
        return new ObjectMapper().readValue(this.cachedBody, UserRecord.class);
    }
}
