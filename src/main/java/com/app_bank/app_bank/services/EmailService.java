package com.app_bank.app_bank.services;

import com.app_bank.app_bank.dto.EmailDetails;

public interface EmailService {

    void sendMailAlert(EmailDetails emailDetails);
    void sendMailWithAttachment(EmailDetails emailDetails);
}
