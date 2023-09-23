package ru.zaroyan.draftcloudstorage.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;


    @Column(name = "file_name")
    @NotBlank
    private String name;


    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(nullable = false)
    private long size;


    @Lob
    @Column(nullable = false)
    private byte[] bytes;

    @ManyToOne
    @JoinColumn(name = "login", referencedColumnName = "login")
    private UserEntity user;

    @PrePersist
    protected void create() {
        this.created = LocalDateTime.now();
    }

}


