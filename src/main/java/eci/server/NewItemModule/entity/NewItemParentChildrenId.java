package eci.server.NewItemModule.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewItemParentChildrenId implements Serializable {

    private NewItem parent;
    private NewItem children;
}

