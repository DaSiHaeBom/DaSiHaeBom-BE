package com.project.dasihaebom.domain.user;

public enum Role {
    WORKER,
    CORP,
    ADMIN,
    ;

    public boolean isWorker() { return this == WORKER; }
    public boolean isCorp()   { return this == CORP; }
}
