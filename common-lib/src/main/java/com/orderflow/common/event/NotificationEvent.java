package com.orderflow.common.event;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {

	private UUID eventId;
	private Long notificationId;
    private String recipientEmail;
    private String subject;
    private String messageBody;
    private String notificationType;
    private LocalDateTime timestamp;
	
}
