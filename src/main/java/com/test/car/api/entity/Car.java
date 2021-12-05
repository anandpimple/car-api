package com.test.car.api.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE car SET deleted_on = NOW() WHERE id=?")
@Where(clause = "deleted_on is null")
public class Car implements Serializable {
    private static final long serialVersionUID = -6290217624709449329L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String businessId;

    @Column(nullable = false)
    private String colour;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String make;

    private Date deletedOn = null;
}
