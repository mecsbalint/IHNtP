import { Developer, DeveloperWithId } from "../types/Developer";
import { GameForEdit, GameForGameProfile, GameForList, GameFormSubmit, GameToEdit, GameToAdd } from "../types/Game";
import { Publisher, PublisherWithId } from "../types/Publisher";
import { Tag, TagWithId } from "../types/Tag";
import { ApiResponse, apiRequest } from "./api";
import { addNewDevelopers } from "./developerService";
import { addNewPublishers } from "./publisherService";
import { addNewTags } from "./tagService";

export async function getAllGames() : Promise<GameForList[]> {
    const responseObj = await apiRequest<GameForList[]>({url: "/api/games/all"});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : [];
}

export async function getGameForProfile(id : number) : Promise<GameForGameProfile | null> {
    const responseObj = await apiRequest<GameForGameProfile>({url: `/api/games/profile/${id}`});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : null;
}

export async function getGameForEdit(id : number) : Promise<ApiResponse<GameForEdit>> {
    const responseObj = await apiRequest<GameForEdit>({url: `/api/games/edit/${id}`});

    if (responseObj.status !== 200) responseObj.body = null;

    return responseObj;;
}

export async function addNewGame(newGameObj : GameFormSubmit) : Promise<ApiResponse<number>> {
    const headerImg = typeof newGameObj.headerImg === "string" ? null : newGameObj.headerImg;
    const screenshots = newGameObj.screenshots.filter((screenshot) : screenshot is File => typeof screenshot !== "string");

    const gameToAdd : GameToAdd = await processEntityLists(newGameObj);

    const requestBody = createMultipartFormData<GameToAdd>(gameToAdd, headerImg, screenshots);

    const responseObj = await apiRequest<number>({url: "/api/games/add", method: "POST", body: requestBody});

    return responseObj;
}

export async function editGame(editGameObj : GameFormSubmit, id : number) : Promise<number> {
    const headerImg = typeof editGameObj.headerImg === "string" ? null : editGameObj.headerImg;
    const screenshots = editGameObj.screenshots.filter((screenshot) : screenshot is File => typeof screenshot !== "string");

    const gameToEdit = await processEntityLists(editGameObj);

    const requestBody = createMultipartFormData<GameToEdit>(gameToEdit, headerImg, screenshots);

    const responseObj = await apiRequest({url: `/api/games/edit/${id}`, method: "PUT", body: requestBody});

    return responseObj.status;
}

function createMultipartFormData<T extends GameToAdd | GameToEdit>(gameObj : T, headerImg : File | null, screenshots : File[]) : FormData {
    const formData = new FormData();

    formData.append("game", new Blob([JSON.stringify(gameObj)], {type: "application/json"}));
    headerImg && formData.append("headerImg", headerImg);
    screenshots.forEach(screenshot => formData.append("screenshots", screenshot));

    return formData;
}

async function processEntityLists(gameObj : GameFormSubmit) : Promise<GameToEdit> {
    const developerIds = await processEntities<[Developer, DeveloperWithId]>({entities: gameObj.developers, postFunction: addNewDevelopers});
    const publisherIds = await processEntities<[Publisher, PublisherWithId]>({entities: gameObj.publishers, postFunction: addNewPublishers});
    const tagIds = await processEntities<[Tag, TagWithId]>({entities: gameObj.tags, postFunction: addNewTags});

    return {
        ...gameObj,
        headerImg: typeof gameObj.headerImg === "string" ? gameObj.headerImg : "",
        screenshots: gameObj.screenshots.filter((screenshot) : screenshot is string => typeof screenshot === "string"),
        developerIds,
        publisherIds,
        tagIds
    }
}

type ProcessEntitiesParams<Entity, EntityWithId> = {
        entities: Array<Entity | EntityWithId>,
        postFunction: (entitiesToAdd: Entity[]) => Promise<number[]>
    };

async function processEntities<ParamTypes extends [Developer, DeveloperWithId] | [Publisher, PublisherWithId] | [Tag, TagWithId]>({entities, postFunction} : ProcessEntitiesParams<ParamTypes[0], ParamTypes[1]>) : Promise<number[]> {
    const entitiesToPost = entities.filter((entity): entity is ParamTypes[0] => !('id' in entity));
    const existingDeveloperIds = entities.filter((entity): entity is ParamTypes[1] => 'id' in entity).map(entity => entity.id);
    
    const createdIds = await postFunction(entitiesToPost);
    
    return [...createdIds, ...existingDeveloperIds];
}
