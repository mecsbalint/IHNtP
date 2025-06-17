import { Publisher, PublisherWithId } from "../types/Publisher";
import { apiRequest } from "./api";

export async function getAllPublishers() : Promise<PublisherWithId[]> {
    const responseObj = await apiRequest<PublisherWithId[]>({url: "/api/publishers/all"});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}

export async function addNewPublishers(publishersToAdd : Publisher[], handleUnauthorizedResponse : () => void) : Promise<number[]> {
    const responseObj = await apiRequest<number[]>({url: "/api/publishers/add", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(publishersToAdd), onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}
