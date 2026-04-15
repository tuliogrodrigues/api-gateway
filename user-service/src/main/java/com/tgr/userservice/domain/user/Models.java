package com.tgr.userservice.domain.user;

public interface Models {
    record User(Long id, String name, String lastName){}
}
