package com.app_bank.app_bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementRequest {

    private String accountNumber;
    private String startDate;
    private String endDate;
}
