package ru.practicum.validation;


import ru.practicum.exception.CommentOwnerAndClaimToBeOwnerUserAreDifferentException;
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

    public static void validate(UserEntity commentOwner, UserEntity claimsToBeOwner)
            throws CommentOwnerAndClaimToBeOwnerUserAreDifferentException {
        validateCommentOwnerAndClaimsToBeOwnerAreSameUser(commentOwner, claimsToBeOwner);
    }

    private static void validateCommentOwnerAndClaimsToBeOwnerAreSameUser(UserEntity commentOwner, UserEntity claimsToBeOwner)
            throws CommentOwnerAndClaimToBeOwnerUserAreDifferentException {
        long ownerId = commentOwner.getId();
        long claimsToBeOwnerId = claimsToBeOwner.getId();

        if (ownerId != claimsToBeOwnerId) {
            String message = String
                    .format("Field: commenter. " +
                            "Error: commenter should not be with id. " +
                            "Value: %s", claimsToBeOwnerId);
            throw new CommentOwnerAndClaimToBeOwnerUserAreDifferentException(message);
        }
    }

    private static void validateCommenterIsNotInitiator(UserEntity commenterEntity, EventEntity eventEntity)
            throws CommenterAndInitiatorAreSameException {
        long requesterId = commenterEntity.getId();
        long initiatorId = eventEntity.getInitiator().getId();

        if (requesterId == initiatorId) {
            String message = String
                    .format("Field: requester. " +
                            "Error: requester should not be initiator. " +
                            "Value: %s", requesterId);
            throw new CommenterAndInitiatorAreSameException(message);
        }
    }

    private static void validateEventPublished(EventEntity eventEntity)
            throws EventIsNotPublishedException {
        EventState state = eventEntity.getState();

        if (!state.equals(EventState.PUBLISHED)) {
            String message = String
                    .format("Field: state. " +
                            "Error: state should be PUBLISHED. " +
                            "Value: %s", state);
            throw new EventIsNotPublishedException(message);
        }
    }
}

