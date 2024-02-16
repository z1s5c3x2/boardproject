package com.application.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter
@Builder @ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_TB")
public class UserEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long seq;

    @Column
    private String name;
}
