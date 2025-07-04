import { GameForList, GameStatuses } from "../types/Game";
import { ApiResponse, apiRequest } from "./apiRequest";

type UserGameListType = "wishlist" | "backlog";

export async function getUserGameList(listType : UserGameListType) : Promise<ApiResponse<GameForList[]>> {
    const responseObj = await apiRequest<GameForList[]>({url: `/api/user/games/${listType}`});

    if (responseObj.status !== 200 || responseObj.body === null) responseObj.body = []

    return responseObj;
}

export async function getGameStatuses(gameId : number) : Promise<ApiResponse<GameStatuses>> {
    const responseObj = await apiRequest<GameStatuses>({url: `/api/user/games/status/${gameId}`});

    if (responseObj.status !== 200 || responseObj.body === null) responseObj.body = {inWishlist: null, inBacklog: null}

    return responseObj;
}

export async function updateUserList(method : "PUT" | "DELETE", listType : UserGameListType, gameId : number)  : Promise<number> {
    const responseObj = await apiRequest<number | void>({url: `/api/user/games/${listType}/${gameId}`, method: method});

    return responseObj.status;
}
