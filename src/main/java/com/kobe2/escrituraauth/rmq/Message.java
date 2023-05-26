package com.kobe2.escrituraauth.rmq;

public record Message(String email, String code, boolean verified) {
}
