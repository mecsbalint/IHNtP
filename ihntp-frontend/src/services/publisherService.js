import { apiRequest } from "./api";

export async function getAllPublishers() {
    const responseObj = await apiRequest({url: "/api/publishers/all"});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return [];
}
