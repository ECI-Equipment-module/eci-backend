package eci.server.ItemModule.entity.item;

import eci.server.NewItemModule.entity.Classification1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class ItemTypes {

    @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE1")
  @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true)
  private ItemType itemType;

  public ItemTypes(ItemType itemType) {
    this.itemType = itemType;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "classification1_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Classification1 classification1;

}
