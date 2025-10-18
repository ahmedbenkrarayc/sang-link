package com.sanglink.entity;

import com.sanglink.entity.enums.Need;
import com.sanglink.entity.enums.ReceiverStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "receivers")
@DiscriminatorValue("RECEIVER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Receiver extends User {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Need need;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReceiverStatus status;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donation> donations;
}
