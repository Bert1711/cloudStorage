package ru.zaroyan.draftcloudstorage.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "created", nullable = false, updatable = false)
    LocalDateTime created;


    @Column(name = "file_name")
    @NotBlank
    String name;


    @Column(name = "file_type", nullable = false)
    String fileType;

    @Column(nullable = false)
    long size;


    @Lob
    @Column(nullable = false)
    byte[] bytes;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "username")
    UserEntity owner;

    @PrePersist
    protected void create() {
        this.created = LocalDateTime.now();
    }

}


