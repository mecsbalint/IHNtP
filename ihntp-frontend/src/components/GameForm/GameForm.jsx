/* eslint-disable react/prop-types */
import { useEffect, useState } from "react";
import { getAllTags } from "../../services/tagService";
import { getAllPublishers } from "../../services/publisherService";
import { getAllDevelopers } from "../../services/developerService";
import AddGameAttributeModal from "../AddGameAttributeModal.jsx/AddGameAttributeModal";
import deleteIcon from "../../assets/delete_icon.png";

function GameForm({game, onSubmit, buttonText}) {
    const [name, setName] = useState("");
    const [isNameAdded, setIsNameAdded] = useState(false);

    const [releaseDate, setReleaseDate] = useState("");
    const [isReleaseDateAdded, setIsReleaseDateAdded] = useState(false);

    const [tags, setTags] = useState([]);
    const [selectedTag, setSelectedTag] = useState(null);
    const [allTags, setAllTags] = useState([]);
    const [allTagsLoaded, setAllTagsLoaded] = useState(false);

    const [publishers, setPublishers] = useState([]);
    const [selectedPublisher, setSelectedPublisher] = useState(null)
    const [allPublishers, setAllPublishers] = useState([]);
    const [allPublishersLoaded, setAllPublishersLoaded] = useState(false);

    const [developers, setDevelopers] = useState([]);
    const [selectedDeveloper, setSelectedDeveloper] = useState(null)
    const [allDevelopers, setAllDevelopers] = useState([]);
    const [allDevelopersLoaded, setAllDevelopersLoaded] = useState(false);

    const [descriptionShort, setDescriptionShort] = useState("");
    const [isDescriptionShortAdded, setIsDescriptionShortAdded] = useState(false);
    
    const [descriptionLong, setDescriptionLong] = useState("");
    const [isDescriptionLongAdded, setIsDescriptionLongAdded] = useState(false);

    const [headerImg, setHeaderImg] = useState("");
    const [isHeaderImgAdded, setIsHeaderImgAdded] = useState(false);

    const [screenshot, setScreenshot] = useState("");
    const [screenshots, setScreenshots] = useState([]);
    
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

            setReleaseDate(game.releaseDate);
            setIsReleaseDateAdded(true);

            setTags(game.tags);
            setAllTags(prev => prev.filter(prevTag => !game.tags.map(tag => tag.id).includes(prevTag.id)));

            setPublishers(game.publishers);
            setAllPublishers(prev => prev.filter(prevPublisher => !game.publishers.map(publisher => publisher.id).includes(prevPublisher.id)));

            setDevelopers(game.developers);
            setAllDevelopers(prev => prev.filter(prevDeveloper => !game.developers.map(developer => developer.id).includes(prevDeveloper.id)));

            setDescriptionShort(game.descriptionShort);
            setIsDescriptionShortAdded(game.descriptionShort !== "");

            setDescriptionLong(game.descriptionLong);
            setIsDescriptionLongAdded(game.descriptionLong !== "");

            setHeaderImg(game.headerImg);
            setIsHeaderImgAdded(game.headerImg !== "");

            game.screenshots.forEach(screenshot => addElementToScreenshots(screenshot));
        }
    }, [game, allTagsLoaded, allPublishersLoaded, allDevelopersLoaded])

    function addToList(list, listSetter, newElement, newElementSetter, allListSetter) {
        const newList = [...list, newElement];
        listSetter(newList);

        const listIds = newList.map(element => element.id);
        allListSetter(prev => prev.filter(element => !listIds.includes(String(element.id))));

        newElementSetter(null);
    }

    function removeFromList(elementToRemove, listSetter, allListSetter) {
        listSetter(prev => prev.filter(element => element.id ? Number(element.id) !== Number(elementToRemove.id) : element.name !== elementToRemove.name));

        allListSetter(prev => {
            const newList = [...prev];
            elementToRemove.id && newList.push(elementToRemove);
            newList.sort((a, b) => a.name.localeCompare(b.name));
            return newList;
        });
    }

    function addElementToScreenshots(screenshotToAdd) {
        setScreenshots(prev => {
            if (prev.includes(screenshotToAdd)){
                return prev;
            } else{
                return [...prev, screenshotToAdd];
            }
        });
        setScreenshot("");
    }

    function removeElementFromScreenshots(elementToRemove) {
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
                    <legend className="fieldset-legend w-full">Tags<button type="button" className="btn btn-xs btn-primary btn-soft justify-end" onClick={()=>document.getElementById('tagModal').showModal()}>Create new tag</button></legend>
                    <div className="flex gap-2">
                        <button type="button" className="btn btn-primary w-15" disabled={!selectedTag} onClick={() => addToList(tags, setTags, selectedTag, setSelectedTag, setAllTags)}>Add</button>
                        <label className="select pl-0">
                            <select value={selectedTag?.name ?? "Choose a tag"} className="ml-0!" onChange={event => setSelectedTag({id: event.target.selectedOptions[0].dataset.id, name: event.target.selectedOptions[0].value})}>
                                <option disabled={true} defaultValue={true}>Choose a tag</option>
                                {allTags.map(tag => (<option  data-id={tag.id} key={tag.id} value={tag.name}>{tag.name}</option>))}
                            </select>
                        </label>
                    </div>
                    <div className="max-w-80">
                        {tags.map(tag => (<span data-id={tag.id} data-name={tag.name} key={tag.id} className="badge badge-outline badge-info mr-1 mb-1 cursor-pointer" onClick={event => removeFromList({id: event.target.dataset.id, name: event.target.dataset.name}, setTags, setAllTags)}>{tag.name}</span>))}
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                    <legend className="fieldset-legend w-full">Developers<button type="button" className="btn btn-xs btn-primary btn-soft justify-end" onClick={()=>document.getElementById('developerModal').showModal()}>Create new developer</button></legend>
                    <div className="flex gap-2">
                        <button type="button" className="btn btn-primary w-15" disabled={!selectedDeveloper} onClick={() => addToList(developers, setDevelopers, selectedDeveloper, setSelectedDeveloper, setAllDevelopers)}>Add</button>
                        <label className="select pl-0">
                            <select value={selectedDeveloper?.name ?? "Choose a developer"} className="ml-0!" onChange={event => setSelectedDeveloper({id: event.target.selectedOptions[0].dataset.id, name: event.target.selectedOptions[0].value})}>
                                <option disabled={true} defaultValue={true}>Choose a developer</option>
                                {allDevelopers.map(developer => (<option data-id={developer.id} key={developer.id} value={developer.name}>{developer.name}</option>))}
                            </select>
                        </label>
                    </div>
                    <div className="max-w-80">
                        {developers.map(developer => (<span data-id={developer.id} data-name={developer.name} key={developer.id} className="badge badge-outline badge-info mr-1 mb-1 cursor-pointer" onClick={event => removeFromList({id: event.target.dataset.id, name: event.target.dataset.name}, setDevelopers, setAllDevelopers)}>{developer.name}</span>))}
                    </div>
                </fieldset>

                <fieldset className="fieldset">
                    <legend className="fieldset-legend w-full">Publishers<button type="button" className="btn btn-xs btn-primary btn-soft justify-end" onClick={()=>document.getElementById('publisherModal').showModal()}>Create new publisher</button></legend>
                    <div className="flex gap-2">
                        <button type="button" className="btn btn-primary w-15" disabled={!selectedPublisher} onClick={() => addToList(publishers, setPublishers, selectedPublisher, setSelectedPublisher, setAllPublishers)}>Add</button>
                        <label className="select pl-0">
                            <select value={selectedPublisher?.name ?? "Choose a publisher"} className="ml-0!" onChange={event => setSelectedPublisher({id: event.target.selectedOptions[0].dataset.id, name: event.target.selectedOptions[0].value})}>
                                <option disabled={true} defaultValue={true}>Choose a publisher</option>
                                {allPublishers.map(publisher => (<option data-id={publisher.id} key={publisher.id} value={publisher.name}>{publisher.name}</option>))}
                            </select>
                        </label>
                    </div>
                    <div className="max-w-80">
                        {publishers.map(publisher => (<span data-id={publisher.id} data-name={publisher.name} key={publisher.id} className="badge badge-outline badge-info mr-1 mb-1 cursor-pointer" onClick={event => removeFromList({id: event.target.dataset.id, name: event.target.dataset.name}, setPublishers, setAllPublishers)}>{publisher.name}</span>))}
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
                            return <div key={"scrsht" + index} className="text-sm ml-20 mb-1 flex items-center"><img data-screenshot={screenshot} onClick={event => removeElementFromScreenshots(event.target.dataset.screenshot)} title="delete screenshot" className="w-5 inline border-1 rounded-full mr-1 cursor-pointer" src={deleteIcon}/><span title={screenshot}>{screenshot.length < 30 ? screenshot : screenshot.slice(0, 22) + "..."}</span></div>
                            })}
                    </div>
                </fieldset>

                <button type="button" onClick={() => onSubmit({name, releaseDate, tags, publishers, developers, descriptionShort, descriptionLong, headerImg, screenshots})} className="btn btn-primary mt-5" disabled={!isNameAdded || !isReleaseDateAdded || tags.length === 0 || developers.length === 0 || publishers.length === 0}>{buttonText}</button>
            </form>
            
            <AddGameAttributeModal modalId="tagModal" modalName="Tag" listSetter={setTags} list={tags} allList={allTags}/>
            <AddGameAttributeModal modalId="developerModal" modalName="Developer" listSetter={setDevelopers} list={developers} allList={allDevelopers}/>
            <AddGameAttributeModal modalId="publisherModal" modalName="Publisher" listSetter={setPublishers} list={publishers} allList={allPublishers}/>
        </>

    )
}

export default GameForm;
