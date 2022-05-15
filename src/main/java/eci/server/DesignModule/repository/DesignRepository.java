package eci.server.DesignModule.repository;

import eci.server.DesignModule.entity.design.Design;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ProjectModule.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignRepository extends JpaRepository<Design, Long> {

    List<Design> findByProject(Project project);

    List<Design> findByMember(Member member);



}
