import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import GameProfile from "../components/GameProfile/GameProfile";
import useUnauthorizedHandler from "../hooks/useUnauthorizedHandler";
import { getGameForProfile } from "../services/gameService";
import { getGameStatuses, updateUserList } from "../services/userGameService";
import { useAuthContext } from "../hooks/useAuthContext";
import { GameForGameProfile, GameForGameProfileWithStatuses } from "../types/Game";

function GamePage() {
    const id = useParams().id as unknown as number;
    const [game, setGame] = useState<GameForGameProfileWithStatuses | null>(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const handleUnauthorizedResponse = useUnauthorizedHandler();
    const {user} = useAuthContext();
    
    useEffect(() => {
        setIsLoggedIn(Boolean(user));
    }, [user]);

    useEffect(() => {
        getGameForProfile(id)
            .then(game => {
                if (game) {
                    if (user) {
                        return getGameStatuses(id).then(response => {
                            response.status === 401 && handleUnauthorizedResponse();
                            return {...game, ...response.body!};
                        });
                    } else return {...game, inWishlist: null, inBacklog: null};
                }
            })
            .then(game => game && setGame(game));
    }, [id]);

    async function onClickListButton(method: "PUT" | "DELETE", listType: "wishlist" | "backlog") {
        const responseStatus = await updateUserList(method, listType, id);

        if (responseStatus === 401) {
            handleUnauthorizedResponse();
        } else if (responseStatus === 200) {
            listType === "wishlist" && setGame(prevGame => ({
                ...prevGame!,
                inWishlist: method === "PUT"
            }));
            
            listType === "backlog" && setGame(prevGame => ({
                ...prevGame!,
                inBacklog: method === "PUT"
            }));
        }
    }

    return (
        <div className="place-items-center bg-blue-400 rounded-b-box pb-5 pt-5">
            {game && <GameProfile 
                game={game} 
                isLoggedIn={isLoggedIn} 
                onClickListButton={onClickListButton}
            />}
        </div>
    )
}

export default GamePage;
