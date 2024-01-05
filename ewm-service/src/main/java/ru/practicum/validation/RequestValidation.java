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

        if (requesterId != claimedRequesterId) {
            String message = String
                    .format("Field: requester. " +
                            "Error: wrong requester id. " +
                            "Value: %s", claimedRequesterId);
            throw new RequesterAndClaimedRequesterAreNotSameException(message);
        }

    }

    private static void validateRequesterIsNotInitiator(UserEntity requesterEntity, EventEntity eventEntity)
            throws RequesterAndInitiatorAreSameException {
        long requesterId = requesterEntity.getId();
        long initiatorId = eventEntity.getInitiator().getId();

        if (requesterId == initiatorId) {
            String message = String
                    .format("Field: requester. " +
                            "Error: requester should not be initiator. " +
                            "Value: %s", requesterId);
            throw new RequesterAndInitiatorAreSameException(message);
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

    private static void validateRequestPending(RequestEntity requestEntity) throws RequestIsNotPendingException {
        RequestStatus status = requestEntity.getStatus();

        if (!status.equals(RequestStatus.PENDING)) {
            String message = String
                    .format("Field: status. " +
                            "Error: status should be PENDING. " +
                            "Value: %s", status);
            throw new RequestIsNotPendingException(message);
        }
    }

    private static void validateParticipationLimitIsNotReached(EventEntity eventEntity, long confirmedRequests)
            throws ParticipationLimitReachedException {
        long participationLimit = eventEntity.getParticipantLimit();

        if (participationLimit != 0 && participationLimit <= confirmedRequests) {
            String message = String
                    .format("Field: participationLimit. " +
                            "Error: participation limit is reached. " +
                            "Value: %s", participationLimit);
            throw new ParticipationLimitReachedException(message);
        }
    }
}

