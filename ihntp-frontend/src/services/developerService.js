import { apiRequest } from "./api";

export async function getAllDevelopers() {
    const responseObj = await apiRequest({url: "/api/developers/all"});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return [];
}
