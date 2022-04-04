//package eci.server;
//
//import eci.server.entity.member.Member;
//import eci.server.entity.member.Role;
//import eci.server.entity.member.RoleType;
//import eci.server.exception.member.sign.RoleNotFoundException;
//import eci.server.exception.member.RoleNotFoundException;

//import eci.server.repository.member.MemberRepository;
//import eci.server.repository.member.RoleRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.annotation.Profile;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//@Profile("prod")
//public class InitDB {
//    private final RoleRepository roleRepository;
//    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @EventListener(ApplicationReadyEvent.class)
//    @Transactional
//    private void initTestAdmin() {
//        memberRepository.save(
//                new Member("admin@admin.com", passwordEncoder.encode("123456a!"), "name", "management", "01012345678",

//    public void initDB() {
//        log.info("initialize database");
//
//        initRole();
//        initTestAdmin();
//        initTestMember();
//    }
//
//    private void initRole() {
//        roleRepository.saveAll(
//                List.of(RoleType.values()).stream().map(roleType -> new Role(roleType)).collect(Collectors.toList())
//        );
//    }
//
//    private void initTestAdmin() {
//        memberRepository.save(
//                new Member("admin@admin.com", passwordEncoder.encode("123456a!"), "name", "management","01012345678",

//                        List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
//                                roleRepository.findByRoleType(RoleType.ROLE_ADMIN).orElseThrow(RoleNotFoundException::new)))
//        );
//    }

//}
//
//
////    public void initDB() {
////        log.info("initialize database");
//////
//////        initRole();
////        initTestAdmin();
////        initTestMember();
////    }
////
//////    private void initRole() {
//////        roleRepository.saveAll(
//////                List.of(RoleType.values()).stream().map(roleType -> new Role(roleType)).collect(Collectors.toList())
//////        );
//////    }
////
////    private void initTestAdmin() {
////        memberRepository.save(
////                new Member("admin@admin.com", passwordEncoder.encode("123456a!"), "name", "management", "01012345678",
////                        List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
////                                roleRepository.findByRoleType(RoleType.ROLE_ADMIN).orElseThrow(RoleNotFoundException::new)))
////        );
////    }
////
////    private void initTestMember() {
////        memberRepository.saveAll(
////                List.of(
////                        new Member(
////                                "member1@member.com",
////                                passwordEncoder.encode("123456a!"),
////                                "member1",
////                                "manage",
////                                "01011111111",
////                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new))),
////
////                        new Member(
////                                "member2@member.com",
////                                passwordEncoder.encode("123456a!"),
////                                "member2",
////                                "advertise2",
////                                "01022222222",
////                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new))))
////        );
////
////    }
////}

//
//    private void initTestMember() {
//        memberRepository.saveAll(
//                List.of(
//                        new Member(
//                                "member1@member.com",
//                                passwordEncoder.encode("123456a!"),
//                                "member1",
//                                "manage",
//                                "01011111111",
//                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new))),
//
//                        new Member(
//                                "member2@member.com",
//                                passwordEncoder.encode("123456a!"),
//                                "member2",
//                                "advertise2",
//                                "01022222222",
//                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new))))
//        );
//    }
//
//}

