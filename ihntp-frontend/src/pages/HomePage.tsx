import { useEffect, useState } from "react";
import GameList from "../components/GameList/GameList";
import { getAllGames } from "../services/gameService";
import { GameForList } from "../types/Game";

function HomePage() {
    const [games, setGames] = useState<GameForList[]>([]);

    useEffect(() => {
        getAllGames().then(allGames => setGames(allGames));
    }, []);

    return (
        <div className="place-items-center">
            <GameList games={games} listTitle={"All games"} />
        </div>
    );
}

export default HomePage;
