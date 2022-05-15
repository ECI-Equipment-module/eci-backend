//package eci.server.Socket.entity;
//
//import eci.server.ItemModule.entity.member.Member;
//import eci.server.ItemModule.entitycommon.EntityDate;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "message")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class ChatMessage extends EntityDate {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "message_id")
//    private Integer id;
//
//    private String content;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "conversation_id")
//    private Conversation conversation;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
//
//    public void Message(String content, Conversation conversation, Member member) {
//        this.content = content;
//        this.conversation = conversation;
//        this.member = member;
//    }
//
//
//}