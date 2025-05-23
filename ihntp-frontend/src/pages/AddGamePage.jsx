import GameForm from "../components/GameForm/GameForm";

function AddGamePage() {

    return (
        <div className="place-items-center bg-blue-400 rounded-b-box p-10 pt-5">
            <div className="shadow-md bg-base-100 rounded-box p-5">
                <h1>Add new game</h1>
                <GameForm  game={{}}/>
            </div>
        </div>
    )
}

export default AddGamePage;
