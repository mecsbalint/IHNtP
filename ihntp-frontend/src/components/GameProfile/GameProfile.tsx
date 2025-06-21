/* eslint-disable react/prop-types */
import { Link } from "react-router-dom";
import editIcon from "../../assets/edit_icon.png";
import { GameForGameProfileWithStatuses } from "../../types/Game";
import { formDate, imagePathFormatter } from "../../utils/utils";
import placeholderScreenshot from "../../assets/placegolder_screenshot.png";

type GameProfileProps = {
    game: GameForGameProfileWithStatuses,
    isLoggedIn: boolean,
    onClickListButton: (method: "PUT" | "DELETE", listType: "backlog" | "wishlist") => Promise<void>
}

function GameProfile({game, isLoggedIn, onClickListButton} : GameProfileProps) {

    return (
        <div className="card shadow-md bg-base-100">
            <div className="carousel">
                {game.screenshots.map((screenshot, index) => {
                    return (
                        <div key={screenshot} className="carousel-item w-full rounded-t-box">
                            <img
                                src={imagePathFormatter(screenshot)}
                                className="w-full rounded-t-box"
                                alt={`${game.name} screenshot no. ${index + 1}`}
                            />
                        </div>
                    );
                })}
                {game.screenshots.length === 0 && (
                        <div key="placeholder" className="carousel-item w-full rounded-t-box">
                            <img
                                src={placeholderScreenshot}
                                className="w-full rounded-t-box"
                                alt="placeholder image"
                            />
                        </div>
                    )
                }
            </div>
            {game.gamePrices &&
            <div className="stats shadow m-2">
                    <div className="stat">
                        <div className="stat-title">
                            Current Best
                        </div>
                        <Link to={game.gamePrices.current.shopUrl}>
                            {game.gamePrices.current.amount} {game.gamePrices.current.currency}
                        </Link>
                    </div>
                    <div className="stat">
                        <div className="stat-title">
                            Historical Low
                        </div>
                        <Link to={game.gamePrices.allTime.shopUrl}>
                            {game.gamePrices.allTime.amount} {game.gamePrices.allTime.currency}
                        </Link>
                    </div>
            </div>
    
            }
            <div className="card-body">
                <div className="flex justify-between w-full">
                    <span className="card-title inline">{game.name}</span>
                    <div>
                        {game.tags.map(tag => {
                            return (
                                <div key={tag.id} className="badge badge-outline badge-info ml-1">{tag.name}</div>
                            )
                        })}
                    </div>
                </div>
                <div>{formDate(game.releaseDate)}</div>
                <div>
                    {game.developers.map((developer, index) => {
                        return (
                            <>
                                <span key={developer.id}>{developer.name}</span>
                                {index < (game.developers.length - 1) && ", "}
                            </>
                        )
                    })}
                </div>
                <div>
                    {game.publishers.map((publisher, index) => {
                        return (
                            <>
                                <span key={publisher.id}>{publisher.name}</span>
                                {index < (game.publishers.length - 1) && ", "}
                            </>
                        )
                    })}
                </div>
                <div>
                    <p>{game.descriptionLong}</p>
                </div>
                <div className={`card-actions justify-between ${isLoggedIn && game.inWishlist !== null && game.inBacklog !== null ? "" : "hidden"}`}>
                    <Link to={`/games/edit/${game.id}`}>
                        <img src={editIcon} className="w-10 h-10 border-2 rounded-full" />
                    </Link>
                    
                    <div className="flex gap-2">
                        <button className={`btn btn-primary ${game.inBacklog ? "hidden" : ""}`} onClick={() => onClickListButton("PUT", "backlog")}>Add to Backlog</button>
                        <button className={`btn btn-primary ${game.inBacklog ? "" : "hidden"}`} onClick={() => onClickListButton("DELETE", "backlog")}>Remove from Backlog</button>

                        <button className={`btn bg-amber-400 ${game.inWishlist ? "hidden" : ""}`} onClick={() => onClickListButton("PUT", "wishlist")}>Add to Wishlist</button>
                        <button className={`btn bg-amber-400 ${game.inWishlist ? "" : "hidden"}`} onClick={() => onClickListButton("DELETE", "wishlist")}>Remove from Wishlist</button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default GameProfile;
