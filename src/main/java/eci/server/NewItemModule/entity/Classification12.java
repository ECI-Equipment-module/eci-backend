package eci.server.NewItemModule.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(ClassificationId12.class)
public class Classification12 {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification1_id")
    private Classification1 classification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification2_id")
    private Classification2 classification2;


}
