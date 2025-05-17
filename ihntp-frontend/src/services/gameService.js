import { apiRequest } from "./api";

export async function getAllGames() {
    const responseObj = await apiRequest({url: "/api/games/all"});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return [];
}

export async function getGame(id) {
    const responseObj = await apiRequest({url: `/api/games/${id}`});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return null;
}
