package com.tgr.userservice.infrastructure.web;

public interface DTOs {
    record User(Long id, String name, String lastName) {}
}
