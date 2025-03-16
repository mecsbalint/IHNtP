import { useEffect, useState } from "react";
import GameList from "../components/GameList/GameList";

function HomePage() {
    const [games, setGames] = useState([]);

    useEffect(() => {
        // fetch('/api/games/all')
        //     .then(response => response.json())
        //     .then(response => setGames(response));
    }, [])

    return (
        <>
            <GameList games={games} />
            <button className="btn btn-primary">Button</button>
        </>
    )
}

export default HomePage;
