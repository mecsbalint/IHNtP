import { useState, useEffect } from "react";
import GameList from "../components/GameList/GameList";
import useUnauthorizedHandler from "../hooks/useUnauthorizedHandler";
import { getUserGameList } from "../services/userGameService";

function BacklogPage() {
    const [games, setGames] = useState([]);

    const handleUnauthorizedResponse = useUnauthorizedHandler();

    useEffect(() => {
        getUserGameList("backlog", handleUnauthorizedResponse).then(backlog => setGames(backlog));
    }, []);

    return (
        <div className="place-items-center">
            <GameList games={games} listTitle={"Backlog"}/>
        </div>
    )
}

export default BacklogPage;
