/* eslint-disable react/prop-types */
import { useEffect, useRef, useState } from "react";
import { getAllTags } from "../../services/tagService";
import { getAllPublishers } from "../../services/publisherService";
import { getAllDevelopers } from "../../services/developerService";
import AddGameAttributeModal from "../AddGameAttributeModal.jsx/AddGameAttributeModal";
import AddImageModal from "../AddImageModal/AddImageModal";
import deleteIcon from "../../assets/delete_icon.png";
import { Tag, TagWithId } from "../../types/Tag";
import { Publisher, PublisherWithId } from "../../types/Publisher";
import { Developer, DeveloperWithId } from "../../types/Developer";
import { GameForEdit, GameFormSubmit } from "../../types/Game";
import { dateFormatter, formDate, imagePathFormatter } from "../../utils/utils";

type GameFormProps = {
    game: GameForEdit | null,
    onSubmit: (submitObj: GameFormSubmit) => Promise<void>,
    buttonText: "Save changes" | "Add new game"
};

function GameForm({game, onSubmit, buttonText} : GameFormProps) {
    const [name, setName] = useState("");
    const [isNameAdded, setIsNameAdded] = useState(false);

    const [releaseDate, setReleaseDate] = useState("");
    const [isReleaseDateAdded, setIsReleaseDateAdded] = useState(false);

    const [tags, setTags] = useState<Array<Tag | TagWithId>>([]);
    const [selectedTag, setSelectedTag] = useState<Tag | TagWithId | null>(null);
    const [allTags, setAllTags] = useState<Array<Tag | TagWithId>>([]);
    const [allTagsLoaded, setAllTagsLoaded] = useState(false);

    const [publishers, setPublishers] = useState<Array<Publisher | PublisherWithId>>([]);
    const [selectedPublisher, setSelectedPublisher] = useState<Publisher | PublisherWithId | null>(null)
    const [allPublishers, setAllPublishers] = useState<Array<Publisher | PublisherWithId>>([]);
    const [allPublishersLoaded, setAllPublishersLoaded] = useState(false);

    const [developers, setDevelopers] = useState<Array<Developer | DeveloperWithId>>([]);
    const [selectedDeveloper, setSelectedDeveloper] = useState<Developer | DeveloperWithId | null>(null)
    const [allDevelopers, setAllDevelopers] = useState<Array<Developer | DeveloperWithId>>([]);
    const [allDevelopersLoaded, setAllDevelopersLoaded] = useState(false);

    const [descriptionShort, setDescriptionShort] = useState("");
    const [isDescriptionShortAdded, setIsDescriptionShortAdded] = useState(false);
    
    const [descriptionLong, setDescriptionLong] = useState("");
    const [isDescriptionLongAdded, setIsDescriptionLongAdded] = useState(false);

    const [headerImgUrl, setHeaderImgUrl] = useState("");
    const [headerImg, setHeaderImg] = useState<File | null>();
    const [isHeaderImgAdded, setIsHeaderImgAdded] = useState(false);

    const [screenshots, setScreenshots] = useState<Array<File | string>>([]);
    const carouselRef = useRef<HTMLDivElement | null>(null);
    const prevScreenshotNumRef = useRef(0);
    
    useEffect(() => {
        getAllTags().then(tags => {
            setAllTags(tags);
            setAllTagsLoaded(true);
        });
        getAllPublishers().then(publishers => {
            setAllPublishers(publishers);
            setAllPublishersLoaded(true);
        });
        getAllDevelopers().then(developers => {
            setAllDevelopers(developers);
            setAllDevelopersLoaded(true);
        });
    }, []);

    useEffect(() => {
        if (game) {
            setName(game.name);
            setIsNameAdded(true);

            setReleaseDate(formDate(game.releaseDate));
            setIsReleaseDateAdded(true);

            setTags(game.tags);
            setAllTags(prev => prev.filter(prevTag => "id" in prevTag && !game.tags.map(tag => tag.id).includes(prevTag.id)));

            setPublishers(game.publishers);
            setAllPublishers(prev => prev.filter(prevPublisher => "id" in prevPublisher &&  !game.publishers.map(publisher => publisher.id).includes(prevPublisher.id)));

            setDevelopers(game.developers);
            setAllDevelopers(prev => prev.filter(prevDeveloper => "id" in prevDeveloper &&  !game.developers.map(developer => developer.id).includes(prevDeveloper.id)));

            setDescriptionShort(game.descriptionShort);
            setIsDescriptionShortAdded(game.descriptionShort !== "");

            setDescriptionLong(game.descriptionLong);
            setIsDescriptionLongAdded(game.descriptionLong !== "");

            setHeaderImgUrl(game.headerImg);
            setIsHeaderImgAdded(typeof game.headerImg === "string" && game.headerImg !== "");

            setScreenshots(game.screenshots);
            prevScreenshotNumRef.current = game.screenshots.length;
        }
    }, [game, allTagsLoaded, allPublishersLoaded, allDevelopersLoaded]);

    useEffect(() => {
        const carousel = carouselRef.current;
        if (screenshots.length > prevScreenshotNumRef.current && carousel?.lastElementChild) {
          const lastChild = carousel.lastElementChild;
          lastChild?.scrollIntoView({ behavior: 'auto', inline: 'start' });
        }
      }, [screenshots]);

    useEffect(() => {
        setIsHeaderImgAdded(headerImgUrl !== null && headerImgUrl.length !== 0);
    }, [headerImgUrl]);

    type AddToListParametersTag = {
        list : Array<Tag | TagWithId>,
        listSetter : React.Dispatch<React.SetStateAction<(Tag | TagWithId)[]>>,
        newElement : Tag | TagWithId,
        newElementSetter : React.Dispatch<React.SetStateAction<Tag | TagWithId | null>>,
        allListSetter: React.Dispatch<React.SetStateAction<(Tag | TagWithId)[]>>
    };

    type AddToListParametersDeveloper = {
        list : Array<Developer | DeveloperWithId>,
        listSetter : React.Dispatch<React.SetStateAction<(Developer | DeveloperWithId)[]>>,
        newElement : Developer | DeveloperWithId,
        newElementSetter : React.Dispatch<React.SetStateAction<Developer | DeveloperWithId | null>>,
        allListSetter: React.Dispatch<React.SetStateAction<(Developer | DeveloperWithId)[]>>
    };

    type AddToListParametersPublisher = {
        list : Array<Publisher | PublisherWithId>,
        listSetter : React.Dispatch<React.SetStateAction<(Publisher | PublisherWithId)[]>>,
        newElement : Publisher | PublisherWithId,
        newElementSetter : React.Dispatch<React.SetStateAction<Publisher | PublisherWithId | null>>,
        allListSetter: React.Dispatch<React.SetStateAction<(Publisher | PublisherWithId)[]>>
    };

    function addToList({list, listSetter, newElement, newElementSetter, allListSetter} : AddToListParametersTag | AddToListParametersDeveloper | AddToListParametersPublisher) {
        const newList = [...list, newElement];
        listSetter(newList);

        const listIds = newList.map(element => "id" in element ? element.id : undefined);
        allListSetter(prev => prev.filter(element => "id" in element ? !listIds.includes(String(element.id)) : element.name !== newElement.name));

        newElementSetter(null);
    }

    type RemoveFromListParametersTag = {
        elementToRemove : Tag | TagWithId,
        listSetter : React.Dispatch<React.SetStateAction<(Tag | TagWithId)[]>>,
        allListSetter : React.Dispatch<React.SetStateAction<(Tag | TagWithId)[]>>
    };

    type RemoveFromListParametersDeveloper = {
        elementToRemove : Developer | DeveloperWithId,
        listSetter : React.Dispatch<React.SetStateAction<(Developer | DeveloperWithId)[]>>,
        allListSetter : React.Dispatch<React.SetStateAction<(Developer | DeveloperWithId)[]>>
    };

    type RemoveFromListParametersPublisher = {
        elementToRemove : Publisher | PublisherWithId,
        listSetter : React.Dispatch<React.SetStateAction<(Publisher | PublisherWithId)[]>>,
        allListSetter : React.Dispatch<React.SetStateAction<(Publisher | PublisherWithId)[]>>
    };

    function removeFromList({elementToRemove, listSetter, allListSetter} : RemoveFromListParametersTag | RemoveFromListParametersDeveloper | RemoveFromListParametersPublisher) {
        listSetter(prev => prev.filter(element => element.name !== elementToRemove.name));

        allListSetter(prev => {
            const newList = [...prev];
            "id" in elementToRemove && newList.push(elementToRemove);
            newList.sort((a, b) => a.name.localeCompare(b.name));
            return newList;
        });
    }

    function handleHeaderImgChange(event: React.FormEvent<HTMLInputElement>) {
        const target = event.target as HTMLInputElement & {
            files: FileList
        }

        const uploadedImage = target.files[0];

        if (uploadedImage.type.startsWith("image/")) {
            setHeaderImg(uploadedImage);
    
            setHeaderImgUrl(URL.createObjectURL(uploadedImage));
    
            setIsHeaderImgAdded(true);
        } else {
            target.value = "";
        }

    }

    function handleHeaderImgDelete() {
        setHeaderImg(null);

        setHeaderImgUrl("");

        setIsHeaderImgAdded(false);
    }

    function addFileToScreenshots(event : React.FormEvent<HTMLInputElement>) {
        const target = event.target as HTMLInputElement & {
            files: FileList
        }

        const uploadedImage = target.files[0];

        if (uploadedImage.type.startsWith("image/")) {
            setScreenshots(prev => [...prev, uploadedImage]);
        } else {
            target.value = "";
        }
    }

    function removeElementFromScreenshots(removeIndex : string) {
        const index = removeIndex as any as number;

        setScreenshots(prev => {
            const newArr = [...prev];
            newArr.splice(index, 1);
            return newArr;
        });
    }

    return (
        <>
            <form className="grid grid-cols-1 w-80">

                <fieldset className="fieldset h-20">
                    <legend className="fieldset-legend">Name</legend>
                    <div className={`flex gap-2 ${isNameAdded ? "hidden" : ""}`}>
                        <button type="button" className="btn btn-primary w-15" onClick={() => setIsNameAdded(true)} disabled={name === ""}>Add</button>
                        <label className="input">
                            <input type="text" onChange={event => setName(event.target.value)} value={name} onKeyDown={event => {event.keyCode === 13 && event.preventDefault(); return event.keyCode === 13 && name !== "" && setIsNameAdded(true)}} />
                        </label>
                    </div>
                    <div className={`flex items-center gap-2 ${isNameAdded ? "" : "hidden"}`}>
                        <button title="edit name" type="button" className="btn btn-primary cursor-pointer w-15" onClick={() => setIsNameAdded(false)}>Edit</button>
                        <span title={name} className="text-sm">{name.length < 30 ? name : name.slice(0, 22) + "..."}</span>
                    </div>
                </fieldset>

                <fieldset className="fieldset h-20">
                    <legend className="fieldset-legend">Release date</legend>
                    <div className={`flex gap-2 ${isReleaseDateAdded ? "hidden" : ""}`}>
                        <button type="button" className="btn btn-primary" onClick={() => setIsReleaseDateAdded(true)} disabled={releaseDate === ""}>Add</button>
                        <label className="input">
                            <input type="date" max="9999-12-31" onChange={event => setReleaseDate(event.target.value)} value={releaseDate} onKeyDown={event => event.keyCode === 13 && releaseDate !== "" && setIsReleaseDateAdded(true)} />
                        </label>
                    </div>
                    <div className={`flex items-center gap-2 ${isReleaseDateAdded ? "" : "hidden"}`}>
                        <button title="edit release date" type="button" className="btn btn-primary cursor-pointer" onClick={() => setIsReleaseDateAdded(false)}>Edit</button>
                        <span className="text-sm">{releaseDate.replaceAll("-", " ")}</span>
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                    <legend className="fieldset-legend w-full">Tags<button type="button" className="btn btn-xs btn-primary btn-soft justify-end" onClick={()=>(document.getElementById('tagModal') as HTMLDialogElement).showModal()}>Create new tag</button></legend>
                    <div className="flex gap-2">
                        <button type="button" className="btn btn-primary w-15" disabled={!selectedTag} onClick={() => addToList({list: tags, listSetter: setTags, newElement: selectedTag!, newElementSetter: setSelectedTag, allListSetter: setAllTags})}>Add</button>
                        <label className="select pl-0">
                            <select value={selectedTag?.name ?? "Choose a tag"} className="ml-0!" onChange={event => setSelectedTag(typeof event.target.selectedOptions[0].dataset.id === "undefined" ? {name: event.target.selectedOptions[0].value} : {id: event.target.selectedOptions[0].dataset.id as unknown as number, name: event.target.selectedOptions[0].value})}>
                                <option disabled={true}>Choose a tag</option>
                                {allTags.map(tag => (<option  data-id={"id" in tag ? tag.id : undefined} key={"id" in tag ? tag.id : undefined} value={tag.name}>{tag.name}</option>))}
                            </select>
                        </label>
                    </div>
                    <div className="max-w-80">
                        {tags.map(tag => (<span data-id={"id" in tag ? tag.id : undefined} data-name={tag.name} key={"id" in tag ? tag.id : undefined} className="badge badge-outline badge-info mr-1 mb-1 cursor-pointer" onClick={event => removeFromList({elementToRemove: typeof (event.target as HTMLSpanElement).dataset.id === "undefined" ? {name: (event.target as HTMLSpanElement).dataset.name!} : {id: (event.target as HTMLSpanElement).dataset.id as unknown as number, name: (event.target as HTMLSpanElement).dataset.name!}, listSetter: setTags, allListSetter: setAllTags})}>{tag.name}</span>))}
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                    <legend className="fieldset-legend w-full">Developers<button type="button" className="btn btn-xs btn-primary btn-soft justify-end" onClick={()=>(document.getElementById('developerModal') as HTMLDialogElement).showModal()}>Create new developer</button></legend>
                    <div className="flex gap-2">
                        <button type="button" className="btn btn-primary w-15" disabled={!selectedDeveloper} onClick={() => addToList({list: developers, listSetter: setDevelopers, newElement: selectedDeveloper!, newElementSetter: setSelectedDeveloper, allListSetter: setAllDevelopers})}>Add</button>
                        <label className="select pl-0">
                            <select value={selectedDeveloper?.name ?? "Choose a developer"} className="ml-0!" onChange={event => setSelectedDeveloper(typeof event.target.selectedOptions[0].dataset.id === "undefined" ? {name: event.target.selectedOptions[0].value} : {id: event.target.selectedOptions[0].dataset.id as unknown as number, name: event.target.selectedOptions[0].value})}>
                                <option disabled={true}>Choose a developer</option>
                                {allDevelopers.map(developer => (<option data-id={"id" in developer ? developer.id : undefined} key={"id" in developer ? developer.id : undefined} value={developer.name}>{developer.name}</option>))}
                            </select>
                        </label>
                    </div>
                    <div className="max-w-80">
                        {developers.map(developer => (<span data-id={"id" in developer ? developer.id : undefined} data-name={developer.name} key={"id" in developer ? developer.id : undefined} className="badge badge-outline badge-info mr-1 mb-1 cursor-pointer" onClick={event => removeFromList({elementToRemove: typeof (event.target as HTMLSpanElement).dataset.id === "undefined" ? {name: (event.target as HTMLSpanElement).dataset.name!} : {id: (event.target as HTMLSpanElement).dataset.id as unknown as number, name: (event.target as HTMLSpanElement).dataset.name!}, listSetter: setDevelopers, allListSetter: setAllDevelopers})}>{developer.name}</span>))}
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                    <legend className="fieldset-legend w-full">Publishers<button type="button" className="btn btn-xs btn-primary btn-soft justify-end" onClick={()=>(document.getElementById('publisherModal') as HTMLDialogElement).showModal()}>Create new publisher</button></legend>
                    <div className="flex gap-2">
                        <button type="button" className="btn btn-primary w-15" disabled={!selectedPublisher} onClick={() => addToList({list: publishers, listSetter: setPublishers, newElement: selectedPublisher!, newElementSetter: setSelectedPublisher, allListSetter: setAllPublishers})}>Add</button>
                        <label className="select pl-0">
                            <select value={selectedPublisher?.name ?? "Choose a publisher"} className="ml-0!" onChange={event => setSelectedPublisher(typeof event.target.selectedOptions[0].dataset.id === "undefined" ? {name: event.target.selectedOptions[0].value} : {id: event.target.selectedOptions[0].dataset.id as unknown as number, name: event.target.selectedOptions[0].value})}>
                                <option disabled={true}>Choose a publisher</option>
                                {allPublishers.map(publisher => (<option data-id={"id" in publisher ? publisher.id : undefined} key={"id" in publisher ? publisher.id : undefined} value={publisher.name}>{publisher.name}</option>))}
                            </select>
                        </label>
                    </div>
                    <div className="max-w-80">
                        {publishers.map(publisher => (<span data-id={"id" in publisher ? publisher.id : undefined} data-name={publisher.name} key={"id" in publisher ? publisher.id : undefined} className="badge badge-outline badge-info mr-1 mb-1 cursor-pointer" onClick={event => removeFromList({elementToRemove: typeof (event.target as HTMLSpanElement).dataset.id === "undefined" ? {name: (event.target as HTMLSpanElement).dataset.name!} : {id: (event.target as HTMLSpanElement).dataset.id as unknown as number, name: (event.target as HTMLSpanElement).dataset.name!}, listSetter: setPublishers, allListSetter: setAllPublishers})}>{publisher.name}</span>))}
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                    <legend className="fieldset-legend">Short description</legend>
                    <div className={`flex gap-2 ${isDescriptionShortAdded ? "hidden" : ""}`}>
                        <button type="button" className="btn btn-primary w-15" disabled={!descriptionShort || descriptionShort.length > 240} onClick={() => setIsDescriptionShortAdded(true)}>Add</button>
                        <div>
                            <textarea className="textarea h-24" onChange={event => setDescriptionShort(event.target.value)} value={descriptionShort} />
                            <div className="label">{`maximum 240 characters (${descriptionShort.length}/240)`}</div>
                        </div>
                    </div>
                    <div className={`flex gap-2 ${isDescriptionShortAdded ? "" : "hidden"}`}>
                        <button type="button" className="btn btn-primary w-15" onClick={() => setIsDescriptionShortAdded(false)}>Edit</button>
                        <div className="flex-grow min-w-0">
                            <div className="textarea h-24 w-full overflow-auto whitespace-pre-wrap p-2 pl-3 rounded resize-none" style={{wordBreak: 'break-word'}}>
                                {descriptionShort}
                            </div>
                            <div className="label">{" "}</div>
                        </div>
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                    <legend className="fieldset-legend">Long description</legend>
                    <div className={`flex gap-2 ${isDescriptionLongAdded ? "hidden" : ""}`}>
                        <button type="button" className="btn btn-primary w-15" disabled={!descriptionLong || descriptionLong.length > 2000} onClick={() => setIsDescriptionLongAdded(true)}>Add</button>
                        <div>
                            <textarea className="textarea h-36" onChange={event => setDescriptionLong(event.target.value)} value={descriptionLong} />
                            <div className="label">{`maximum 2000 characters (${descriptionLong.length}/2000)`}</div>
                        </div>
                    </div>
                    <div className={`flex gap-2 ${isDescriptionLongAdded ? "" : "hidden"}`}>
                        <button type="button" className="btn btn-primary w-15" onClick={() => setIsDescriptionLongAdded(false)}>Edit</button>
                        <div className="flex-grow min-w-0">
                            <div className="textarea h-36 w-full overflow-auto whitespace-pre-wrap p-2 pl-3 rounded resize-none" style={{wordBreak: 'break-word'}}>
                                {descriptionLong}
                            </div>
                            <div className="label">{" "}</div>
                        </div>
                    </div>
                </fieldset>
                
                <fieldset className="fieldset h-fit">
                    <legend className="fieldset-legend">Header image</legend>
                    <div className={`flex gap-2 ${isHeaderImgAdded ? "hidden" : ""}`}>
                        <label className="btn btn-primary grow">
                            Upload File
                            <input className="hidden" type="file" accept="image/*" onChange={(event) => handleHeaderImgChange(event)}/>
                        </label>
                        <label className="grow">
                            <button type="button" className="btn btn-primary w-full" onClick={()=>(document.getElementById('headerImgModal') as HTMLDialogElement).showModal()}>Add Link</button>
                        </label>
                    </div>
                    <div className={`w-full h-30 flex justify-left ${isHeaderImgAdded ? "" : "hidden"}`}>
                        <img title="delete uploaded header image" className="w-5 h-5 inline border-1 rounded-full mr-1 cursor-pointer" src={deleteIcon} onClick={handleHeaderImgDelete} />
                        <img className="h-full" src={headerImgUrl} />
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                        <legend className="fieldset-legend w-full">Screenshots</legend>
                        <div className="flex gap-2 w-full">
                            <label className="btn btn-primary grow">
                                Upload File
                                <input className="hidden" type="file" accept="image/*" onChange={addFileToScreenshots}/>
                            </label>
                            <label className="grow">
                                <button type="button" className="btn btn-primary w-full" onClick={()=>(document.getElementById('screenshotModal') as HTMLDialogElement).showModal()}>Add Link</button>
                            </label>

                        </div>
                        <div ref={carouselRef} className="carousel w-full">
                            {screenshots.map((screenshot, index) => {
                                const screenshotUrl = typeof screenshot === "string" ? screenshot : URL.createObjectURL(screenshot);
                                return (
                                    <div key={screenshotUrl} className="carousel-item w-full rounded-t-box relative">
                                        <img 
                                            title="delete screenshot" 
                                            className="w-5 h-5 absolute top-1 left-1 border-1 rounded-full mr-1 cursor-pointer bg-white" 
                                            src={deleteIcon}
                                            data-index={index}
                                            onClick={event => removeElementFromScreenshots((event.target as HTMLImageElement).dataset.index!)} 
                                            />
                                        <img
                                            src={imagePathFormatter(screenshotUrl)}
                                            alt={`${name} screenshot no. ${index + 1}`}
                                        />
                                    </div>
                                )
                            })}
                        </div>
                </fieldset>

                <button type="button" onClick={() => onSubmit({name, releaseDate: dateFormatter(releaseDate), tags, publishers, developers, descriptionShort, descriptionLong, headerImg: headerImg ?? headerImgUrl, screenshots})} className="btn btn-primary mt-5" disabled={!isNameAdded || !isReleaseDateAdded || tags.length === 0 || developers.length === 0 || publishers.length === 0}>{buttonText}</button>
            </form>
            
            <AddGameAttributeModal modalId="tagModal" modalName="Tag" listSetter={setTags} list={tags} allList={allTags}/>
            <AddGameAttributeModal modalId="developerModal" modalName="Developer" listSetter={setDevelopers} list={developers} allList={allDevelopers}/>
            <AddGameAttributeModal modalId="publisherModal" modalName="Publisher" listSetter={setPublishers} list={publishers} allList={allPublishers}/>
            <AddImageModal modalId="screenshotModal" modalName="Screenshot" list={screenshots} setter={setScreenshots}/>
            <AddImageModal modalId="headerImgModal" modalName="Header Image" list={null} setter={setHeaderImgUrl} />
        </>

    )
}

export default GameForm;
