import { useState, useEffect } from "react";
import GameList from "../components/GameList/GameList";
import useUnauthorizedHandler from "../hooks/useUnauthorizedHandler";

function BacklogPage() {
    const [games, setGames] = useState([]);

    const handleUnauthorizedResponse = useUnauthorizedHandler();

    useEffect(() => {
        fetch('/api/user/games/backlog', {headers: {Authorization: `Bearer ${localStorage.getItem("ihntpJwt")}`}})
            .then(response => {
                response.status === 401 && handleUnauthorizedResponse();
                return response.status === 200 ? response.json() : response;
            })
            .then(response => setGames(response));
    }, []);

    return (
        <div className="place-items-center">
            <GameList games={games} listTitle={"Backlog"}/>
        </div>
    )
}

export default BacklogPage;
