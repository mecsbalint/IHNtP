/* eslint-disable react/prop-types */

import { Link } from "react-router-dom";

function GameListElement({game}) {

    return (
        <li className="list-row">
            <Link>
                <div className="grid grid-cols-2 card card-side bg-base-100 shadow-sm">
                    <figure>
                        <img
                        src={game.headerImg}
                        alt={game.name} />
                                </figure>
                    <div className="card-body">
                        <h2 className="card-title">{game.name}</h2>
                        <p>{game.releaseDate.split("-").join(" ")}</p>
                        <div className="card-actions justify-end">
                            {game.tags.map(tag => tag.name).join(", ")}
                        </div>
                    </div>
                </div>
            </Link>
        </li>
    )
}

export default GameListElement;
