/* eslint-disable react/prop-types */
import { useEffect, useState } from "react";
import { getAllTags } from "../../services/tagService";
import { getAllPublishers } from "../../services/publisherService";
import { getAllDevelopers } from "../../services/developerService";
import AddGameAttributeModal from "../AddGameAttributeModal.jsx/AddGameAttributeModal";
import deleteIcon from "../../assets/delete_icon.png";
import { Tag, TagWithId } from "../../types/Tag";
import { Publisher, PublisherWithId } from "../../types/Publisher";
import { Developer, DeveloperWithId } from "../../types/Developer";
import { GameForEdit, GameFormSubmit } from "../../types/Game";
import { formDate } from "../../utils/utils";

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

    const [headerImg, setHeaderImg] = useState("");
    const [isHeaderImgAdded, setIsHeaderImgAdded] = useState(false);

    const [screenshot, setScreenshot] = useState("");
    const [screenshots, setScreenshots] = useState<string[]>([]);
    
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

            setHeaderImg(game.headerImg);
            setIsHeaderImgAdded(game.headerImg !== "");

            game.screenshots.forEach(screenshot => addElementToScreenshots(screenshot));
        }
    }, [game, allTagsLoaded, allPublishersLoaded, allDevelopersLoaded])

    function addToList(
            list : Array<Tag | TagWithId> | Array<Developer | DeveloperWithId> | Array<Publisher | PublisherWithId>, 
            listSetter : React.Dispatch<React.SetStateAction<(Tag | TagWithId)[]>> | React.Dispatch<React.SetStateAction<(Developer | DeveloperWithId)[]>> | React.Dispatch<React.SetStateAction<(Publisher | PublisherWithId)[]>>, 
            newElement : Tag | TagWithId | Developer | DeveloperWithId | Publisher | PublisherWithId,
            newElementSetter : React.Dispatch<React.SetStateAction<Tag | TagWithId | null>> | React.Dispatch<React.SetStateAction<Developer | DeveloperWithId | null>> | React.Dispatch<React.SetStateAction<Publisher | PublisherWithId | null>>,
            allListSetter: React.Dispatch<React.SetStateAction<(Tag | TagWithId)[]>> | React.Dispatch<React.SetStateAction<(Developer | DeveloperWithId)[]>> | React.Dispatch<React.SetStateAction<(Publisher | PublisherWithId)[]>>
        ) {
        const newList = [...list, newElement];
        listSetter(newList);

        const listIds = newList.map(element => "id" in element ? element.id : undefined);
        allListSetter(prev => prev.filter(element => "id" in element ? !listIds.includes(String(element.id)) : element.name !== newElement.name));

        newElementSetter(null);
    }

    function removeFromList(
        elementToRemove : Tag | TagWithId | Developer | DeveloperWithId | Publisher | PublisherWithId, 
        listSetter : React.Dispatch<React.SetStateAction<(Tag | TagWithId)[]>> | React.Dispatch<React.SetStateAction<(Developer | DeveloperWithId)[]>> | React.Dispatch<React.SetStateAction<(Publisher | PublisherWithId)[]>>,
        allListSetter : React.Dispatch<React.SetStateAction<(Tag | TagWithId)[]>> | React.Dispatch<React.SetStateAction<(Developer | DeveloperWithId)[]>> | React.Dispatch<React.SetStateAction<(Publisher | PublisherWithId)[]>>
    ) {
        listSetter(prev => prev.filter(element => element.name !== elementToRemove.name));

        allListSetter(prev => {
            const newList = [...prev];
            "id" in elementToRemove && newList.push(elementToRemove);
            newList.sort((a, b) => a.name.localeCompare(b.name));
            return newList;
        });
    }

    function addElementToScreenshots(screenshotToAdd : string) {
        setScreenshots(prev => {
            if (prev.includes(screenshotToAdd)){
                return prev;
            } else{
                return [...prev, screenshotToAdd];
            }
        });
        setScreenshot("");
    }

    function removeElementFromScreenshots(elementToRemove : string) {
        setScreenshots(prev => {
            return [...prev.filter(element => element !== elementToRemove)];
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
                            <input type="text" onChange={event => setName(event.target.value)} value={name} onKeyDown={event => event.keyCode === 13 && name !== "" && setIsNameAdded(true)} />
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
                        <button type="button" className="btn btn-primary w-15" disabled={!selectedTag} onClick={() => addToList(tags, setTags, selectedTag!, setSelectedTag, setAllTags)}>Add</button>
                        <label className="select pl-0">
                            <select value={selectedTag?.name ?? "Choose a tag"} className="ml-0!" onChange={event => setSelectedTag(typeof event.target.selectedOptions[0].dataset.id === "undefined" ? {name: event.target.selectedOptions[0].value} : {id: event.target.selectedOptions[0].dataset.id as unknown as number, name: event.target.selectedOptions[0].value})}>
                                <option disabled={true}>Choose a tag</option>
                                {allTags.map(tag => (<option  data-id={"id" in tag ? tag.id : undefined} key={"id" in tag ? tag.id : undefined} value={tag.name}>{tag.name}</option>))}
                            </select>
                        </label>
                    </div>
                    <div className="max-w-80">
                        {tags.map(tag => (<span data-id={"id" in tag ? tag.id : undefined} data-name={tag.name} key={"id" in tag ? tag.id : undefined} className="badge badge-outline badge-info mr-1 mb-1 cursor-pointer" onClick={event => removeFromList(typeof (event.target as HTMLSpanElement).dataset.id === "undefined" ? {name: (event.target as HTMLSpanElement).dataset.name!} : {id: (event.target as HTMLSpanElement).dataset.id as unknown as number, name: (event.target as HTMLSpanElement).dataset.name!}, setTags, setAllTags)}>{tag.name}</span>))}
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                    <legend className="fieldset-legend w-full">Developers<button type="button" className="btn btn-xs btn-primary btn-soft justify-end" onClick={()=>(document.getElementById('developerModal') as HTMLDialogElement).showModal()}>Create new developer</button></legend>
                    <div className="flex gap-2">
                        <button type="button" className="btn btn-primary w-15" disabled={!selectedDeveloper} onClick={() => addToList(developers, setDevelopers, selectedDeveloper!, setSelectedDeveloper, setAllDevelopers)}>Add</button>
                        <label className="select pl-0">
                            <select value={selectedDeveloper?.name ?? "Choose a developer"} className="ml-0!" onChange={event => setSelectedDeveloper(typeof event.target.selectedOptions[0].dataset.id === "undefined" ? {name: event.target.selectedOptions[0].value} : {id: event.target.selectedOptions[0].dataset.id as unknown as number, name: event.target.selectedOptions[0].value})}>
                                <option disabled={true}>Choose a developer</option>
                                {allDevelopers.map(developer => (<option data-id={"id" in developer ? developer.id : undefined} key={"id" in developer ? developer.id : undefined} value={developer.name}>{developer.name}</option>))}
                            </select>
                        </label>
                    </div>
                    <div className="max-w-80">
                        {developers.map(developer => (<span data-id={"id" in developer ? developer.id : undefined} data-name={developer.name} key={"id" in developer ? developer.id : undefined} className="badge badge-outline badge-info mr-1 mb-1 cursor-pointer" onClick={event => removeFromList(typeof (event.target as HTMLSpanElement).dataset.id === "undefined" ? {name: (event.target as HTMLSpanElement).dataset.name!} : {id: (event.target as HTMLSpanElement).dataset.id as unknown as number, name: (event.target as HTMLSpanElement).dataset.name!}, setDevelopers, setAllDevelopers)}>{developer.name}</span>))}
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                    <legend className="fieldset-legend w-full">Publishers<button type="button" className="btn btn-xs btn-primary btn-soft justify-end" onClick={()=>(document.getElementById('publisherModal') as HTMLDialogElement).showModal()}>Create new publisher</button></legend>
                    <div className="flex gap-2">
                        <button type="button" className="btn btn-primary w-15" disabled={!selectedPublisher} onClick={() => addToList(publishers, setPublishers, selectedPublisher!, setSelectedPublisher, setAllPublishers)}>Add</button>
                        <label className="select pl-0">
                            <select value={selectedPublisher?.name ?? "Choose a publisher"} className="ml-0!" onChange={event => setSelectedPublisher(typeof event.target.selectedOptions[0].dataset.id === "undefined" ? {name: event.target.selectedOptions[0].value} : {id: event.target.selectedOptions[0].dataset.id as unknown as number, name: event.target.selectedOptions[0].value})}>
                                <option disabled={true}>Choose a publisher</option>
                                {allPublishers.map(publisher => (<option data-id={"id" in publisher ? publisher.id : undefined} key={"id" in publisher ? publisher.id : undefined} value={publisher.name}>{publisher.name}</option>))}
                            </select>
                        </label>
                    </div>
                    <div className="max-w-80">
                        {publishers.map(publisher => (<span data-id={"id" in publisher ? publisher.id : undefined} data-name={publisher.name} key={"id" in publisher ? publisher.id : undefined} className="badge badge-outline badge-info mr-1 mb-1 cursor-pointer" onClick={event => removeFromList(typeof (event.target as HTMLSpanElement).dataset.id === "undefined" ? {name: (event.target as HTMLSpanElement).dataset.name!} : {id: (event.target as HTMLSpanElement).dataset.id as unknown as number, name: (event.target as HTMLSpanElement).dataset.name!}, setPublishers, setAllPublishers)}>{publisher.name}</span>))}
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
                
                <fieldset className="fieldset h-20">
                    <legend className="fieldset-legend">Header image</legend>
                    <div className={`flex gap-2 ${isHeaderImgAdded ? "hidden" : ""}`}>
                        <button type="button" className="btn btn-primary w-15" onClick={() => setIsHeaderImgAdded(true)} disabled={headerImg === ""}>Add</button>
                        <label className="input">
                            <input type="text" onChange={event => setHeaderImg(event.target.value)} value={headerImg} onKeyDown={event => event.keyCode === 13 && headerImg !== "" && setIsHeaderImgAdded(true)}/>
                        </label>
                    </div>
                    <div className={`flex items-center gap-2 ${isHeaderImgAdded ? "" : "hidden"}`}>
                        <button title="edit name" type="button" className="btn btn-primary cursor-pointer w-15" onClick={() => setIsHeaderImgAdded(false)}>Edit</button>
                        <span title={headerImg} className="text-sm">{headerImg.length < 30 ? headerImg : headerImg.slice(0, 22) + "..."}</span>
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                    <legend className="fieldset-legend w-full">Screenshots</legend>
                    <div className="flex gap-2">
                        <button type="button" className="btn btn-primary w-15" onClick={() => addElementToScreenshots(screenshot)} disabled={screenshot.length === 0}>Add</button>
                        <label className="input">
                            <input type="text" onChange={event => setScreenshot(event.target.value)} value={screenshot} onKeyDown={event => event.keyCode === 13 && screenshot !== "" && addElementToScreenshots(screenshot)} />
                        </label>
                    </div>
                    <div>
                        {screenshots.map((screenshot, index) => {
                            return <div key={"scrsht" + index} className="text-sm ml-20 mb-1 flex items-center"><img data-screenshot={screenshot} onClick={event => removeElementFromScreenshots((event.target as HTMLImageElement).dataset.screenshot!)} title="delete screenshot" className="w-5 inline border-1 rounded-full mr-1 cursor-pointer" src={deleteIcon}/><span title={screenshot}>{screenshot.length < 30 ? screenshot : screenshot.slice(0, 22) + "..."}</span></div>
                            })}
                    </div>
                </fieldset>

                <button type="button" onClick={() => onSubmit({name, releaseDate: new Date(releaseDate), tags, publishers, developers, descriptionShort, descriptionLong, headerImg, screenshots})} className="btn btn-primary mt-5" disabled={!isNameAdded || !isReleaseDateAdded || tags.length === 0 || developers.length === 0 || publishers.length === 0}>{buttonText}</button>
            </form>
            
            <AddGameAttributeModal modalId="tagModal" modalName="Tag" listSetter={setTags} list={tags} allList={allTags}/>
            <AddGameAttributeModal modalId="developerModal" modalName="Developer" listSetter={setDevelopers} list={developers} allList={allDevelopers}/>
            <AddGameAttributeModal modalId="publisherModal" modalName="Publisher" listSetter={setPublishers} list={publishers} allList={allPublishers}/>
        </>

    )
}

export default GameForm;
