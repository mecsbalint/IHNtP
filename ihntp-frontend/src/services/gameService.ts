import { Developer, DeveloperWithId } from "../types/Developer";
import { GameForEdit, GameForGameProfile, GameForList, GameFormSubmit, GameToAdd } from "../types/Game";
import { Publisher, PublisherWithId } from "../types/Publisher";
import { Tag, TagWithId } from "../types/Tag";
import { apiRequest } from "./api";
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

export async function getGameForEdit(id : number, handleUnauthorizedResponse : () => void) : Promise<GameForEdit | null> {
    const responseObj = await apiRequest<GameForEdit>({url: `/api/games/edit/${id}`, onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status === 200 && responseObj.body !== null ? responseObj.body : null;
}

export async function addNewGame(newGameObj : GameFormSubmit, handleUnauthorizedResponse : () => void) {
    const gameToAdd = await processEntityLists(newGameObj, handleUnauthorizedResponse);

    const responseObj = await apiRequest({url: "/api/games/add", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(gameToAdd), onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status === 200 ? responseObj.body : null;
}

export async function editGame(editGameObj : GameFormSubmit, id : number, handleUnauthorizedResponse : () => void) {
    const gameToEdit = await processEntityLists(editGameObj, handleUnauthorizedResponse);

    const responseObj = await apiRequest({url: `/api/games/edit/${id}`, method: "PUT", headers: {"Content-Type": "application/json"}, body: JSON.stringify(gameToEdit), onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status === 200;
}

async function processEntityLists(gameObj : GameFormSubmit, handleUnauthorizedResponse : () => void) : Promise<GameToAdd> {
    const developerIds = await processEntities<[Developer, DeveloperWithId]>({entities: gameObj.developers, postFunction: addNewDevelopers, handleUnauthorizedResponse});
    const publisherIds = await processEntities<[Publisher, PublisherWithId]>({entities: gameObj.publishers, postFunction: addNewPublishers, handleUnauthorizedResponse});
    const tagIds = await processEntities<[Tag, TagWithId]>({entities: gameObj.tags, postFunction: addNewTags, handleUnauthorizedResponse});

    return {
        ...gameObj,
        developerIds,
        publisherIds,
        tagIds
    }
}

type ProcessEntitiesParams<Entity, EntityWithId> = {
        entities: Array<Entity | EntityWithId>,
        postFunction: (entitiesToAdd: Entity[], handleUnauthorizedResponse: () => void) => Promise<number[]>,
        handleUnauthorizedResponse : () => void
    };

async function processEntities<ParamTypes extends [Developer, DeveloperWithId] | [Publisher, PublisherWithId] | [Tag, TagWithId]>({entities, postFunction, handleUnauthorizedResponse} : ProcessEntitiesParams<ParamTypes[0], ParamTypes[1]>) : Promise<number[]> {
    const entitiesToPost = entities.filter((entity): entity is ParamTypes[0] => !('id' in entity));
    const existingDeveloperIds = entities.filter((entity): entity is ParamTypes[1] => 'id' in entity).map(entity => entity.id);
    
    const createdIds = await postFunction(entitiesToPost, handleUnauthorizedResponse);
    
    return [...createdIds, ...existingDeveloperIds];
}
