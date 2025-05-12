import { useState, useEffect } from "react";
import GameList from "../components/GameList/GameList";
import { useUnauthorizedHandler } from "../utils";

function WishlistPage() {
    const [games, setGames] = useState([]);

    const handleUnauthorizedResponse = useUnauthorizedHandler();

    useEffect(() => {
        fetch('/api/user/games/wishlist', {headers: {Authorization: `Bearer ${localStorage.getItem("ihntpJwt")}`}})
            .then(response => {
                response.status === 401 && handleUnauthorizedResponse();
                return response.status === 200 ? response.json() : [];
            })
            .then(response => setGames(response));
    }, []);

    return (
        <div className="place-items-center">
            <GameList games={games} listTitle={"Wishlist"}/>
        </div>
    )
}

export default WishlistPage;
