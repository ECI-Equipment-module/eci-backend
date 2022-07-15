package eci.server.ReleaseModule.entity;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cofeatures.CoEffect;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReleaseOrgReleaseId implements Serializable {
    private ReleaseOrganization releaseOrganization;
    private Release release;
}

