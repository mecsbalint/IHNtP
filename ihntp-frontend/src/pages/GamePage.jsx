import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import GameProfile from "../components/GameProfile/GameProfile";

function GamePage() {
    const {id} = useParams();
    const [game, setGame] = useState(null);

    useEffect(() => {
        fetch(`/api/games/${id}`)
            .then(response => response.json())
            .then(response => setGame(response));
    }, [id])

    return (
        <div className="place-items-center bg-blue-400 rounded-box p-10">
            {game && <GameProfile game={game}/>}
        </div>
    )
}

export default GamePage;
