import { apiRequest } from "./api";
import { addNewDevelopers } from "./developerService";
import { addNewPublishers } from "./publisherService";
import { addNewTags } from "./tagService";

export async function getAllGames() {
    const responseObj = await apiRequest({url: "/api/games/all"});

    if (responseObj.status === 200) {
        return responseObj.body;
    }

    return [];
}

export async function getGameForProfile(id) {
    const responseObj = await apiRequest({url: `/api/games/profile/${id}`});

    return responseObj.status === 200 ? responseObj.body : null;
}

export async function getGameForEdit(id) {
    const responseObj = await apiRequest({url: `/api/games/edit/${id}`});

    return responseObj.status === 200 ? responseObj.body : null;
}

export async function addNewGame(newGameObj, handleUnauthorizedResponse) {
    await processEntityLists(newGameObj);

    const responseObj = await apiRequest({url: "/api/games/add", method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(newGameObj), onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status === 200 ? responseObj.body : null;
}

export async function editGame(editGameObj, id, handleUnauthorizedResponse) {
    await processEntityLists(editGameObj);

    const responseObj = await apiRequest({url: `/api/games/edit/${id}`, method: "PUT", headers: {"Content-Type": "application/json"}, body: JSON.stringify(editGameObj), onUnauthorizedResponse: handleUnauthorizedResponse});

    return responseObj.status === 200;
}

async function processEntityLists(gameObj) {
    gameObj.tagIds = await processEntities(gameObj.tags, addNewTags);
    delete gameObj.tags;
    gameObj.developerIds = await processEntities(gameObj.developers, addNewDevelopers);
    delete gameObj.developers;
    gameObj.publisherIds = await processEntities(gameObj.publishers, addNewPublishers);
    delete gameObj.publishers;
}

async function processEntities(entityList, postEntityFunction) {
    const entitiesToPost = entityList.filter(entity => !entity.id);

    const createdIds = await postEntityFunction(entitiesToPost);

    return [...createdIds, ...entityList.filter(entity => entity.id).map(entity => entity.id)];
}
