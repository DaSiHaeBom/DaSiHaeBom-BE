package com.project.dasihaebom.domain.auth.service.command;

public interface AuthCommandService {

    void savePassword(Object user, String encodedPassword);
}
