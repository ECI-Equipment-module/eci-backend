package eci.server.DocumentModule.repository;

import eci.server.DocumentModule.entity.Document;
import eci.server.ItemModule.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByMember(Member member);


}
