import { apiRequest } from "./api";

export async function getUserGameList(listType, handleUnauthorizedResponse) {
    const responseObj = await apiRequest({url: `/api/user/games/${listType}`, onUnauthorizedResponse: handleUnauthorizedResponse});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return [];
}

export async function getGameStatuses(gameId, handleUnauthorizedResponse) {
    const responseObj = await apiRequest({url: `/api/user/games/status/${gameId}`, onUnauthorizedResponse: handleUnauthorizedResponse});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return {
        inWishlist: null,
        inBacklog: null
    };
}

export function updateUserList(method, listType, gameId, handleUnauthorizedResponse) {
    const responseObj = apiRequest({url: `/api/user/games/${listType}/${gameId}`, method: method, onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status;
}
