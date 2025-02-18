package com.rider.it_request_service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomPageWithStatisticsDTO<T> {
    private List<RequestAdminBoardDTO> content;
    private long totalElements;
    private long totalPages;
    private int size;
    private int page;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private boolean empty;
    private long pendingRequests;
    private long inProgressRequests;
    private long resolvedRequests;
    private long rejectedRequests;
}
