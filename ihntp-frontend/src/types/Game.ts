import { Developer, DeveloperWithId } from "./Developer";
import { Publisher, PublisherWithId } from "./Publisher";
import { Tag, TagWithId } from "./Tag";

type Game = {
    name: string,
    releaseDate: string,
};

type GameWithId = Game & {
    id: number
}

export type GameForList = GameWithId & {
    headerImg: string,
    tags: TagWithId[]
};

type GamePrice = {
    currency: string,
    amount: number,
    shopUrl: string
}

type GamePrices = {
    current: GamePrice,
    allTime: GamePrice
}

export type GameForGameProfile = GameWithId & {
    descriptionLong: string,
    screenshots: string[],
    developers: DeveloperWithId[],
    publishers: PublisherWithId[],
    tags: TagWithId[],
    gamePrices: null | GamePrices
};

export type GameForGameProfileWithStatuses = GameForGameProfile & {
    inBacklog: boolean | null,
    inWishlist: boolean | null
}

export type GameForEdit = GameWithId & {
    descriptionShort: string,
    descriptionLong: string,
    headerImg: string,
    screenshots: string[],
    developers: DeveloperWithId[],
    publishers: PublisherWithId[],
    tags: TagWithId[]
};

export type GameFormSubmit = Game & {
    descriptionShort: string,
    descriptionLong: string,
    headerImg: string | File,
    screenshots: Array<string | File>,
    developers: Array<Developer | DeveloperWithId>,
    publishers: Array<Publisher | PublisherWithId>,
    tags: Array<Tag | TagWithId>
};

export type GameToAdd = Game & {
    descriptionShort: string,
    descriptionLong: string,
    developerIds: number[],
    publisherIds: number[],
    tagIds: number[],
    headerImg: string | null,
    screenshots: string[],
};

export type GameToEdit = GameToAdd;

export type GameStatuses = {
    inWishlist: boolean | null,
    inBacklog: boolean | null
}
