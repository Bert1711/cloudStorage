package ru.zaroyan.draftcloudstorage.models;

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
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String nameFile;

    @Enumerated(EnumType.STRING)
    private FileType typeFile;

    @Column(nullable = false)
    private Integer sizeFile;

    @Lob // Аннотация @Lob для хранения больших объектов (например, контента файла).
    @Basic(fetch = FetchType.LAZY) // Аннотация @Basic с FetchType.LAZY для отложенной загрузки контента файла.
    private byte[] contentFile;

    @ManyToOne(fetch = FetchType.LAZY) // Аннотация @ManyToOne с FetchType.LAZY для отложенной загрузки связанного пользователя.
    @JoinColumn(name = "person_id", nullable = false) // Уточняем свойства связи ManyToOne и задаем поле user_id для связи.
    private Person user;
}


