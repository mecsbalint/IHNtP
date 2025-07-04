import { Publisher, PublisherWithId } from "../types/Publisher";
import { apiRequest } from "./apiRequest";

export async function getAllPublishers() : Promise<PublisherWithId[]> {
    const responseObj = await apiRequest<PublisherWithId[]>({url: "/api/publishers"});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}

export async function addNewPublishers(publishersToAdd : Publisher[]) : Promise<number[]> {
    const responseObj = await apiRequest<number[]>({url: "/api/publishers", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(publishersToAdd)});

    return responseObj.status === 201 && responseObj.body !== null ? responseObj.body : [];
}
