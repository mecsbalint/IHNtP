/* eslint-disable react/prop-types */
import { Link } from "react-router-dom";
import { GameForList } from "../../types/Game";
import { formDate, imagePathFormatter } from "../../utils/utils";
import placeholderHeaderImg from "../../assets/placegolder_header_img.png";

type GameListElementProps = {
    game: GameForList
}

function GameListElement({game} : GameListElementProps) {

    return (
        <li className="list-row">
            <Link to={`/game/${game.id}`}>
                <div className="grid grid-cols-2 card card-side bg-base-100 shadow-sm">
                    <figure>
                        <img
                        src={game.headerImg ? imagePathFormatter(game.headerImg) : placeholderHeaderImg}
                        alt={game.name} />
                                </figure>
                    <div className="card-body">
                        <h2 className="card-title">{game.name}</h2>
                        <p>{formDate(game.releaseDate)}</p>
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
