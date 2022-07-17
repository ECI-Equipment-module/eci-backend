package eci.server.DocumentModule.entity.classification;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DocClassificationId implements Serializable {

    private DocClassification1 docClassification1;

    private DocClassification2 docClassification2;

}

