package ru.zaroyan.draftcloudstorage.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "files")
public class FileEntity implements Serializable {
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
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "bytes",nullable = false)
    private byte[] bytes;

    @ManyToOne
    @JoinColumn(name = "user_login", referencedColumnName = "login")
    private UserEntity user;

    @PrePersist
    protected void create() {
        this.created = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + id +
                ", created=" + created +
                ", name='" + name + '\'' +
                ", fileType='" + fileType + '\'' +
                ", size=" + size +
                ", bytes=" + Arrays.toString(bytes) +
                ", user=" + user +
                '}';
    }
}


