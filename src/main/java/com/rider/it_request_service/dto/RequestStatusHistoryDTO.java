package com.rider.it_request_service.dto;

import com.rider.it_request_service.entity.Request;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
