import { GameForList, GameStatuses } from "../types/Game";
import { apiRequest } from "./api";

type UserGameListType = "wishlist" | "backlog";

export async function getUserGameList(listType : UserGameListType, handleUnauthorizedResponse : () => void) : Promise<GameForList[]> {
    const responseObj = await apiRequest<GameForList[]>({url: `/api/user/games/${listType}`, onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}

export async function getGameStatuses(gameId : number, handleUnauthorizedResponse : () => void) : Promise<GameStatuses> {
    const responseObj = await apiRequest<GameStatuses>({url: `/api/user/games/status/${gameId}`, onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : {inWishlist: null, inBacklog: null};
}

export async function updateUserList(method : "PUT" | "DELETE", listType : UserGameListType, gameId : number, handleUnauthorizedResponse : () => void)  : Promise<number> {
    const responseObj = await apiRequest<number | void>({url: `/api/user/games/${listType}/${gameId}`, method: method, onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status;
}
