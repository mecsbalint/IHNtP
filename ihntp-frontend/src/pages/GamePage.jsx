import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import GameProfile from "../components/GameProfile/GameProfile";
import useUnauthorizedHandler from "../hooks/useUnauthorizedHandler";

function GamePage() {
    const {id} = useParams();
    const [game, setGame] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [wishlistStatus, setWishlistStatus] = useState(null);
    const [backlogStatus, setbacklogStatus] = useState(null);

    const handleUnauthorizedResponse = useUnauthorizedHandler();

    useEffect(() => {
        setIsLoggedIn(![null, "null"].includes(localStorage.getItem("ihntpJwt")));
    }, []);

    useEffect(() => {
        isLoggedIn && getGameStatus();
        
        async function getGameStatus() {
            const response = await fetch(`/api/user/games/status/${id}`,  {headers: {Authorization: `Bearer ${localStorage.getItem("ihntpJwt")}`}});

            switch (response.status) {
                case 401:
                    handleUnauthorizedResponse();
                    break;
                case 200: {
                    const responseBody = await response.json();
                    setWishlistStatus(responseBody.inWishlist);
                    setbacklogStatus(responseBody.inBacklog);
                    break;
                }
            }
        }

    }, [isLoggedIn, id, handleUnauthorizedResponse]);

    useEffect(() => {
        fetch(`/api/games/${id}`)
            .then(response => response.json())
            .then(response => setGame(response));
    }, [id]);

    async function onClickListButton(method, listType) {
        const response = await fetch(`/api/user/games/${listType}/${id}`,  {method: method, headers: {Authorization: `Bearer ${localStorage.getItem("ihntpJwt")}`}});

        switch (response.status) {
            case 401:
                handleUnauthorizedResponse();
                break;
            case 200: {
                listType === "wishlist" && setWishlistStatus(method === "PUT");
                listType === "backlog" && setbacklogStatus(method === "PUT");
                break;
            }
        }
    }

    return (
        <div className="place-items-center bg-blue-400 rounded-b-box p-10 pt-5">
            {game && <GameProfile 
                game={game} 
                isLoggedIn={isLoggedIn} 
                inBacklog={backlogStatus} 
                inWishlist={wishlistStatus}
                onClickListButton={onClickListButton}
            />}
        </div>
    )
}

export default GamePage;
