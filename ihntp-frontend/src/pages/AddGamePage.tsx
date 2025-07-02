import { useNavigate } from "react-router-dom";
import GameForm from "../components/GameForm/GameForm";
import { addNewGame } from "../services/gameService";
import useUnauthorizedHandler from "../hooks/useUnauthorizedHandler";
import { useEffect } from "react";
import { useAuthContext } from "../hooks/useAuthContext";
import { GameFormSubmit } from "../types/Game";

function AddGamePage() {
    const navigate = useNavigate();
    const handleUnauthorizedResponse = useUnauthorizedHandler();
    const {isLoggedIn} = useAuthContext();

    useEffect(() => {
        isLoggedIn !== "null" || !isLoggedIn && navigate("/login");
    }, [isLoggedIn, navigate]);

    async function onSubmit(newGameObj : GameFormSubmit) {
        const response = await addNewGame(newGameObj);

        response.status === 401 && handleUnauthorizedResponse();

        response.status === 201 && navigate(`/game/${response.body}`);
    }

    return (
        <div className="place-items-center bg-blue-400 rounded-b-box p-10 pt-5">
            <div className="shadow-md bg-base-100 rounded-box p-5">
                <h1>Add new game</h1>
                <GameForm  game={null} onSubmit={onSubmit} buttonText={"Add new game"}/>
            </div>
        </div>
    )
}

export default AddGamePage;
