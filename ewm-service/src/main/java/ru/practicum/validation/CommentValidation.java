package ru.practicum.validation;


import ru.practicum.exception.CommenterAndInitiatorAreSameException;
import ru.practicum.exception.EventIsNotPublishedException;
import ru.practicum.model.EventEntity;
import ru.practicum.model.EventState;
import ru.practicum.model.UserEntity;


public class CommentValidation {

    public static void validate(UserEntity commenterEntity, EventEntity eventEntity)
            throws
            EventIsNotPublishedException, CommenterAndInitiatorAreSameException {
        validateCommenterIsNotInitiator(commenterEntity, eventEntity);
        validateEventPublished(eventEntity);
    }

    private static void validateCommenterIsNotInitiator(UserEntity commenterEntity, EventEntity eventEntity)
            throws CommenterAndInitiatorAreSameException {
        long requesterId = commenterEntity.getId();
        long initiatorId = eventEntity.getInitiator().getId();
        String message = String
                .format("Field: requester. " +
                        "Error: requester should not be initiator. " +
                        "Value: %s", requesterId);

        if (requesterId == initiatorId) {
            throw new CommenterAndInitiatorAreSameException(message);
        }
    }

    private static void validateEventPublished(EventEntity eventEntity)
            throws EventIsNotPublishedException {
        EventState state = eventEntity.getState();
        String message = String
                .format("Field: state. " +
                        "Error: state should be PUBLISHED. " +
                        "Value: %s", state);

        if (!state.equals(EventState.PUBLISHED)) {
            throw new EventIsNotPublishedException(message);
        }
    }
}

