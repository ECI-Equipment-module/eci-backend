package eci.server.NewItemModule.entity.attachment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentTag {
    @Id
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE3")
   @SequenceGenerator(name="SEQUENCE3", sequenceName="SEQUENCE3", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String module;
}
