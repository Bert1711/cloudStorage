package ru.zaroyan.draftcloudstorage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDto {

    @JsonProperty("filename")
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private LocalDateTime created;
    private String hash;
    private Long size;

}
