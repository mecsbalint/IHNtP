import { Tag, TagWithId } from "../types/Tag";
import { apiRequest } from "./api";

export async function getAllTags() : Promise<TagWithId[]> {
    const responseObj = await apiRequest<TagWithId[]>({url: "/api/tags"});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}

export async function addNewTags(tagsToAdd : Tag[]) : Promise<number[]> {
    const responseObj = await apiRequest<number[]>({url: "/api/tags", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(tagsToAdd)});

    return responseObj.status === 201 && responseObj.body !== null ? responseObj.body : [];
}
