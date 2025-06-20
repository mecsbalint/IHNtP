import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import GameForm from "../components/GameForm/GameForm";
import { editGame, getGameForEdit } from "../services/gameService";
import useUnauthorizedHandler from "../hooks/useUnauthorizedHandler";
import { useAuthContext } from "../hooks/useAuthContext";
import { GameForEdit, GameFormSubmit } from "../types/Game";

function EditGamePage() {
    const id = useParams().id as unknown as number;
    const {isLoggedIn} = useAuthContext();
    const [game, setGame] = useState<GameForEdit | null>(null);
    const navigate = useNavigate();
    const handleUnauthorizedResponse = useUnauthorizedHandler();
    
    useEffect(() => {
        isLoggedIn !== null && !isLoggedIn && navigate("/login");
    }, [isLoggedIn, navigate]);

    useEffect(() => {
        getGameForEdit(id, handleUnauthorizedResponse).then(game => setGame(game));
    }, [id]);

    async function onSubmit(editGameObj : GameFormSubmit) {
        const isSuccess = await editGame(editGameObj, id, handleUnauthorizedResponse);

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
