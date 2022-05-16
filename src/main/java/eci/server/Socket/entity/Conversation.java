//package eci.server.Socket.entity;
//
//import eci.server.ItemModule.entity.member.Member;
//import eci.server.ItemModule.entitycommon.EntityDate;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.messaging.Message;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Entity
//@Getter
//@Setter
//public class Conversation extends EntityDate {
//
//    @Id
//////  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
//    private Long id;
//
//    @OneToMany(mappedBy = "conversation" , cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Message> messages = new ArrayList<>();
//
//    private String text;
//
//
//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinTable(
//            name = "conversation_user",
//            joinColumns = @JoinColumn(name = "conversation_id"),
//            inverseJoinColumns = @JoinColumn(name = "member_id")
//    )
//    private Set<Member> users = new HashSet<>();
//
//    public Conversation() {
//    }
//
//    public void addUser(Member member){
//        this.users.add(member);
//    }
//
//}