import { apiRequest } from "./api";

export async function getAllTags() {
    const responseObj = await apiRequest({url: "/api/tags/all"});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return [];
}
