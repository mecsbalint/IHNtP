import { Developer, DeveloperWithId } from "../types/Developer";
import { apiRequest } from "./apiRequest";

export async function getAllDevelopers() : Promise<DeveloperWithId[]> {
    const responseObj = await apiRequest<DeveloperWithId[]>({url: "/api/developers"});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}

export async function addNewDevelopers(developersToAdd : Developer[]) : Promise<number[]> {
    const responseObj = await apiRequest<number[]>({url: "/api/developers", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(developersToAdd)});

    return responseObj.status === 201 && responseObj.body !== null ? responseObj.body : [];
}
