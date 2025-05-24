import { useState } from "react";
import { useParams } from "react-router-dom";

function EditGamePage() {
    const {id} = useParams();
    const [game, setGame] = useState({});
}

export default EditGamePage;
