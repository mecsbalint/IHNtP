/* eslint-disable react/prop-types */

function GameProfile({game, isLoggedIn, onClickListButton}) {

    return (
        <div className="card shadow-md bg-base-100">
            <div className="carousel">
                {game.screenshots.map((screenshot, index) => {
                    return (
                        <div key={screenshot} className="carousel-item w-full rounded-t-box">
                            <img
                                src={screenshot}
                                className="w-full rounded-t-box"
                                alt={`${game.name} screenshot no. ${index + 1}`}
                            />
                        </div>
                    );
                })}
            </div>
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
                <div>{game.releaseDate.split("-").join(" ")}</div>
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
                <div className={`card-actions justify-end ${isLoggedIn && game.inWishlist !== null && game.inBacklog !== null ? "" : "hidden"}`}>
                    <button className={`btn btn-primary ${game.inBacklog ? "hidden" : ""}`} onClick={() => onClickListButton("PUT", "backlog")}>Add to Backlog</button>
                    <button className={`btn btn-primary ${game.inBacklog ? "" : "hidden"}`} onClick={() => onClickListButton("DELETE", "backlog")}>Remove from Backlog</button>

                    <button className={`btn bg-amber-400 ${game.inWishlist ? "hidden" : ""}`} onClick={() => onClickListButton("PUT", "wishlist")}>Add to Wishlist</button>
                    <button className={`btn bg-amber-400 ${game.inWishlist ? "" : "hidden"}`} onClick={() => onClickListButton("DELETE", "wishlist")}>Remove from Wishlist</button>
                </div>
            </div>
        </div>
    )
}

export default GameProfile;
