package ru.zaroyan.draftcloudstorage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private Long id;

    @Column(length = 150, nullable = false)
    private String nameFile;

    @Enumerated(EnumType.STRING)
    private FileType typeFile;

    @Column(nullable = false)
    private Integer sizeFile;


    @ManyToOne(fetch = FetchType.LAZY) // Аннотация @ManyToOne с FetchType.LAZY для отложенной загрузки связанного пользователя.
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // Уточняем свойства связи ManyToOne и задаем поле user_id для связи.
    @JsonIgnore
    private UserEntity user;
}


