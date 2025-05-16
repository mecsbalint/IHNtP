import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import GameProfile from "../components/GameProfile/GameProfile";
import useUnauthorizedHandler from "../hooks/useUnauthorizedHandler";
import { getGame } from "../services/gameService";
import { getGameStatuses, updateUserList } from "../services/userGameService";

function GamePage() {
    const {id} = useParams();
    console.log("GamePage param id:", id); 
    const [game, setGame] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const handleUnauthorizedResponse = useUnauthorizedHandler();

    
    useEffect(() => {
        setIsLoggedIn(![null, "null"].includes(localStorage.getItem("ihntpJwt")));
    }, []);

    useEffect(() => {
        getGame(id).then(game => game && setGame(game));        
    }, [id]);

    useEffect(() => {
        isLoggedIn && game && getGameStatuses(id, handleUnauthorizedResponse).then(statuses => setGame(prevGame => ({...prevGame, ...statuses})))
    }, [game, id, isLoggedIn]);

    async function onClickListButton(method, listType) {
        const responseStatus = await updateUserList(method, listType, id, handleUnauthorizedResponse);

        if (responseStatus === 200) {
            listType === "wishlist" && setGame(prevGame => ({
                ...prevGame,
                inWishlist: method === "PUT"
            }));

            listType === "backlog" && setGame(prevGame => ({
                ...prevGame,
                inBacklog: method === "PUT"
            }));
        }
    }

    return (
        <div className="place-items-center bg-blue-400 rounded-b-box p-10 pt-5">
            {game && <GameProfile 
                game={game} 
                isLoggedIn={isLoggedIn} 
                onClickListButton={onClickListButton}
            />}
        </div>
    )
}

export default GamePage;
