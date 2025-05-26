import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import GameProfile from "../components/GameProfile/GameProfile";
import useUnauthorizedHandler from "../hooks/useUnauthorizedHandler";
import { getGameForProfile } from "../services/gameService";
import { getGameStatuses, updateUserList } from "../services/userGameService";
import { useAuthContext } from "../hooks/useAuthContext";

function GamePage() {
    const {id} = useParams();
    const [game, setGame] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const handleUnauthorizedResponse = useUnauthorizedHandler();
    const {user} = useAuthContext();
    
    useEffect(() => {
        setIsLoggedIn(Boolean(user));
    }, [user]);

    useEffect(() => {
        getGameForProfile(id)
            .then(game => {
                if (user) {
                    return getGameStatuses(id, handleUnauthorizedResponse).then(statuses => game = {...game, ...statuses});
                } else return game;
            })
            .then(game => game && setGame(game))
    }, [id]);

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
