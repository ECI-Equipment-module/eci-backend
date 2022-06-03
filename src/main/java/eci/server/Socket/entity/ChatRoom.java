package eci.server.Socket.entity;

import eci.server.ItemModule.entitycommon.EntityDate;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;

public class ChatRoom extends EntityDate {
    @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;
    private Set<WebSocketSession> sessions = new HashSet<>();
}
