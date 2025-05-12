import { useState, useEffect } from "react";
import GameList from "../components/GameList/GameList";

function WishlistPage() {
    const [games, setGames] = useState([]);

    useEffect(() => {
        fetch('/api/user/games/wishlist')
            .then(response => response.json())
            .then(response => setGames(response));
    }, [])

    return (
        <div className="place-items-center">
            <GameList games={games} />
        </div>
    )
}

export default WishlistPage;
