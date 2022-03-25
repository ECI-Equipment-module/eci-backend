package eci.server.factory.entity;


import eci.server.entity.member.Member;
import eci.server.entity.member.Role;

import java.util.List;

import static java.util.Collections.emptyList;

public class MemberFactory {

    public static Member createMember() {
        return new Member("email@email.com", "123456a!", "username", "adb", "01012345678",emptyList());
    }

    public static Member createMember(String email, String password, String username, String department, String contact) {
        return new Member(email, password, username, department, contact, emptyList());
    }

    public static Member createMemberWithRoles(List<Role> roles) {
        return new Member("email@email.com", "123456a!", "username", "adv", "01012345678",roles);
    }

}