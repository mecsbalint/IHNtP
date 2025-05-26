import { apiRequest } from "./api";

export async function getAllPublishers() {
    const responseObj = await apiRequest({url: "/api/publishers/all"});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return [];
}

export async function addNewPublishers(publishersToAdd) {
    const responseObj = await apiRequest({url: "/api/publishers/add", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(publishersToAdd)});

    return responseObj.status === 200 ? responseObj.body : [];
}
