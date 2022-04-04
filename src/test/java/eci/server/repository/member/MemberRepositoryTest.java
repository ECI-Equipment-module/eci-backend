package eci.server.repository.member;

import eci.server.entity.member.Member;
import eci.server.entity.member.MemberRole;
import eci.server.entity.member.Role;
import eci.server.entity.member.RoleType;
<<<<<<< HEAD
import eci.server.exception.member.sign.MemberNotFoundException;
=======
<<<<<<< HEAD
import eci.server.exception.member.sign.MemberNotFoundException;
=======
import eci.server.exception.member.MemberNotFoundException;
import eci.server.repository.member.MemberRepository;
import eci.server.repository.member.RoleRepository;
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
>>>>>>> c70f16eab3f2a48a8503ae9c3994e85054cfafb6
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void createAndReadTest() {
        // given
        Member member = createMember();

        // when
        memberRepository.save(member);
        clear();

        // then
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @Test
    void memberDateTest() {
        // given
        Member member = createMember();

        // when
        memberRepository.save(member);
        clear();

        // then
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        assertThat(foundMember.getCreatedAt()).isNotNull();
        assertThat(foundMember.getModifiedAt()).isNotNull();
        assertThat(foundMember.getCreatedAt()).isEqualTo(foundMember.getModifiedAt());
    }

    @Test
    void updateTest() {
        // given
        String updatedDepartment
                = "updated";
        Member member = memberRepository.save(createMember());
        clear();

        // when
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        foundMember.updateDepartment(updatedDepartment);
        clear();

        // then
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        assertThat(updatedMember.getDepartment()).isEqualTo(updatedDepartment);
    }

    @Test
    void deleteTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when
        memberRepository.delete(member);
        clear();

        // then
        assertThatThrownBy(() -> memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void findByEmailTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when
        Member foundMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(MemberNotFoundException::new);

        // then
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void findByUsernameTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when
        Member foundMember = memberRepository.findByUsername(member.getUsername()).orElseThrow(MemberNotFoundException::new);

        // then
        assertThat(foundMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    void uniqueEmailTest() {
        // given
        Member member = memberRepository.save(createMember("email1", "password1", "username1", "departmaent", "contact"));
        clear();

        // when, then
        assertThatThrownBy(() -> memberRepository.save(createMember(member.getEmail(), "password2", "username2", "ndepartment2", "contact")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void uniqueContactTest() {
        // given
        Member member = memberRepository.save(createMember("email1", "password1", "username1", "department1", "contact1" ));
        clear();

        // when, then
        assertThatThrownBy(() -> memberRepository.save(createMember("email2", "password2", "username2", "department", "contatct")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void existsByEmailTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when, then
        assertThat(memberRepository.existsByEmail(member.getEmail())).isTrue();
        assertThat(memberRepository.existsByEmail(member.getEmail() + "test")).isFalse();
    }

    @Test
    void existsByUsernameTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when, then
        assertThat(memberRepository.existsByUsername(member.getUsername())).isTrue();
        assertThat(memberRepository.existsByUsername(member.getUsername() + "test")).isFalse();
    }

    @Test
    void memberRoleCascadePersistTest() {
        // given
        List<RoleType> roleTypes = List.of(RoleType.ROLE_APPLICANT, RoleType.ROLE_APPROVAL, RoleType.ROLE_REVIEWER , RoleType.ROLE_ADMIN);
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        // when
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        Set<MemberRole> memberRoles = foundMember.getRoles();

        // then
        assertThat(memberRoles.size()).isEqualTo(roles.size());
    }

    @Test
    void memberRoleCascadeDeleteTest() {
        // given
        List<RoleType> roleTypes = List.of(RoleType.ROLE_APPLICANT, RoleType.ROLE_APPROVAL, RoleType.ROLE_REVIEWER , RoleType.ROLE_ADMIN);
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        List<MemberRole> result = em.createQuery("select mr from MemberRole mr", MemberRole.class).getResultList();
        assertThat(result.size()).isZero();
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    private Member createMemberWithRoles(List<Role> roles) {
        return new Member("email", "password", "username", "departmanet","contact", roles);
    }

    private Member createMember(String email, String password, String username, String department, String contact) {
        return new Member(email, password, username, department, contact, emptyList());
    }

    private Member createMember() {
        return new Member("email", "password", "username", "department", "contact", emptyList());
    }


}