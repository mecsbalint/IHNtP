import { apiRequest } from "./api";

export async function getAllDevelopers() {
    const responseObj = await apiRequest({url: "/api/developers/all"});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return [];
}

export async function addNewDevelopers(developersToAdd) {
    const responseObj = await apiRequest({url: "/api/developers/add", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(developersToAdd)});

    return responseObj.status === 200 ? responseObj.body : [];
}
