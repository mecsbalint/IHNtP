import { Developer, DeveloperWithId } from "../types/Developer";
import { apiRequest } from "./api";

export async function getAllDevelopers() : Promise<DeveloperWithId[]> {
    const responseObj = await apiRequest<DeveloperWithId[]>({url: "/api/developers/all"});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}

export async function addNewDevelopers(developersToAdd : Developer[], handleUnauthorizedResponse : () => void) : Promise<number[]> {
    const responseObj = await apiRequest<number[]>({url: "/api/developers/add", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(developersToAdd), onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}
