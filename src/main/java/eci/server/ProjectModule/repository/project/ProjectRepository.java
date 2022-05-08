package eci.server.ProjectModule.repository.project;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ProjectModule.entity.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository  extends JpaRepository<Project, Long>, CustomProjectRepository {

    Page<Project> findAll(Pageable pageable);

    Page<Project> findByMember(Member member, Pageable pageable);

    List<Project> findByItem(Item item);

    List<Project> findByMember(Member member);



}
