package com.kobe2.escrituraauth.rmq;

import com.kobe2.escrituraauth.enums.CodePurpose;

public record Message(String email, CodePurpose purpose, String code) {
}
