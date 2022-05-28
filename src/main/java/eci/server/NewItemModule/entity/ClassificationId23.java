package eci.server.NewItemModule.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(ClassificationId23.class)
public class ClassificationId23 implements Serializable {

    private Classification12 classification12;
    private Classification3 classification3;

}
