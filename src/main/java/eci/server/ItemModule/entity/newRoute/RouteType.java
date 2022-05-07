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

//Route Type은 itemType에 있는 routeType따라서 지정이 되는 방식
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RouteType {
    @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
//   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
//   @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    private Long id;

    @Column
    private String name;

    @Column
    private String module;

    @Column
    private String todo;
}
