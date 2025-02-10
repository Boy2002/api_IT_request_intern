package com.rider.it_request.dto;

import com.rider.it_request.entity.Request;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestStatusHistoryDTO {


    private Integer historyId;

    private int requestId;

    private String changedBy;

    private Request.Status status;

    private String note;

    private LocalDateTime changedAt;

    private Integer refStatusHistory;



}
