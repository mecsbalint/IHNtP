import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import GameForm from "../components/GameForm/GameForm";
import { editGame, getGameForEdit } from "../services/gameService";

function EditGamePage() {
    const {id} = useParams();
    const [game, setGame] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        getGameForEdit(id).then(game => setGame(game ?? {}));
    }, [id]);

    async function onSubmit(editGameObj) {
        const isSuccess = await editGame(editGameObj, id);

        isSuccess && navigate(`/game/${id}`);
    }

    return (
        <div className="place-items-center bg-blue-400 rounded-b-box p-10 pt-5">
            <div className="shadow-md bg-base-100 rounded-box p-5">
                <h1>Edit game</h1>
                <GameForm  game={game} onSubmit={onSubmit} buttonText={"Save changes"}/>
            </div>
        </div>
    );
}

export default EditGamePage;
