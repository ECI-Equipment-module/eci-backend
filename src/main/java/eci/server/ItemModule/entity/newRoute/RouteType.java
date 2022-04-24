package eci.server.ItemModule.entity.newRoute;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//RouteType <= 권한과, next
//        Type name
//        소속 모듈
//        소속 단계
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RouteType {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String module;

    @Column
    private String todo;
}
