import { useNavigate } from "react-router-dom";
import GameForm from "../components/GameForm/GameForm";
import { addNewGame } from "../services/gameService";

function AddGamePage() {
    const navigate = useNavigate();

    async function onSubmit(newGameObj) {
        const newGameId = await addNewGame(newGameObj);

        newGameId && navigate(`/game/${newGameId}`);
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
