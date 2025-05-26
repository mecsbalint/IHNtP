import { apiRequest } from "./api";

export async function getAllTags() {
    const responseObj = await apiRequest({url: "/api/tags/all"});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return [];
}

export async function addNewTags(tagsToAdd) {
    const responseObj = await apiRequest({url: "/api/tags/add", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(tagsToAdd)});

    return responseObj.status === 200 ? responseObj.body : [];
}
