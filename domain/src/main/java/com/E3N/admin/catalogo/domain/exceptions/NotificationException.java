package com.E3N.admin.catalogo.domain.exceptions;

import com.E3N.admin.catalogo.domain.validation.handler.Notification;

public class NotificationException extends DomainException {
    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
}
