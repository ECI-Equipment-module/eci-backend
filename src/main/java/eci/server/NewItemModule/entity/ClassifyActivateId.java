package eci.server.NewItemModule.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClassifyActivateId implements Serializable {
    private Classification classification;
    private ActivateAttributes activateAttributes;
}
