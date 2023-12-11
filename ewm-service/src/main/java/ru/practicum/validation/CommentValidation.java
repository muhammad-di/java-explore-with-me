package ru.practicum.validation;


import ru.practicum.exception.CommenterAndInitiatorAreSameException;
import ru.practicum.exception.EventIsNotPublishedException;
import ru.practicum.exception.ParticipationLimitReachedException;
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

//    public static void validate(UserEntity claimedRequesterEntity, RequestEntity requestEntity)
//            throws RequesterAndClaimedRequesterAreNotSameException {
//        validateRequesterAndClaimedRequesterAreSame(claimedRequesterEntity, requestEntity);
//    }

//    public static void validate(RequestEntity request,
//                                EventEntity eventEntity,
//                                long confirmedRequests)
//            throws ParticipationLimitReachedException, RequestIsNotPendingException {
//        validateParticipationLimitIsNotReached(eventEntity, confirmedRequests);
//        validateRequestPending(request);
//    }

//    private static void validateRequesterAndClaimedRequesterAreSame(UserEntity claimedRequesterEntity,
//                                                                    RequestEntity requestEntity)
//            throws RequesterAndClaimedRequesterAreNotSameException {
//        long claimedRequesterId = claimedRequesterEntity.getId();
//        long requesterId = requestEntity.getRequester().getId();
//        String message = String
//                .format("Field: requester. " +
//                        "Error: wrong requester id. " +
//                        "Value: %s", claimedRequesterId);
//
//        if (requesterId != claimedRequesterId) {
//            throw new RequesterAndClaimedRequesterAreNotSameException(message);
//        }
//
//    }

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

//    private static void validateRequestPending(RequestEntity requestEntity) throws RequestIsNotPendingException {
//        RequestStatus status = requestEntity.getStatus();
//        String message = String
//                .format("Field: status. " +
//                        "Error: status should be PENDING. " +
//                        "Value: %s", status);
//
//        if (!status.equals(RequestStatus.PENDING)) {
//            throw new RequestIsNotPendingException(message);
//        }
//    }
}

