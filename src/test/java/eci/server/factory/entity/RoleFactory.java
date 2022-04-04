package eci.server.factory.entity;

import eci.server.entity.member.Role;
import eci.server.entity.member.RoleType;

public class RoleFactory {
    public static Role createRole() {
        return new Role(RoleType.ROLE_NORMAL);
    }
}