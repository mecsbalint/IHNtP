/* eslint-disable react/prop-types */

import { useState, KeyboardEvent } from "react";
import { Tag, TagWithId } from "../../types/Tag";
import { Developer, DeveloperWithId } from "../../types/Developer";
import { Publisher, PublisherWithId } from "../../types/Publisher";

type AddGameAttributeModalPropsTag = {
    modalId: "tagModal",
    modalName: "Tag",
    listSetter:  React.Dispatch<React.SetStateAction<(Tag | TagWithId)[]>>,
    list: Array<Tag | TagWithId>,
    allList: Array<Tag | TagWithId>
};

type AddGameAttributeModalPropsDeveloper = {
    modalId: "developerModal",
    modalName: "Developer",
    listSetter:  React.Dispatch<React.SetStateAction<(Developer | DeveloperWithId)[]>>,
    list: Array<Developer | DeveloperWithId>,
    allList: Array<Developer | DeveloperWithId>
};

type AddGameAttributeModalPropsPublisher = {
    modalId: "publisherModal",
    modalName: "Publisher",
    listSetter:  React.Dispatch<React.SetStateAction<(Publisher | PublisherWithId)[]>>,
    list: Array<Publisher | PublisherWithId>,
    allList: Array<Publisher | PublisherWithId>
};

function AddGameAttributeModal
({ modalId, modalName, listSetter, allList, list } : AddGameAttributeModalPropsTag | AddGameAttributeModalPropsDeveloper | AddGameAttributeModalPropsPublisher ) {
    const [name, setName] = useState("");
    const [errorState, setErrorState] = useState(false);

    function handleAddBtnClick() {
        if (![...allList, ...list].some(att => att.name === name)) {
            const newAttribute = {name: name};
            
            listSetter(prev => {
                const newList = [...prev];
                newList.push(newAttribute);
                return newList;
            });

            setName("");

            (document.getElementById(modalId) as HTMLDialogElement).close();
        } else {
            setErrorState(true);
        }
    }

    function handleKeyDown(event : KeyboardEvent<HTMLInputElement>) {
        if (event.keyCode === 13) {
            event.preventDefault();
            handleAddBtnClick();
        }
    }

    return (
        <dialog id={modalId} className="modal">
            <div className="modal-box w-fit">
                <div className="fieldset w-xs">
                    <legend className="fieldset-legend">New {modalName}</legend>
                    <label className="label">Name</label>
                    <input type="text" className={`input ${errorState && "input-error"}`} value={name} onKeyDown={event => handleKeyDown(event)} required onChange={(event) => {
                        setName(event.target.value);
                        setErrorState(false);
                        }}/>
                    <p className={`text-error ${errorState ? "" : "hidden"}`}>{modalName} is already exist</p>
                    <button className="btn btn-neutral mt-4" onClick={handleAddBtnClick} disabled={errorState || name === ""}>Add</button>
                </div>
            </div>
            <form method="dialog" className="modal-backdrop">
                <button onClick={() => setName("")}>close</button>
            </form>
        </dialog>
    );
}

export default AddGameAttributeModal;
