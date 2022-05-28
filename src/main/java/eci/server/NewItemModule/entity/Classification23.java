package eci.server.NewItemModule.entity;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Classification23 {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification12_id")
    private Classification12 classification12;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification3_id")
    private Classification3 classification3;


}
