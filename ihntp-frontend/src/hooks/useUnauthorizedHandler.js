import { useNavigate } from "react-router-dom";
import { useAuthContext } from "./useAuthContext";

function useUnauthorizedHandler() {
    const navigate = useNavigate();
    const {dispatch} = useAuthContext();

    return function handleUnauthorizedResponse() {
        localStorage.setItem("ihntpUser", null);
        dispatch({type: "LOGOUT"});
        navigate("/login");
    }
}

export default useUnauthorizedHandler;
