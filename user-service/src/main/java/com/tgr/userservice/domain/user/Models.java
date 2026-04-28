package com.tgr.userservice.domain.user;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

public interface Models {

    enum Status {
        ACTIVE("Active"),
        BLOCKED("Blocked"),
        PENDING("Pending");

        private final String label;

        Status(String label) {
            this.label = label;
        }

        public String label() {
            return label;
        }
    }

    enum Role {
        USER("USER"),
        ADMIN("ADMIN"),
        ACCOUNT_MANAGER("ACCOUNT_MANAGER");

        private final String value;

        Role(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    @RecordBuilder
    @Table("users")
    record User(
            @Id Long id,
            String name,
            String lastName,
            String email,
            String taxId,
            String password,
            Status status,
            Role role,
            LocalDateTime createdAt,
            LocalDateTime lastLogin) {

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(lastName, user.lastName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, lastName, email);
        }
    }
}