import { ChangeEvent, useEffect, useState } from "react";

type AddScreenshotModalProps = {
    modalId: string,
    list: (File | string)[],
    listSetter: React.Dispatch<React.SetStateAction<(File | string)[]>>
}

function AddScreenshotModal({modalId, list, listSetter} : AddScreenshotModalProps) {
    const [link, setLink] = useState("");
    const [errorMsg, setErrorMsg] = useState("");
    const [validationStatus, setValidationStatus] = useState<boolean | null>(null);

    async function handleAddBtnClick() {
        if (list.includes(link)) {
            setErrorMsg("The image has already added to the list");
        } else {
            setLink("");
            listSetter(prev => [...prev, link]);
            (document.getElementById(modalId) as HTMLDialogElement).close();
        }
    }

    function handleOnChange(event : ChangeEvent<HTMLInputElement>) {
        setLink(event.target.value);
        setErrorMsg("");
    }

    return (
        <dialog id={modalId} className="modal">
            <div className="modal-box w-fit">
                <div className="fieldset w-xs">
                    <legend className="fieldset-legend">Link</legend>
                    <input type="text" className="input" value={link} onChange={event => handleOnChange(event)}></input>
                    <p className={`text-error ${errorMsg ? "" : "hidden"}`}>{errorMsg}</p>
                    <div className="w-full" title={!validationStatus ? "The link provided does not show an image or is unavailable." : ""}>
                        <button disabled={!validationStatus} className="btn btn-neutral mt-4 w-full" onClick={handleAddBtnClick}>Add</button>
                    </div>
                </div>
            </div>
            <form method="dialog" className="modal-backdrop">
                <button onClick={() => {setLink(""); setErrorMsg("")}}>close</button>
            </form>
            <img className="hidden" onError={() => setValidationStatus(false)} onLoad={() => setValidationStatus(true)} src={link}/>
        </dialog>
    )
}

export default AddScreenshotModal;
