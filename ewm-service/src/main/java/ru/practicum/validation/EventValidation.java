package ru.practicum.validation;

import org.springframework.util.ObjectUtils;
import ru.practicum.dto.events.UpdateEventAdminRequest;
import ru.practicum.dto.events.UpdateEventUserRequest;
import ru.practicum.exception.*;
import ru.practicum.model.EventEntity;
import ru.practicum.model.EventState;
import ru.practicum.model.EventStateAdminAction;
import ru.practicum.model.UserEntity;

import java.time.LocalDateTime;

public class EventValidation {
    private static final long MIN_INTERVAL_FOR_CREATING_BEFORE_EVENT = 2;
    private static final long MIN_INTERVAL_FOR_PUBLISHING_BEFORE_EVENT = 1;


    public static void validate(EventEntity entity)
            throws EventDateBeforeTwoHoursException, EventDateInPastException {
        validateEventDateNotInPast(entity.getEventDate());
        validateEventStartsAfterTwoHours(entity.getEventDate());
    }

    public static void validate(UserEntity claimedInitiator, EventEntity entity, UpdateEventUserRequest dto)
            throws EventDateBeforeTwoHoursException,
            EventDateInPastException,
            IncorrectInitiatorException,
            EventIsAlreadyPublishedException {
        if (dto.getEventDate() == null) {
            return;
        }
        validateEventDateNotInPast(dto.getEventDate());
        validateEventStartsAfterTwoHours(dto.getEventDate());
        validateInitiator(claimedInitiator, entity);
        validateEventIsNotPublished(entity);
    }

    public static void validate(UpdateEventAdminRequest dto)
            throws EventDateInPastException, EventDateBeforeOneHourFromPublishingException {
        if (dto.getEventDate() == null) {
            return;
        }
        validateEventDateNotInPast(dto.getEventDate());
        validateEventStartsAfterOneHourFromPublishing(dto.getEventDate());
    }

    public static void validateState(EventEntity entity, UpdateEventAdminRequest dto)
            throws EventIsAlreadyPublishedException,
            EventIsAlreadyCanceledException {
        EventStateAdminAction state = dto.getStateAction();
        if (state == null) {
            return;
        }
        if (state.equals(EventStateAdminAction.PUBLISH_EVENT)) {
            validateEventIsNotPublished(entity);
            validateEventIsNotCanceledBeforePublishing(entity);
            return;
        }
        validateEventIsNotPublishedBeforeCanceling(entity);
    }

    public static void validateInitiator(UserEntity claimedInitiator, EventEntity entity)
            throws IncorrectInitiatorException {
        long claimedInitiatorId = claimedInitiator.getId();
        long initiatorId = entity.getInitiator().getId();
        String message = String
                .format("Field: eventDate. " +
                        "Error: wrong initiator id. " +
                        "Value: %s", claimedInitiatorId);

        if (initiatorId != claimedInitiatorId) {
            throw new IncorrectInitiatorException(message);
        }
    }

    public static void validateRangeStartIsBeforeRangeEnd(LocalDateTime rangeStart, LocalDateTime rangeEnd)
            throws RangeStartIsAfterRangeEndException {
        if (ObjectUtils.isEmpty(rangeStart) || ObjectUtils.isEmpty(rangeEnd)) {
            return;
        }
        String message = String
                .format("Field: eventDate. " +
                        "Error: wrong initiator id. " +
                        "Value:rangeStart %s " +
                        "Value:rangeEnd %s ", rangeStart, rangeEnd);

        if (rangeStart.isAfter(rangeEnd)) {
            throw new RangeStartIsAfterRangeEndException(message);
        }
    }

    public static void validateEventIsPublished(EventEntity entity)
            throws EventIsNotPublishedForPublicException {
        EventState state = entity.getState();
        String message = String.format("Cannot access the event because it's not in the right state: %s", state);

        if (!state.equals(EventState.PUBLISHED)) {
            throw new EventIsNotPublishedForPublicException(message);
        }
    }

    private static void validateEventStartsAfterTwoHours(LocalDateTime eventDate)
            throws EventDateBeforeTwoHoursException {
        LocalDateTime timeAfterWhichEventCanStart = LocalDateTime.now().plusHours(MIN_INTERVAL_FOR_CREATING_BEFORE_EVENT);
        String message = String
                .format("Field: eventDate. " +
                        "Error: should contain date which is after 2 hours from current time. " +
                        "Value: %s", eventDate);

        if (eventDate.isBefore(timeAfterWhichEventCanStart)) {
            throw new EventDateBeforeTwoHoursException(message);
        }
    }

    private static void validateEventStartsAfterOneHourFromPublishing(LocalDateTime eventDate)
            throws EventDateBeforeOneHourFromPublishingException {
        LocalDateTime timeAfterWhichEventCanStart = LocalDateTime.now().plusHours(MIN_INTERVAL_FOR_PUBLISHING_BEFORE_EVENT);
        String message = String
                .format("Field: eventDate. " +
                        "Error: should contain date which is after 1 hour from publishing time. " +
                        "Value: %s", eventDate);

        if (eventDate.isBefore(timeAfterWhichEventCanStart)) {
            throw new EventDateBeforeOneHourFromPublishingException(message);
        }
    }

    private static void validateEventDateNotInPast(LocalDateTime eventDate) throws EventDateInPastException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String message = String
                .format("Field: eventDate. " +
                        "Error: should contain date which is not in past. " +
                        "Value: %s", eventDate);

        if (eventDate.isBefore(currentDateTime)) {
            throw new EventDateInPastException(message);
        }
    }

    private static void validateEventIsNotPublished(EventEntity entity)
            throws EventIsAlreadyPublishedException {
        EventState state = entity.getState();
        String message = String.format("Cannot publish the event because it's not in the right state: %s", state);

        if (state.equals(EventState.PUBLISHED)) {
            throw new EventIsAlreadyPublishedException(message);
        }
    }

    private static void validateEventIsNotCanceledBeforePublishing(EventEntity entity)
            throws EventIsAlreadyCanceledException {
        EventState state = entity.getState();
        String message = String.format("Cannot publish the event because it's not in the right state: %s", state);

        if (state.equals(EventState.CANCELED)) {
            throw new EventIsAlreadyCanceledException(message);
        }
    }

    private static void validateEventIsNotPublishedBeforeCanceling(EventEntity entity)
            throws EventIsAlreadyPublishedException {
        EventState state = entity.getState();
        String message = String.format("Cannot cancel the event because it's not in the right state: %s", state);

        if (state.equals(EventState.PUBLISHED)) {
            throw new EventIsAlreadyPublishedException(message);
        }
    }
}

