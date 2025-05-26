/* eslint-disable react/prop-types */

import { useState } from "react";

function AddGameAttributeModal({ modalId, modalName, listSetter, allList, list }) {
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

            document.getElementById(modalId).close();
        } else {
            setErrorState(true);
        }
    }

    function handleKeyDown(event) {
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
