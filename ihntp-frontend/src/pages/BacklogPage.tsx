import { useState, useEffect } from "react";
import GameList from "../components/GameList/GameList";
import useUnauthorizedHandler from "../hooks/useUnauthorizedHandler";
import { getUserGameList } from "../services/userGameService";
import { GameForList } from "../types/Game";

function BacklogPage() {
    const [games, setGames] = useState<GameForList[]>([]);

    const handleUnauthorizedResponse = useUnauthorizedHandler();

    useEffect(() => {
        getUserGameList("backlog").then(response => {
            response.status === 401 && handleUnauthorizedResponse();
            setGames(response.body!);
        });
    }, []);

    return (
        <div className="place-items-center">
            <GameList games={games} listTitle={"Backlog"}/>
        </div>
    )
}

export default BacklogPage;
