package ru.practicum.validation;


import ru.practicum.exception.*;
import ru.practicum.model.*;


public class RequestValidation {

    public static void validate(UserEntity requesterEntity, EventEntity eventEntity, long confirmedRequests)
            throws RequesterAndInitiatorAreSameException,
            EventIsNotPublishedException,
            ParticipationLimitReachedException {
        validateRequesterIsNotInitiator(requesterEntity, eventEntity);
        validateEventPublished(eventEntity);
        validateParticipationLimitIsNotReached(eventEntity, confirmedRequests);
    }

    public static void validate(UserEntity claimedRequesterEntity, RequestEntity requestEntity)
            throws RequesterAndClaimedRequesterAreNotSameException {
        validateRequesterAndClaimedRequesterAreSame(claimedRequesterEntity, requestEntity);
    }

    public static void validate(RequestEntity request,
                                EventEntity eventEntity,
                                long confirmedRequests)
            throws ParticipationLimitReachedException, RequestIsNotPendingException {
        validateParticipationLimitIsNotReached(eventEntity, confirmedRequests);
        validateRequestPending(request);
    }

    private static void validateRequesterAndClaimedRequesterAreSame(UserEntity claimedRequesterEntity,
                                                                    RequestEntity requestEntity)
            throws RequesterAndClaimedRequesterAreNotSameException {
        long claimedRequesterId = claimedRequesterEntity.getId();
        long requesterId = requestEntity.getRequester().getId();
        String message = String
                .format("Field: requester. " +
                        "Error: wrong requester id. " +
                        "Value: %s", claimedRequesterId);

        if (requesterId != claimedRequesterId) {
            throw new RequesterAndClaimedRequesterAreNotSameException(message);
        }

    }

    private static void validateRequesterIsNotInitiator(UserEntity requesterEntity, EventEntity eventEntity)
            throws RequesterAndInitiatorAreSameException {
        long requesterId = requesterEntity.getId();
        long initiatorId = eventEntity.getInitiator().getId();
        String message = String
                .format("Field: requester. " +
                        "Error: requester should not be initiator. " +
                        "Value: %s", requesterId);

        if (requesterId == initiatorId) {
            throw new RequesterAndInitiatorAreSameException(message);
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

    private static void validateRequestPending(RequestEntity requestEntity) throws RequestIsNotPendingException {
        RequestStatus status = requestEntity.getStatus();
        String message = String
                .format("Field: status. " +
                        "Error: status should be PENDING. " +
                        "Value: %s", status);

        if (!status.equals(RequestStatus.PENDING)) {
            throw new RequestIsNotPendingException(message);
        }
    }

    private static void validateParticipationLimitIsNotReached(EventEntity eventEntity, long confirmedRequests)
            throws ParticipationLimitReachedException {
        long participationLimit = eventEntity.getParticipantLimit();
        String message = String
                .format("Field: participationLimit. " +
                        "Error: participation limit is reached. " +
                        "Value: %s", participationLimit);

        if (participationLimit != 0 && participationLimit <= confirmedRequests) {
            throw new ParticipationLimitReachedException(message);
        }
    }
}

