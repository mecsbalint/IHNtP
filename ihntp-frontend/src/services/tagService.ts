import { Tag, TagWithId } from "../types/Tag";
import { apiRequest } from "./api";

export async function getAllTags() : Promise<TagWithId[]> {
    const responseObj = await apiRequest<TagWithId[]>({url: "/api/tags/all"});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}

export async function addNewTags(tagsToAdd : Tag[], handleUnauthorizedResponse : () => void) : Promise<number[]> {
    const responseObj = await apiRequest<number[]>({url: "/api/tags/add", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(tagsToAdd), onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}
