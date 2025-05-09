package com.hackethon.squad2.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "split_bills")
@AllArgsConstructor
@NoArgsConstructor
public class SplitBill implements Serializable {
    private static final long serialVersionUID = -428003792951985974L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "UUID")
    private UUID id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sof_id", nullable = false)
    private Sof sof;

    @Column(name = "title")
    private String title;

    @Column(name = "bill_img")
    private String billImg;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "subtotal_amount")
    private BigDecimal subtotalAmount;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "service_amount")
    private BigDecimal serviceAmount;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "others")
    private BigDecimal others;

    @Column(name = "bill_id")
    private String billId;

    @OneToMany(mappedBy = "splitBill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SplitBillUser> splitBillUsers;

}
