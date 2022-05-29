package eci.server.NewItemModule.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClassificationId implements Serializable {

    private Classification1 classification1;

    private Classification2 classification2;

    private Classification3 classification3;
}
