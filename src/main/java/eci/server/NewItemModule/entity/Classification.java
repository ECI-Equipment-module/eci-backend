package eci.server.NewItemModule.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(ClassificationId.class)
public class Classification {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification1_id")
    private Classification1 classification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification2_id")
    private Classification2 classification2;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification3_id")
    private Classification3 classification3;
}
